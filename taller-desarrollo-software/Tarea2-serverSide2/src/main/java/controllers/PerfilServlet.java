package controllers;

import java.io.IOException;
import java.util.List;

import excepciones.RutaNoExisteException;
import excepciones.VueloNoExisteException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.DTAerolinea;
import logica.DTCliente;
import logica.DTUser;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.IControladorUsuarios;
@WebServlet ("/perfil")

public class PerfilServlet extends HttpServlet {
	public PerfilServlet() {
        super();
        // TODO Auto-generated constructor stub
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nick = request.getParameter("nick");
		Fabrica f = Fabrica.getInstancia();
		IControladorUsuarios iCU = f.getIControladorUsuarios();
		
		DTUser user = iCU.mostrarDatosUsuario(nick.trim());
		System.out.println("Tipo de usuario: "+user.getClass().getName());

		if(user != null) {
			request.setAttribute("perfil", user);
			if(user instanceof DTCliente) {
				
				List<String> reservas;
				try {
					reservas = iCU.mostrarReservasVuelos(nick.trim());
					request.setAttribute("reservas", reservas);
				} catch (VueloNoExisteException e) {
					 //TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("/WEB-INF/usuarios/PerfilCliente.jsp").forward(request, response);
			}else if(user instanceof DTAerolinea) {
				IControladorRutasDeVuelo iCRV = f.getIControladorRutasDeVuelo();
				
					List<List<String>> confirmadas = iCRV.listarDatosRutasConfirmadas(nick.trim());
					List<List<String>> ingresadas = iCRV.listarDatosRutasIngresadas(nick.trim());
					List<List<String>> rechazadas = iCRV.listarDatosRutasRechazadas(nick.trim());

					//List<String> ingresadas = iCRV.listarRutasDeVueloIngresadas(nick.trim());
					request.setAttribute("confirmadas",confirmadas);
					request.setAttribute("ingresadas", ingresadas);
					request.setAttribute("rechazadas", rechazadas);
			
				request.getRequestDispatcher("/WEB-INF/usuarios/PerfilAerolinea.jsp").forward(request,response);
				
			}
		}else {
			System.out.println("no se encontro");
		}
	}
}
