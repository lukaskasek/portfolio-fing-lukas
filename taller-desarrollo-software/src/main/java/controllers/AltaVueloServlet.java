package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import excepciones.CategoriaExistenteException;
import excepciones.CiudadExistenteException;
import excepciones.DatosInvalidosException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.Vuelo;
import logica.RutaDeVuelo;
import logica.DTAerolinea;
import logica.DTUser;


@WebServlet("/AltaVuelo")
@MultipartConfig
public class AltaVueloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AltaVueloServlet() {
        super();
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener la instancia del controlador
            Fabrica fabrica = Fabrica.getInstancia();
            IControladorRutasDeVuelo icr = fabrica.getIControladorRutasDeVuelo();
            
            // Obtener el usuario actual de la sesión
            DTAerolinea usuario = (DTAerolinea) request.getSession().getAttribute("user");
            
            // Verificar si el usuario está autenticado
            if (usuario == null) {
                request.setAttribute("error", "Usuario no autenticado.");
                request.getRequestDispatcher("/WEB-INF/usuarios/AltaVuelo.jsp").forward(request, response);
                return;
            }

            // Obtener las rutas confirmadas desde el controlador
            List<RutaDeVuelo> rutasConfirmadas = icr.obtenerRutasConfirmadas();
            
            // Filtrar las rutas según el usuario
            List<RutaDeVuelo> rutasUsuario = new ArrayList<>();
            if (rutasConfirmadas != null) {
                for (RutaDeVuelo ruta : rutasConfirmadas) {
                    if (ruta.getAerolinea().getNickName().equals(usuario.getNick())) {
                        rutasUsuario.add(ruta);
                    }
                }
            }
            
            // Pasar las rutas filtradas al JSP para ser mostradas
            request.setAttribute("rutasUsuario", rutasUsuario);
            
            // Redirigir a la página JSP de alta de vuelo
            request.getRequestDispatcher("/WEB-INF/usuarios/AltaVuelo.jsp").forward(request, response);
        } catch (Exception e) {
            // Manejar la excepción, opcionalmente puedes establecer un mensaje de error
            request.setAttribute("error", "Ocurrió un error al cargar las rutas de vuelo.");
            request.getRequestDispatcher("/WEB-INF/usuarios/AltaVuelo.jsp").forward(request, response);
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la instancia del controlador directamente en este método
    	Fabrica F = Fabrica.getInstancia();
        IControladorRutasDeVuelo icr = F.getIControladorRutasDeVuelo();
        System.out.println("aca llego");
        // Obtener los parámetros del formulario
        DTUser usuario = (DTUser) request.getSession().getAttribute("user");

        String aerolinea = usuario.getName();
        String rutaSeleccionada = request.getParameter("ruta");
        String nombreVuelo = request.getParameter("nombreVuelo");
        String fechaVuelo = request.getParameter("fecha");
        String duracion = request.getParameter("duracion");
        String asientosTurista = request.getParameter("asientosTurista");
        String asientosEjecutivo = request.getParameter("asientosEjecutivo");
        
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
        // Manejo de la imagen, validaciones, etc.

        try {
            LocalDate fechaAlta = LocalDate.now();  
            icr.ingresarDatos(aerolinea, rutaSeleccionada, LocalDate.parse(fechaVuelo), fechaAlta, 
                              nombreVuelo, Integer.parseInt(asientosTurista), 
                              Integer.parseInt(asientosEjecutivo), Integer.parseInt(duracion));
            Vuelo v = icr.obtenerVuelo(nombreVuelo);
            if (imagePath != null) {
                v.setImagen(imagePath); 
            }
            request.getRequestDispatcher("/WEB-INF/home/MenuAerolinea.jsp").
    		forward(request, response);
        } catch (DatosInvalidosException e) {
            request.setAttribute("error", "Los datos ingresados son inválidos: " + e.getMessage());
           
        } catch (Exception e) {
            request.setAttribute("error", "Ocurrió un error al registrar el vuelo: " + e.getMessage());
            
        }
    }
}
