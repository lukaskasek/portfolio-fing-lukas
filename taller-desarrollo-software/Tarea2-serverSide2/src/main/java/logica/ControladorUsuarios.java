package logica;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import excepciones.DatosInvalidosException;
import excepciones.PaqueteNoExisteException;
import excepciones.RutaNoExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VueloNoExisteException;

public class ControladorUsuarios implements IControladorUsuarios {
	private ManejadorCUsuarios mcu;
	private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

	public ControladorUsuarios() {
		mcu = ManejadorCUsuarios.getInstancia();
	}

	public void NuevoCliente(String nickName, String nombre, String apellido, String Email, LocalDate fechaNac,
			String nacionalidad, String tipoDoc, String nroDoc, String contraseña, String foto)

			throws UsuarioYaExisteException, DatosInvalidosException {
		checkearDatos(nickName, nombre, apellido, Email, fechaNac, nacionalidad, tipoDoc, nroDoc, contraseña, foto);
		if (usuarioYaExiste(nickName)) {
			throw new UsuarioYaExisteException("El usuario con el nickName " + nickName + " ya existe");
		}
		if (usuarioYaExisteMail(Email)) {
			throw new UsuarioYaExisteException("El usuario con el email " + Email + " ya existe");
		}
		Cliente cli = new Cliente(nombre, apellido, nacionalidad, tipoDoc, nickName, Email,
				fechaNac, nroDoc, contraseña, foto);
		mcu.addUser(cli);
	}

	public int cuantasRutasHay(String hhh) {// para probar
		return mcu.cuantasRutasHay(hhh);
	}

	private void checkearDatos(String nickName, String nombre, String apellido, String email, LocalDate fechaNac,
			String nacionalidad, String tipoDoc, String nroDoc, String contra, String foto)
			throws DatosInvalidosException {
		// TODO Auto-generated method stub
		if (nickName.trim().isEmpty()) {
			throw new DatosInvalidosException("El nickName es invalido");

		}
		if (nombre.trim().isEmpty()) {
			throw new DatosInvalidosException("El nombres es invalido, no puede ser vacio.");
		}

		if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
			throw new DatosInvalidosException("El nombres es invalido, solo puede contener letras");
		}
		if (apellido.trim().isEmpty()) {
			throw new DatosInvalidosException("El apellido es invalido, no puede ser vacio.");
		}
		if (!apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
			throw new DatosInvalidosException("El apellido " + apellido + " es invalido, solo puede contener letras");
		}
		if (email.trim().isEmpty()) {
			throw new DatosInvalidosException("El email es invalido, no puede ser vacio ");
		}

		if (!email.matches(EMAIL_REGEX)) {
			throw new DatosInvalidosException("El email es invalido");
		}

		if (fechaNac == null) {
			throw new DatosInvalidosException("La fecha no puede ser vacia");
		}
		if (nacionalidad.trim().isEmpty()) {
			throw new DatosInvalidosException("La nacionalidad es invalidad, no puede ser vacia");
		}

