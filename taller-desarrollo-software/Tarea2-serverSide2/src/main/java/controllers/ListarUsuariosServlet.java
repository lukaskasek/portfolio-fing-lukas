package controllers;

import java.io.IOException;
import java.util.List;

import excepciones.UsuarioNoExisteException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.EstadoSesion;
import logica.Fabrica;
import logica.IControladorUsuarios;
@WebServlet ("/usuarios")

public class ListarUsuariosServlet extends HttpServlet {
	
	
	public ListarUsuariosServlet() {
        super();
        // TODO Auto-generated constructor stub
 }
	public static void initSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("estado_sesion") == null) {
			session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
		}
	}
	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//al principio solo tengo que mostrar el home pero primero consulto las rutas confirmadas
		Fabrica f = Fabrica.getInstancia();
		IControladorUsuarios iCU = f.getIControladorUsuarios();
		try {
			List<String> usuarios = iCU.listarUsuarios();
			req.setAttribute("usuarios", usuarios);
		} catch (UsuarioNoExisteException e) {
			// TODO Auto-generated catch block
			req.setAttribute("no-users", e.getMessage());
			e.printStackTrace();
		}
		req.getRequestDispatcher("/WEB-INF/usuarios/ListarUsuarios.jsp").
		forward(req, resp);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
