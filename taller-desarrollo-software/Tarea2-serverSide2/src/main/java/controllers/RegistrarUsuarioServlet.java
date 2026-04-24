package controllers;

import java.io.File;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import excepciones.DatosInvalidosException;
import excepciones.UsuarioYaExisteException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logica.Fabrica;
import logica.IControladorUsuarios;

@MultipartConfig
@WebServlet ("/registroUsuario")
public class RegistrarUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RegistrarUsuarioServlet() {
        super();
        // TODO Auto-generated constructor stub
	}
	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	
		req.getRequestDispatcher("/WEB-INF/usuarios/AltaUsuario.jsp").
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
		String nick = request.getParameter("nickName");
		System.out.println("NickName: "+nick);
		String nombre = request.getParameter("nombre");
		String tipo = request.getParameter("userType");
		System.out.println("tipo de usuario: "+tipo);
		String contraseña = request.getParameter("contraseña");
		String email = request.getParameter("correo");
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
		Fabrica fab = Fabrica.getInstancia();
		IControladorUsuarios iCU = fab.getIControladorUsuarios();
		if(tipo.equalsIgnoreCase("cliente")) {
			
			String apellido = request.getParameter("apellido");
			String fecha = request.getParameter("fechaNacimiento");
			String nacionalidad = request.getParameter("nacionalidad");
			String tipoDoc = request.getParameter("tipoDocumento");
			String numdoc = request.getParameter("numDocumento");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
            	LocalDate fechaAlta = LocalDate.parse(fecha, formatter);
            	try {
					iCU.NuevoCliente(nick, nombre, apellido, email, fechaAlta, nacionalidad, tipoDoc, numdoc, contraseña,imagePath);
					request.getRequestDispatcher("/WEB-INF/usuarios/LoginUsuario.jsp").forward(request, response);
				} catch (UsuarioYaExisteException | DatosInvalidosException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }catch(DateTimeParseException e7) {
            	e7.printStackTrace();
            }
		}else if(tipo.equalsIgnoreCase("aerolinea")) {
			String descripcion = request.getParameter("descripcion");
			String link = request.getParameter("link");
			try {
				iCU.NuevaAerolinea(nick, nombre, email, descripcion, link, contraseña, imagePath);
				request.getRequestDispatcher("/WEB-INF/usuarios/LoginUsuario.jsp").forward(request, response);
			} catch (UsuarioYaExisteException | DatosInvalidosException e) {
				// TODO Auto-generated catch block
				request.setAttribute("error", "Ocurrio un error al ingresar el usuario: " + e.getMessage());
			    request.getRequestDispatcher("/WEB-INF/usuarios/AltaUsuario.jsp").forward(request, response);
				e.printStackTrace();
			}
			
		}
	}
}
