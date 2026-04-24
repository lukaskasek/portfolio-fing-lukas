package logica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import excepciones.RutaNoExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.DatosInvalidosException;
import excepciones.VueloYaExisteException;
import excepciones.VueloNoExisteException;
import excepciones.AsientosInsuficientesException;
import excepciones.CategoriaExistenteException;
import excepciones.CiudadExistenteException;
public interface IControladorRutasDeVuelo {
	public abstract void NuevoRutaDeVuelo(String aerolinea,String nombre,String desCorta, String descripcion,LocalTime hora,int costoTurista, int costoEjecutivo ,int costoEExtra,String ciudadOrigen,String ciudadDestino, LocalDate fechaAlta,String[] categorias) throws DatosInvalidosException, VueloYaExisteException;
	public abstract void setImagen(String ruta, String imagen);

	public abstract List<String> listarRutasDeVuelo(String aerolinea) throws RutaNoExisteException;
	public abstract List<String> listarRutasDeVueloConfirmadas(String aerolinea) throws RutaNoExisteException;
	public abstract List<List<String>> listarDatosRutasConfirmadasCategoria(String categoria);
	public abstract List<List<String>> listarDatosRutasConfirmadas();
	public abstract List<List<String>> listarDatosRutasConfirmadas(String aerolinea);
	public abstract List<List<String>> listarDatosRutasIngresadas(String aerolinea);
	public abstract List<List<String>> listarDatosRutasRechazadas(String aerolinea);
	public abstract List<String> listarVuelos(String ruta) throws VueloNoExisteException;
	public abstract List<String> imagenesVuelos(String ruta) throws VueloNoExisteException;
	public abstract DTVuelo mostrarDatosDeVuelo(String vuelo) throws VueloNoExisteException;
	public abstract void altaCiudad(String nombre, String pais, String aeropuerto, String descripcion, String website, LocalDate fechaAlta)throws CiudadExistenteException, DatosInvalidosException;
	public abstract void altaCategoria(String nombre)   throws CategoriaExistenteException, DatosInvalidosException;
	public abstract void ingresarDatos(String Aerolinea, String RutaVuelo, LocalDate fecha, LocalDate fechaAlta, String nombre, int asientosTurista, int asientosEjecutivos,int duracion) throws VueloYaExisteException, DatosInvalidosException;
    public abstract String[] listarCiudades() throws UsuarioNoExisteException;
	public List<String> obtenerDatosRuta(String nombre);
	public abstract String[] listarCategorias() throws UsuarioNoExisteException;
	public abstract int cuantasRutasHay();
    public void ingresarDatosVuelo(String cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, String vuelo) throws VueloNoExisteException, UsuarioNoExisteException, DatosInvalidosException, AsientosInsuficientesException;
	public abstract List<String> listarReservas(String vuelo);
	public abstract List<String> cambiarVuelo(String vuelo, String ruta);
	public abstract void ingresarNombres(String cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, String vuelo, int cantPasajeros, Set<String[]> nombresApellidosPasajeros) throws VueloNoExisteException, UsuarioNoExisteException, DatosInvalidosException, AsientosInsuficientesException;
	public abstract DTParAerolineaRuta getPar(String vueloSeleccionado);
	public abstract DTRutaDeVuelo obtenerInfoRuta(String ruta) throws RutaNoExisteException;
	public abstract String getAerolinea(String nombreRuta);
	public abstract boolean chequearReserva(String cliente, String vuelo);
	public void cambiarEstadoRuta(String ruta, int estado);

	public abstract DTCiudad obtenerDatosCiudad(String nombre, String pais) ;
	public abstract boolean categoriaYaExiste(String nombre);
	public abstract List<String> listarRutasDeVueloIngresadas(String aerolinea) throws RutaNoExisteException;
	public abstract RutaDeVuelo findRutaDeVuelo(String nomRuta) throws RutaNoExisteException;
	public abstract List<DTRutaDeVuelo> obtenerRutasPorCategoria(String categoria);
	public abstract List<Vuelo> listarVueloss() throws VueloNoExisteException;
	public abstract List<RutaDeVuelo> obtenerRutasConfirmadas() throws RutaNoExisteException;
	public abstract RutaDeVuelo obtenerRuta2(String nombre);
	public abstract Vuelo obtenerVuelo(String nombre);
	//la comento de mientras que no este implementa
	
	//RutaDeVuelo findRutaDeVuelo(String nomRuta);
}