		if (!nacionalidad.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\\\s]+$")) {
			throw new DatosInvalidosException(
					"La nacionalidad " + nacionalidad + " es invalida, solo puede contener letras");
		}
		if (tipoDoc.trim().isEmpty()) {
			throw new DatosInvalidosException("El tipo de documento es invalido, no puede ser vacio");
		}

		if (!tipoDoc.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\\\s]+$")) {
			throw new DatosInvalidosException("El tipo de Documento es invalido. Solo puede contener letras");
		}
		if (nroDoc.trim().isEmpty()) {
			throw new DatosInvalidosException("El numero de Documento es invalido,no puede ser vacio");
		}
		if (!nroDoc.matches("^[A-Z0-9]+$")) {
			throw new DatosInvalidosException("El formato del numero de documento no es valido");
		}
		if (contra.trim().isEmpty()) {
			throw new DatosInvalidosException("La contraseña es invalida,no puede ser vacia");
		}

	}

	public void NuevaAerolinea(String nickName, String nombre, String email, String descripcion, String link,
			String contra, String foto) throws UsuarioYaExisteException, DatosInvalidosException {
		checkearDatosAerolinea(nickName, nombre, email, descripcion, link, contra, foto);
		if (usuarioYaExiste(nickName)) {
			throw new UsuarioYaExisteException("El usuario con el nickName " + nickName + " ya existe.");
		}
		if (usuarioYaExisteMail(email)) {
			throw new UsuarioYaExisteException("El usuario con el email" + email + " ya existe.");
		}
		Aerolinea aer = new Aerolinea(nombre, email, nickName, descripcion, link, contra, foto);

		mcu.addUser(aer);
	}

	private boolean usuarioYaExisteMail(String email) {
		// TODO Auto-generated method stub
		return mcu.usuarioYaExisteMail(email);
	}

	private void checkearDatosAerolinea(String nickName, String nombre, String email, String descripcion, String link,
			String contra, String foto) throws DatosInvalidosException {
		// TODO Auto-generated method stub
		if (nickName.trim().isEmpty()) {
			throw new DatosInvalidosException("El nickName no puede ser vacio");
		}
		if (nombre.trim().isEmpty()) {
			throw new DatosInvalidosException("El nombre no puede ser vacio");

		}
		if (email.trim().isEmpty()) {
			throw new DatosInvalidosException("El email no puede ser vacio");
		}
		if (descripcion.trim().isEmpty()) {
			throw new DatosInvalidosException("La descripcion no puede ser vacia");
		}
		if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
			throw new DatosInvalidosException("El nombres es invalido, solo puede contener letras");
		}
		if (!email.matches(EMAIL_REGEX)) {
			throw new DatosInvalidosException("El email " + email + " es invalido");
		}
		if (contra.trim().isEmpty()) {
			throw new DatosInvalidosException("La contraseña es invalida,no puede ser vacia");
		}

	}

	private boolean usuarioYaExiste(String nickname) {
		ManejadorCUsuarios mcu = ManejadorCUsuarios.getInstancia();
		return mcu.usuarioYaExiste(nickname);
	}

	public List<String> listarUsuarios() throws UsuarioNoExisteException {
		List<String> names = mcu.darNickNamesUsuarios();
		if (names.isEmpty()) {
			throw new UsuarioNoExisteException("no hay usuarios");
		}
		return mcu.darNickNamesUsuarios();
	}

	public DTUser mostrarDatosUsuario(String nickName) {
		return mcu.obtenerUser(nickName);
	}

	public List<String> mostrarNombreRutasDeVuelo(String nickName) throws RutaNoExisteException {
		if (mcu.obtenerNombresRutasDeVuelo(nickName).isEmpty()) {
			throw new RutaNoExisteException("no tiene rutas de vuelo asociadas ");
		}
		return mcu.obtenerNombresRutasDeVuelo(nickName);
	}

	@Override
	public List<String> mostrarReservasVuelos(String nickName) throws VueloNoExisteException {
		// TODO Auto-generated method stub
		List<String> vuelos = mcu.obtenerVuelosReservados(nickName);
		if (vuelos.isEmpty()) {
			throw new VueloNoExisteException("no hay vuelos reservados");
		}
		return vuelos;
	}

	@Override
	public List<String> mostrarPaquetes(String nickName) throws PaqueteNoExisteException {
		// TODO Auto-generated method stub
		if (mcu.obtenerNombresPaquetes(nickName).isEmpty()) {
			throw new PaqueteNoExisteException("no tiene paquetes comprados");
		}
		return mcu.obtenerNombresPaquetes(nickName);
	}

	@Override
	public void editarDatosCliente(String nickName, String nuevoNombre, String nuevoApellido, LocalDate nuevaFechaNac,
			String nuevaNacionalidad, String nuevoTipoDoc, String nuevoNUmDoc) {
		// TODO Auto-generated method stub
		mcu.cambiarDatosCliente(nickName, nuevoNombre, nuevoApellido, nuevaFechaNac, nuevaNacionalidad, nuevoTipoDoc,
				nuevoNUmDoc);
	}

	@Override
	public void editarDatosAerolinea(String nickName, String nuevoNombre, String nuevaDesc, String nuevoLink) {
		// TODO Auto-generated method stub
		mcu.cambiarDatosAerolinea(nickName, nuevoNombre, nuevaDesc, nuevoLink);
	}

	@Override
	public List<String> listarAerolineas() throws UsuarioNoExisteException {
		// TODO Auto-generated method stub
		List<String> aerolineas = mcu.obtenerNickNamesAerolineas();
		if (aerolineas == null) {
			throw new UsuarioNoExisteException("no hay aerolineas");
		}

		return aerolineas;
	}

	public List<String> listarClientes() throws UsuarioNoExisteException {
		// TODO Auto-generated method stub
		List<String> clients = mcu.obtenerClientes();
		if (clients == null) {
			throw new UsuarioNoExisteException("no hay clientes");
		}
		return clients;
	}

	public List<String> cambiarAerolinea(String aerolinea) {
		return mcu.cambioAerolineaEnReserva(aerolinea);
	}

	public List<String> cambiarCliente(String cliente) {
		return mcu.cambioClienteEnReserva(cliente);
	}

	public List<String> cambiarRuta(String ruta, String aerolinea) {
		return mcu.cambioRutaEnReserva(ruta, aerolinea);
	}

}
