package controllers;

import java.io.File;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.nio.file.Paths;

import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.DTUser;
import excepciones.CategoriaExistenteException;
import excepciones.CiudadExistenteException;
import excepciones.DatosInvalidosException;
import excepciones.UsuarioNoExisteException;
import excepciones.VueloYaExisteException;


@MultipartConfig
@WebServlet("/altaRutaDeVuelo")
public class AltaRutaDeVueloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AltaRutaDeVueloServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Fabrica fab = Fabrica.getInstancia();
        IControladorRutasDeVuelo iCr = fab.getIControladorRutasDeVuelo();

        
        try {
            String[] ciudades = iCr.listarCiudades();
            String[] categorias = iCr.listarCategorias();

            request.setAttribute("ciudades", Arrays.asList(ciudades));
            request.setAttribute("categorias", Arrays.asList(categorias));
        } catch (UsuarioNoExisteException e) {
            // Manejar la excepción si es necesario
            e.printStackTrace();
        }

        request.getRequestDispatcher("/WEB-INF/usuarios/AltaRutaDeVuelo.jsp").forward(request, response);
    }


	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Fabrica fab = Fabrica.getInstancia();
    IControladorRutasDeVuelo iCr = fab.getIControladorRutasDeVuelo();

    try {
    	String aerolinea = "esta mal";
        DTUser usuario = (DTUser) request.getSession().getAttribute("user");
        if (usuario != null) {
        	aerolinea = usuario.getNick();
        }
        else {
        	System.out.println("no se agarro bien el nombre");
        }
    	
    	
        String nombre = request.getParameter("nombre");
        String desCorta = request.getParameter("descripcionCorta");
        String descripcion = request.getParameter("descripcion");
        
        LocalTime hora = LocalTime.parse(request.getParameter("hora"));
        
        int costoTurista = Integer.parseInt(request.getParameter("costoBaseTurista"));
        int costoEjecutivo = Integer.parseInt(request.getParameter("costoBaseEjecutivo"));
        int costoEExtra = Integer.parseInt(request.getParameter("costoEquipajeExtra"));
        
        String ciudadOrigen = request.getParameter("ciudadOrigen");
        String ciudadDestino = request.getParameter("ciudadDestino");
        
        LocalDate fechaAlta = LocalDate.parse(request.getParameter("fechaAlta"));
        
        String[] categorias = request.getParameterValues("categorias");
        
        // Manejar la subida de la imagen
        Part filePart = request.getPart("imagen");
        String imagePath = null; // Aquí guardaremos la ruta de la imagen

        // Verificar si se ha subido una imagen
        if (filePart != null && filePart.getSize() > 0) {
            // Obtener el nombre del archivo
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            
            // Definir la ruta donde se guardará la imagen
            String uploadPath = getServletContext().getRealPath("") + "uploads"; // Carpeta uploads en tu proyecto
            
            // Crear la carpeta si no existe
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            // Guardar la imagen en la carpeta
            filePart.write(uploadPath + File.separator + fileName);
            
            // Guardar la ruta de la imagen como atributo del usuario
            imagePath = "uploads/" + fileName;
        }
        
        
        


        iCr.NuevoRutaDeVuelo(aerolinea, nombre, desCorta, descripcion, hora, costoTurista, costoEjecutivo, costoEExtra, ciudadOrigen, ciudadDestino, fechaAlta, categorias);

        
        if (imagePath != null) {
            iCr.setImagen(nombre, imagePath);
        }
        

        //response.sendRedirect("/homeAerolinea");
        request.getRequestDispatcher("/homeAerolinea").forward(request, response);


    } catch (VueloYaExisteException e) {
    	System.out.println("ruta de vuelo ya existe");
    	request.setAttribute("error", "Error: Ya existe una ruta de vuelo con ese nombre");
    	doGet(request, response);
    }
     catch (DatosInvalidosException e) {
    	System.out.println("e1");
        request.setAttribute("error", "Datos inválidos: " + e.getMessage());
        doGet(request, response);
    } catch (NumberFormatException e) {
    	System.out.println("e2");
        request.setAttribute("error", "Error en el formato de los costos");
        doGet(request, response);
    } catch (DateTimeParseException e) {
    	System.out.println("e3");
        request.setAttribute("error", "Error en el formato de la fecha o la hora");
        doGet(request, response);
    } catch (Exception e) {
    	System.out.println("e4");
        request.setAttribute("error", "Error inesperado: " + e.getMessage());
        doGet(request, response);
    }
	}
}

