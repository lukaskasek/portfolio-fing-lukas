import xml
import xml.etree.ElementTree as ET
import socket
import threading
import email.utils

class Server :
    def __init__(self,address,port):
        self.address=address
        self.port=port
        self.procedimientos= {}
        self.server_socket= socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.server_socket.bind((address,port))
        #self.server_socket.listen()
        self.running=True 
    
    def add_method(self,proc1):
        self.procedimientos[proc1.__name__] = proc1

    def serve(self):
        self.server_socket.listen(1) #asi estaba en el libro pero ver si hay que subirlo con mas clientes
        while self.running:
            try:
                conn, addr = self.server_socket.accept()
                threading.Thread(target=self.administrar_cliente, args=(conn,addr)).start()
            except OSError:
                break
        
        
    def administrar_cliente(self, conn, addr):
        """Gestiona un cliente, leyendo requests y enviando respuestas XML-RPC."""
        buffer = b""
        try:
            while True:
                # Leer datos en chunks hasta tener al menos los headers completos
                data = conn.recv(4096)
                if not data:
                    break
                buffer += data

                try:
                    decoded = buffer.decode("utf-8")
                    
                    # Buscar el final de los headers HTTP (\r\n\r\n)
                    header_end = decoded.find("\r\n\r\n")
                    if header_end == -1:
                        # Headers incompletos, seguir leyendo
                        continue
                    
                    # Separar headers y body
                    headers_part = decoded[:header_end]
                    body_part = decoded[header_end + 4:]  # +4 para saltar \r\n\r\n
                    
                    # Validar que sea un POST válido
                    lines = headers_part.split("\r\n")
                    if not lines:
                        continue
                        
                    first_line = lines[0]  # ej: "POST /RPC2 HTTP/1.1"
                    method = first_line.split(" ")[0] if first_line else ""
                    if method.upper() != "POST":
                        resp = (
                            "HTTP/1.1 405 Method Not Allowed\r\n"
                            "Allow: POST\r\n"
                            "\r\n"
                        )
                        conn.sendall(resp.encode("utf-8"))
                        break
                    
                    # Validar Content-Type
                    if not any(line.lower().startswith("content-type: text/xml") for line in lines):
                        resp = (
                            "HTTP/1.1 400 Bad Request\r\n"
                            "Content-Type: text/plain\r\n"
                            "Connection: close\r\n"
                            "\r\n"
                            "Missing or invalid Content-Type header"
                        )
                        conn.sendall(resp.encode("utf-8"))
                        break

                    # Validar header Host (obligatorio en HTTP/1.1)
                    if not any(line.lower().startswith("host:") for line in lines):
                        resp = (
                            "HTTP/1.1 400 Bad Request\r\n"
                            "Content-Type: text/plain\r\n"
                            "Connection: close\r\n"
                            "\r\n"
                            "Missing Host header"
                        )
                        conn.sendall(resp.encode("utf-8"))
                        break

                    # Obtener Content-Length
                    content_length = 0
                    for line in lines:
                        if line.lower().startswith("content-length:"):
                            content_length = int(line.split(":", 1)[1].strip())
                            break
                    
                    if content_length == 0:
                        resp = (
                            "HTTP/1.1 400 Bad Request\r\n"
                            "Content-Type: text/plain\r\n"
                            "Connection: close\r\n"
                            "\r\n"
                            "Missing Content-Length header"
                        )
                        conn.sendall(resp.encode("utf-8"))
                        break

                    # Leer el body completo usando Content-Length
                    body_bytes = body_part.encode("utf-8")
                    while len(body_bytes) < content_length:
                        data = conn.recv(4096)
                        if not data:
                            break
                        body_bytes += data
                    
                    # Convertir a string para procesamiento
                    body = body_bytes[:content_length].decode("utf-8")


                    try:
                        request_xml = ET.fromstring(body)
                    except ET.ParseError as e:
                        # Fault 1: Error parseo de XML
                        print("DEBUG SERVER: ParseError capturado:", e)
                        print("DEBUG SERVER: Body recibido =>", repr(body[:200]))
                        response_xml = self.build_fault(1, "Error parseo de XML")
                    else:
                        try:
                            response_xml = self.administrar_pedido(request_xml)
                        except Exception as e:
                            # Fault 4: Error interno en la ejecución del método
                            response_xml = self.build_fault(4, f"Error interno en la ejecución del método: {str(e)}")

                    # enviar siempre la respuesta (calculando longitud en bytes)
                    response_bytes = response_xml.encode("utf-8")
                    headers = self.http_headers(len(response_bytes)).encode("utf-8")
                    conn.sendall(headers + response_bytes)
                    buffer = b""

                except Exception as e:
                    # Fault 5: Otros errores
                    response_xml = self.build_fault(5, f"Otros errores: {str(e)}")
                    try:
                        response_bytes = response_xml.encode("utf-8")
                        headers = self.http_headers(len(response_bytes)).encode("utf-8")
                        conn.sendall(headers + response_bytes)
                    except Exception:
                        pass
                    buffer = b""
                    break

        finally:
            conn.close()

    def parse_value(self, value_elem):
        """Convierte un nodo <value> en el tipo de Python correspondiente."""
        if value_elem is None:
            return None

        # si tiene hijos tipados (<int>, <string>, etc.)
        typed_child = None
        for c in value_elem:
            typed_child = c
            break

        if typed_child is None:
            # <value>texto</value>
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
                    vals.append(self.parse_value(v))
            return vals
        if tag == "struct":
            d = {}
            for m in typed_child.findall("member"):
                name = m.findtext("name")
                d[name] = self.parse_value(m.find("value"))
            return d

        # default: texto plano del hijo
        return typed_child.text

    def serialize_value(self, pyobj):
        """Convierte un objeto Python en un nodo <value> (XML-RPC)."""
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
                data.append(self.serialize_value(item))
        elif isinstance(pyobj, dict):
            st = ET.SubElement(v, "struct")
            for k, val in pyobj.items():
                m = ET.SubElement(st, "member")
                name = ET.SubElement(m, "name"); name.text = str(k)
                m.append(self.serialize_value(val))
        else:
            # fallback sencillo: string
            t = ET.SubElement(v, "string"); t.text = str(pyobj)
        return v


    def administrar_pedido(self, xml_root):
        """
        Procesa el <methodCall> y construye el <methodResponse>.
        """
        try:
            method_name = xml_root.find("methodName").text
            params = []
            for p in xml_root.findall("params/param"):
                value_elem = p.find("value")
                if value_elem is not None:
                    try:
                        params.append(self.parse_value(value_elem))
                    except Exception:
                        return self.build_fault(3, "Error en parámetros del método invocado")
            
            # Despacho con los procedimients registrados
            if method_name in self.procedimientos:
                try:
                    result = self.procedimientos[method_name](*params)
                    return self.build_response(result)
                except TypeError:
                    return self.build_fault(3, "Error en parámetros del método invocado")
                except Exception as e:
                    return self.build_fault(4, f"Error interno en la ejecución: {str(e)}")
            else:
                return self.build_fault(2, "No existe el método invocado")

        except Exception as e:
            return self.build_fault(5, f"Otros errores: {str(e)}")



    def build_response(self, result):
        # arma <methodResponse><params><param><value>...</value></param></params></methodResponse>
        #para que cumpla especif. XML-RCP
        mr = ET.Element("methodResponse")
        params = ET.SubElement(mr, "params")
        param = ET.SubElement(params, "param")
        param.append(self.serialize_value(result))
        return ET.tostring(mr, encoding="utf-8", xml_declaration=True).decode("utf-8")

    def build_fault(self, code, message):
        return f"""<?xml version="1.0"?>
            <methodResponse>
            <fault>
                <value>
                <struct>
                    <member>
                    <name>faultCode</name>
                    <value><int>{int(code)}</int></value>
                    </member>
                    <member>
                    <name>faultString</name>
                    <value><string>{str(message)}</string></value>
                    </member>
                </struct>
                </value>
            </fault>
            </methodResponse>"""

    def http_headers(self, content_length, status="200 OK"):
        return (
            f"HTTP/1.1 {status}\r\n"
            f"Content-Type: text/xml; charset=utf-8\r\n"
            f"Content-Length: {content_length}\r\n"
            f"Connection: close\r\n"
            f"Date: {email.utils.formatdate(usegmt=True)}\r\n"
            f"Server: SimpleXMLRPC-Python/1.0\r\n"
            f"\r\n"
        )

    def close(self):
        self.running=False
        self.server_socket.close()