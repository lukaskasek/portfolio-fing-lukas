package controllers;

import logica.EstadoSesion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import CSVs.CargarDatos;
import excepciones.UsuarioNoExisteException;

@WebServlet ("/home")
public class Home extends HttpServlet{
	//private static final long serialVersionUID = 1L;
	 public Home() {
	        super();
	        // TODO Auto-generated constructor stub
	 }
	
	 /**
		 * inicializa la sesión si no estaba creada 
		 * @param request 
		 */
		public static void initSession(HttpServletRequest request) {
			HttpSession session = request.getSession();
			if (session.getAttribute("estado_sesion") == null) {
				session.setAttribute("estado_sesion", EstadoSesion.NO_LOGIN);
			}
		}
		/**
		 * Devuelve el estado de la sesión
		 * @param request
		 * @return 
		 */
		public static EstadoSesion getEstado(HttpServletRequest request)
		{
			return (EstadoSesion) request.getSession().getAttribute("estado_sesion");
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
    
	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//al principio solo tengo que mostrar el home pero primero consulto las rutas confirmadas
		initSession(req);
		
		//Cargar los datos
	    CargarDatos cargador = new CargarDatos();
	    cargador.cargar();
	    
	 // Cargo las rutas de vuelo confirmadas para mostrarlas en el menu
      Fabrica fabrica = Fabrica.getInstancia();
      IControladorRutasDeVuelo controladorRutas = fabrica.getIControladorRutasDeVuelo();
      List<List<String>> rutasConfirmadas = controladorRutas.listarDatosRutasConfirmadas();
      req.setAttribute("rutasConfirmadas", rutasConfirmadas);
      List<String> categorias = obtenerCategorias();
      req.setAttribute("categorias", categorias);
	    
		req.getRequestDispatcher("/WEB-INF/home/MenuVisitante.jsp").forward(req, resp);
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
