package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.IControladorUsuarios;
import logica.DTAerolinea;
import logica.DTCliente;
import logica.DTUser;
import logica.EstadoSesion;
import excepciones.UsuarioNoExisteException;



@WebServlet("/loginUsuario")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorMessage = (String) req.getAttribute("errorMessage");
        req.setAttribute("errorMessage", errorMessage);
        req.getRequestDispatcher("/WEB-INF/usuarios/LoginUsuario.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // No crear una nueva sesión
        if (session != null && session.getAttribute("user") != null) {
        		Fabrica fab = Fabrica.getInstancia();
        		IControladorRutasDeVuelo iCr = fab.getIControladorRutasDeVuelo();
            DTUser usuario = (DTUser) session.getAttribute("user");
            List<List<String>> rutasConfirmadas = iCr.listarDatosRutasConfirmadas();
            request.setAttribute("rutasConfirmadas", rutasConfirmadas);
            // Redirigir al menú correspondiente según el tipo de usuario
            if (usuario instanceof DTCliente) {
                response.sendRedirect(request.getContextPath() + "/clienteHome");
            } else if (usuario instanceof DTAerolinea) {
                response.sendRedirect(request.getContextPath() + "/aerolineaHome");
            }
        } else {
            processRequest(request, response);
        }
    }
    
    private List<String> obtenerCategorias() {
      // Implementa la lógica para obtener las categorías
  	Fabrica fab = Fabrica.getInstancia();
      IControladorRutasDeVuelo iCr = fab.getIControladorRutasDeVuelo();
      List<String> lista;
	try {
		lista = Arrays.asList(iCr.listarCategorias());
		return lista;
	} catch (UsuarioNoExisteException e) {
		// TODO Auto-generated catch block
		 List<String> listaa = new ArrayList<>();
		 return listaa;
	}
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nick = request.getParameter("nickname");
        String contraseña = request.getParameter("password");
        
        // Validar la entrada
        if (nick == null || nick.trim().isEmpty() || contraseña == null || contraseña.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Por favor, ingrese su nombre de usuario y contraseña.");
            request.getRequestDispatcher("/WEB-INF/usuarios/LoginUsuario.jsp").forward(request, response); // Redirige a la misma página si falta información
            return;
        }

        // Obtener el controlador de usuarios desde la fábrica
        Fabrica fab = Fabrica.getInstancia();
        IControladorUsuarios iCU = fab.getIControladorUsuarios();
        IControladorRutasDeVuelo iCr = fab.getIControladorRutasDeVuelo();
        
        // Verificar si el usuario existe
        DTUser usuario = iCU.mostrarDatosUsuario(nick);
        
        if (usuario == null) {
            // Usuario no encontrado, marcar el error
            HttpSession session = request.getSession();
            session.setAttribute("estado_sesion", EstadoSesion.LOGIN_INCORRECTO);
            request.setAttribute("errorMessage", "Usuario no encontrado.");
            processRequest(request, response);
            return;
        }

        // Validar la contraseña
        if (usuario.getContra().equals(contraseña)) {
            // Guardar el objeto completo del usuario en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("user", usuario); 
            session.setAttribute("estado_sesion", EstadoSesion.LOGIN_CORRECTO);
            List<List<String>> rutasConfirmadas = iCr.listarDatosRutasConfirmadas();
            request.setAttribute("rutasConfirmadas", rutasConfirmadas);
            List<String> categorias = obtenerCategorias();
            request.setAttribute("categorias", categorias);
            // Redirigir según el tipo de usuario
            if (usuario instanceof DTCliente) {
            	request.getRequestDispatcher("/WEB-INF/home/MenuCliente.jsp").
        		forward(request, response); // Ruta actualizada
            } else if (usuario instanceof DTAerolinea) {
            	request.getRequestDispatcher("/WEB-INF/home/MenuAerolinea.jsp").
        		forward(request, response); // Ruta actualizada
            }
        } else {
            // Contraseña incorrecta
            HttpSession session = request.getSession();
            session.setAttribute("estado_sesion", EstadoSesion.LOGIN_INCORRECTO);
            request.setAttribute("errorMessage", "Contraseña incorrecta.");
            System.out.println("contraseña incorrecta, la contraseña es: "+usuario.getContra()+" y lo que ingresaste fue: "+contraseña);
            processRequest(request, response);
        }
    }


}
