package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L; // Siempre es buena práctica incluir un serialVersionUID.

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain"); // Establece el tipo de contenido de la respuesta.
        response.getWriter().write("Servlet is working!"); // Escribe un mensaje de respuesta.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Si necesitas manejar POST, puedes implementar esto.
        doGet(request, response); // Redirigir POST a GET (opcional).
    }
}
