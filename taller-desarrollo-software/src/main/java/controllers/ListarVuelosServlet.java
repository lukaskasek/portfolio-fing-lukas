package controllers;

import java.io.IOException;
import java.util.List;

import excepciones.VueloNoExisteException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.EstadoSesion;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.Vuelo;

@WebServlet("/Vuelos")
public class ListarVuelosServlet extends HttpServlet {

    public ListarVuelosServlet() {
        super();
    }

    public static void initSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("estado_sesion") == null) {
            session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
        }
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        initSession(req);

        Fabrica f = Fabrica.getInstancia();
        IControladorRutasDeVuelo iCV = f.getIControladorRutasDeVuelo(); 

        try {
            // Obtener la lista de vuelos desde el controlador
            List<Vuelo> vuelos = iCV.listarVueloss();
            req.setAttribute("vuelos", vuelos); // Establecer la lista de vuelos como atributo.
        } catch (VueloNoExisteException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/usuarios/ListarVuelos.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
