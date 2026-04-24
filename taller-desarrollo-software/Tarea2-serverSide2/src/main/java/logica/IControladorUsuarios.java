package logica;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import excepciones.DatosInvalidosException;
import excepciones.PaqueteNoExisteException;
import excepciones.RutaNoExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VueloNoExisteException;

public interface IControladorUsuarios {
	public abstract void NuevoCliente(String nickName,String nombre,String apellido,String Email,LocalDate fechaNac,String nacionalidad,String tipoDoc,String nroDoc,String contraseña, String foto) throws UsuarioYaExisteException, DatosInvalidosException;
	public abstract void NuevaAerolinea(String nickName,String nombre,String email,String descripcion,String link,String contraseña,String foto) throws UsuarioYaExisteException, DatosInvalidosException;
	public abstract List<String> listarUsuarios() throws UsuarioNoExisteException;
	public abstract DTUser mostrarDatosUsuario(String nickName);
	public abstract List<String> mostrarNombreRutasDeVuelo(String nickName)throws RutaNoExisteException ;
	public abstract List<String> mostrarReservasVuelos(String nickName) throws VueloNoExisteException;
	public abstract List<String> mostrarPaquetes(String nickName)throws PaqueteNoExisteException;
	public abstract void editarDatosCliente(String nickName,String nuevoNombre ,String nuevoApellido,LocalDate nuevaFechaNac,String nuevaNacionalidad,String nuevoTipoDoc,String nuevoNUmDoc);
	public abstract void editarDatosAerolinea(String nickName,String nuevoNombre, String nuevaDesc,String nuevoLink);
	public abstract List<String> listarAerolineas() throws UsuarioNoExisteException;
	public abstract List<String> listarClientes()throws UsuarioNoExisteException;
	public abstract List<String> cambiarRuta(String ruta, String aerolinea );
	public abstract List<String> cambiarCliente(String cliente );
	public abstract List<String> cambiarAerolinea(String aerolinea);
	public abstract int cuantasRutasHay(String hhh);//para probar cosas
	
}
