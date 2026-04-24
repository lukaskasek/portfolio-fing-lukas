import socket
from socket import *
import xml.etree.ElementTree as ET


class Client:
    def __init__(self):
        self.clientSocket = None
        
    def connect(self, address, port):
        self.address = address
        self.port = port
        self.clientSocket = socket(AF_INET, SOCK_STREAM)
        self.clientSocket.connect((address, port))
        return self

    # ------- funciones d helpers XML-RPC -------

    def _serialize_value(self, pyobj):
        """Python -> <value>...</value> (XML-RPC) como string."""
        v = ET.Element("value")
        if isinstance(pyobj, bool):
            t = ET.SubElement(v, "boolean"); t.text = "1" if pyobj else "0"
        elif isinstance(pyobj, int) and not isinstance(pyobj, bool):
            t = ET.SubElement(v, "int"); t.text = str(pyobj)
        elif isinstance(pyobj, float):
            t = ET.SubElement(v, "double"); t.text = repr(pyobj)
        elif isinstance(pyobj, str):
            t = ET.SubElement(v, "string"); t.text = pyobj
        elif isinstance(pyobj, (list, tuple)):
            arr = ET.SubElement(v, "array")
            data = ET.SubElement(arr, "data")
            for item in pyobj:
                data.append(ET.fromstring(self._serialize_value(item)))
        elif isinstance(pyobj, dict):
            st = ET.SubElement(v, "struct")
            for k, val in pyobj.items():
                m = ET.SubElement(st, "member")
                name = ET.SubElement(m, "name"); name.text = str(k)
                m.append(ET.fromstring(self._serialize_value(val)))
        else:
            t = ET.SubElement(v, "string"); t.text = str(pyobj)
        return ET.tostring(v, encoding="utf-8").decode("utf-8")

    def _parse_value(self, value_elem):
        """<value>...</value> -> Python"""
        if value_elem is None:
            return None
        typed_child = None
        for c in value_elem:
            typed_child = c
            break
        if typed_child is None:
            return value_elem.text

        tag = typed_child.tag
        if tag in ("int", "i4"):
            return int(typed_child.text)
        if tag == "boolean":
            return bool(int(typed_child.text))
        if tag == "double":
            return float(typed_child.text)
        if tag == "string":
            return "" if typed_child.text is None else typed_child.text
        if tag == "array":
            data = typed_child.find("data")
            vals = []
            if data is not None:
                for v in data.findall("value"):
                    vals.append(self._parse_value(v))
            return vals
        if tag == "struct":
            d = {}
            for m in typed_child.findall("member"):
                name = m.findtext("name")
                d[name] = self._parse_value(m.find("value"))
            return d
        return typed_child.text

    def __getattr__(self, name):
        # Devuelve un callable que encapsula todo
        def method(*args):
            # construir <params> serializando x TIPOS (no todo como string)
            #MANDA LA REQUEST
            params_xml = ""
            for arg in args:
                params_xml += f"<param>{self._serialize_value(arg)}</param>"

            body = f"""<?xml version="1.0"?>
                <methodCall>
                <methodName>{name}</methodName>
                <params>{params_xml}</params>
                </methodCall>"""

            body_bytes = body.encode("utf-8")   

            headers_str = (
                "POST /RPC2 HTTP/1.1\r\n"
                f"Host: {self.address}:{self.port}\r\n"
                "Content-Type: text/xml; charset=utf-8\r\n"
                f"Content-Length: {len(body_bytes)}\r\n"
                "Connection: close\r\n"
                "\r\n"
            )
            headers_bytes = headers_str.encode("utf-8")

            self.clientSocket.sendall(headers_bytes + body_bytes)

            #LEE LA RESPONSE
            data = b""
            content_length = 0
            
            # Primero leer los headers HTTP
            while True:
                chunk = self.clientSocket.recv(4096)
                if not chunk:
                    break
                data += chunk
                
                # Buscar el final de los headers (\r\n\r\n)
                header_end = data.find(b"\r\n\r\n")
                if header_end != -1:
                    # Headers completos encontrados
                    headers = data[:header_end].decode("utf-8")
                    body_start = header_end + 4  # +4 para saltar \r\n\r\n
                    
                    # Extraer Content-Length de los headers
                    for line in headers.split("\r\n"):
                        if line.lower().startswith("content-length:"):
                            content_length = int(line.split(":", 1)[1].strip())
                            break
                    
                    if content_length > 0:
                        # Leer el body completo usando Content-Length
                        body_data = data[body_start:]
                        while len(body_data) < content_length:
                            chunk = self.clientSocket.recv(4096)
                            if not chunk:
                                break
                            body_data += chunk
                        
                        # Extraer solo el body (sin headers)
                        response_body = body_data[:content_length].decode("utf-8")
                    else:
                        # Fallback si no hay Content-Length (no debería pasar)
                        response_body = data.decode("utf-8")
                    break
        
            if not response_body:
                response_body = data.decode("utf-8")

            try:
                # Usar directamente el body extraído
                body = response_body
                root = ET.fromstring(body)

                # ¿fault?
                fault = root.find(".//fault")
                if fault is not None:
                    val = fault.find("value")
                    parsed = self._parse_value(val)
                    # parsed debería ser dict con faultCode/faultString
                    if isinstance(parsed, dict):
                        return f"Fault {parsed.get('faultCode')}: {parsed.get('faultString')}"
                    else:
                        return "Fault"

                # valor normal
                value = root.find(".//params/param/value")
                return self._parse_value(value)

            except Exception as e:
                return f"Fault 5: Excepcion generada por el cliente al parsear respuesta: {e}"

        return method
   