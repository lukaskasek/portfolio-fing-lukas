package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import excepciones.RutaNoExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.VueloNoExisteException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.DTUser;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.IControladorUsuarios;

@WebServlet("/reservarVuelo")
public class ReservaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReservaServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Fabrica fabrica = Fabrica.getInstancia();
            IControladorUsuarios controladorUsuarios = fabrica.getIControladorUsuarios();
            IControladorRutasDeVuelo controladorRutas = fabrica.getIControladorRutasDeVuelo();

            // Obtener la lista de aerolíneas
            List<String> aerolineas = controladorUsuarios.listarAerolineas();
            request.setAttribute("aerolineas", aerolineas);

            // Verificar si se ha seleccionado una aerolínea
            String aerolineaSeleccionada = request.getParameter("aerolinea");
            if (aerolineaSeleccionada != null && !aerolineaSeleccionada.trim().isEmpty()) {
                // Obtener rutas de vuelo para la aerolínea seleccionada
                List<String> rutas = controladorRutas.listarRutasDeVueloConfirmadas(aerolineaSeleccionada);
                request.setAttribute("rutas", rutas);
            }

            // Verificar si se ha seleccionado una ruta de vuelo
            String rutaSeleccionada = request.getParameter("rutaVuelo");
            if (rutaSeleccionada != null && !rutaSeleccionada.trim().isEmpty()) {
                // Obtener vuelos para la ruta seleccionada
                List<String> vuelos = controladorRutas.listarVuelos(rutaSeleccionada);
                request.setAttribute("vuelos", vuelos);
            }

            // Reenviar a la página JSP
            request.getRequestDispatcher("/WEB-INF/usuarios/reservarVuelo.jsp").forward(request, response);
        } catch (UsuarioNoExisteException | RutaNoExisteException | VueloNoExisteException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Procesar solo la reserva completa
        if (request.getParameter("vuelo") != null && !request.getParameter("vuelo").isEmpty()) {
            try {
                Fabrica f = Fabrica.getInstancia();
                IControladorRutasDeVuelo iCR = f.getIControladorRutasDeVuelo();

                // Obtener el usuario de la sesión
                DTUser usuario = (DTUser) request.getSession().getAttribute("user");
                String cliente = usuario.getNick(); // O el campo que almacene el identificador del cliente

                // Obtener los parámetros del formulario
                String tipoAsiento = request.getParameter("tipoAsiento");
                String vuelo = request.getParameter("vuelo");
                int cantPasajeros = Integer.parseInt(request.getParameter("cantidadPasajes"));
                int equipajeExtra = Integer.parseInt(request.getParameter("equipajeExtra"));
                LocalDate fechaReserva = LocalDate.parse(request.getParameter("fechaReserva"));

                // Procesar la reserva
                if (cantPasajeros == 1) {
                    iCR.ingresarDatosVuelo(cliente, tipoAsiento, equipajeExtra, fechaReserva, vuelo);
                } else {
                    Set<String[]> pasajerosExtras = new HashSet<>();
                    for (int i = 2; i <= cantPasajeros; i++) {
                        String nombre = request.getParameter("nombrePasajero" + i);
                        String apellido = request.getParameter("apellidoPasajero" + i);
                        pasajerosExtras.add(new String[]{nombre, apellido});
                    }
                    iCR.ingresarNombres(cliente, tipoAsiento, equipajeExtra, fechaReserva, vuelo, cantPasajeros, pasajerosExtras);
                }

                // Reserva exitosa
                request.setAttribute("message", "Reserva realizada con éxito.");
                request.getRequestDispatcher("/WEB-INF/usuarios/confirmarReserva.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("message", "Error al realizar la reserva: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/usuarios/reservarVuelo.jsp").forward(request, response);
            }
        } else {
            // Si no se seleccionó un vuelo válido, volver a cargar el formulario
            doGet(request, response);
        }
    }
}
