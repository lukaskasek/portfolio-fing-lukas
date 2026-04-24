package controllers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import excepciones.UsuarioNoExisteException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;

@WebServlet("/sideBarAerolinea")
public class BarraLateralServletA extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtén las categorías u otra información necesaria
        List<String> categorias = obtenerCategorias(); // Implementa este método
        request.setAttribute("categorias", categorias);
        
        // En lugar de forward, use include
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/template/sideBars/sideBarAerolinea.jsp");
        dispatcher.include(request, response);
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
}
