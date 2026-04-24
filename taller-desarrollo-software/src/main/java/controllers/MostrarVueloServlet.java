package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.DTCliente;
import logica.DTVuelo;
import excepciones.VueloNoExisteException;

@WebServlet("/MostrarVueloServlet")
public class MostrarVueloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MostrarVueloServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Fabrica f = Fabrica.getInstancia();
        IControladorRutasDeVuelo icr = f.getIControladorRutasDeVuelo();
        String nombre = request.getParameter("codigo");
        
        if (nombre != null && !nombre.isEmpty()) {
            try {
                DTVuelo info = icr.mostrarDatosDeVuelo(nombre);
                request.setAttribute("info", info);
                request.setAttribute("imagen", info.getImagen());
            } catch (VueloNoExisteException e) {
                System.out.println("Error: " + e.getMessage());
                request.setAttribute("error", "El vuelo solicitado no existe.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                return; // Salimos del método para evitar continuar
            }
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/usuarios/MostrarVuelo.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Fabrica f = Fabrica.getInstancia();
        IControladorRutasDeVuelo icr = f.getIControladorRutasDeVuelo();
        
        try {
        	Map<String, String[]> parameterMap = request.getParameterMap();

        	for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
        	    String paramName = entry.getKey();
        	    String[] paramValues = entry.getValue();
        	    
        	    System.out.println("Parámetro: " + paramName);
        	    for (String value : paramValues) {
        	        System.out.println("Valor: " + value);
        	    }
        	}
            // 1. Obtener el cliente de la sesión
            DTCliente cliente = (DTCliente) request.getSession().getAttribute("user");
            if (cliente == null) {
                response.sendRedirect("login.jsp"); // Redirigir al login si no está logueado
                return;
            }
            
            // 2. Obtener los parámetros enviados desde el formulario
            String nombreVuelo = request.getParameter("nombreVuelo");
            String tipoAsiento = request.getParameter("tipoAsiento");  // "Ejecutivo" o "Turista"
            int cantidadPasajes = Integer.parseInt(request.getParameter("cantidadPasajes"));
            int equipajeExtra = Integer.parseInt(request.getParameter("equipajeExtra"));
            
            // Convertir el tipo de asiento a booleano
            LocalDate fechaReserva = LocalDate.now();
            if (cantidadPasajes <= 0) {
                throw new IllegalArgumentException("La cantidad de pasajes debe ser mayor a cero.");
            }
            if (cantidadPasajes == 1) {
                // Llamar al método para crear una reserva de un solo pasaje
                icr.ingresarDatosVuelo(cliente.getNick(), tipoAsiento, equipajeExtra, fechaReserva, nombreVuelo);
            }else {
            	 Set<String[]> nombresApellidosPasajeros = new HashSet<>();

                 // Recoger nombres y apellidos de los pasajeros
            	 for (int i = 2; i <= cantidadPasajes; i++) {
                     String nombrePasajero = request.getParameter("nombrePasajero" );
                     System.out.println("el nombre del pasajero  es "+nombrePasajero);
                     String apellidoPasajero = request.getParameter("apellidoPasajero");

                     

                     // Agregar los nombres y apellidos al Set
                     nombresApellidosPasajeros.add(new String[]{nombrePasajero, apellidoPasajero});
                     
                     icr.ingresarNombres(cliente.getNick(), tipoAsiento, equipajeExtra, fechaReserva, nombreVuelo, cantidadPasajes, nombresApellidosPasajeros);
                 }
            	 

            }
            
            // 5. Redirigir a una página de confirmación
            request.setAttribute("mensaje", "Reserva realizada con éxito para el vuelo " + nombreVuelo);
            request.getRequestDispatcher("/WEB-INF/home/MenuCliente.jsp").forward(request, response);
        } catch (Exception e) {
            // Manejar errores y mostrar mensaje en caso de excepción
            System.out.println("Error al realizar la reserva: " + e.getMessage());
            request.setAttribute("error", "Error al realizar la reserva: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
