package controllers;

import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import excepciones.DatosInvalidosException;
import excepciones.UsuarioYaExisteException;
import excepciones.VueloNoExisteException;
import excepciones.VueloYaExisteException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.DTRutaDeVuelo;
//import logica.DTUser;
//import logica.RutaDeVuelo;

@WebServlet("/rutaDeVuelo")
public class ConsultaDeRutasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ConsultaDeRutasServlet() {
        super();
    } 

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Fabrica fab = Fabrica.getInstancia();
    	IControladorRutasDeVuelo iCRV = fab.getIControladorRutasDeVuelo();
    	String ruta = request.getParameter("route");
    	String categoria = request.getParameter("categoria");
    	if (ruta != null && !ruta.isEmpty()) {
	    	
	    	List<String> vuelos = Arrays.asList("noHayVuelos");
	    	List<String> imaVuelos = Arrays.asList("noHayVuelos");
	    	try {
	    	vuelos = iCRV.listarVuelos(ruta);
	    	imaVuelos = iCRV.imagenesVuelos(ruta);
	    	} catch(VueloNoExisteException e) {
	    		System.out.println(e.getMessage());
	    	}
	
	    	request.setAttribute("vuelos", vuelos);
	    	request.setAttribute("imagenes", imaVuelos);
	    	
	    	List<String> datosRuta = iCRV.obtenerDatosRuta(ruta);
	    	request.setAttribute("datosRuta", datosRuta);
	    	request.getRequestDispatcher("/WEB-INF/usuarios/resultadoConsultaDeRuta.jsp").forward(request, response);
    	}
    	else if (categoria != null && !categoria.isEmpty()) {

    		List<List<String>> datos = iCRV.listarDatosRutasConfirmadasCategoria(categoria);

    		request.setAttribute("datosRutas", datos);
    		request.getRequestDispatcher("/WEB-INF/usuarios/resultadoConsultaRutas.jsp").forward(request, response);
    	}
    }
}
