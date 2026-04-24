package controllers;

import logica.EstadoSesion;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/homeCliente")
public class HomeCliente extends HttpServlet{
    private static final long serialVersionUID = 1L;
	public HomeCliente() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Fabrica fab = Fabrica.getInstancia();
    IControladorRutasDeVuelo iCr = fab.getIControladorRutasDeVuelo();
		HttpSession session = request.getSession(false); 
	    if (session == null || session.getAttribute("estado_sesion") != EstadoSesion.LOGIN_CORRECTO) {
	        response.sendRedirect(request.getContextPath() + "/loginUsuario");
	    } else {
	    //obtengo las rutas confirmadas para ponerlas en el menu 
	    	List<List<String>> rutasConfirmadas = iCr.listarDatosRutasConfirmadas();
        request.setAttribute("rutasConfirmadas", rutasConfirmadas);
	      request.getRequestDispatcher("/WEB-INF/home/MenuCliente.jsp").forward(request, response);
	    }
	}
}