package controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.EstadoSesion;

@WebServlet("/cerrarSesion")
public class CerrarSesionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CerrarSesionServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Invalidar la sesión del usuario si existe
        if (session != null && session.getAttribute("user")!=null) {
            session.removeAttribute("user");
            session.setAttribute("estado_sesion",EstadoSesion.NO_LOGIN);
        }
        
        // Redirigir al menú de visitante
        response.sendRedirect(request.getContextPath() + "/home");
    }
}

