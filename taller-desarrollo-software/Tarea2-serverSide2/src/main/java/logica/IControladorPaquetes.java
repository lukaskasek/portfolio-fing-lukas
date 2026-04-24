package logica;

import java.time.LocalDate;

import excepciones.DatosInvalidosException;
import excepciones.PaqueteYaExisteException;
import excepciones.RutaNoExisteException;

public interface IControladorPaquetes {
	public abstract void crearPaquete(String nombre,String descripcion,int periodoValidez,int descuento,LocalDate fechaAlta ) throws PaqueteYaExisteException, DatosInvalidosException;

	public abstract String MostrarNombrePaquete(String string);
	
	public abstract String[] listarPaquetes();
	
	public abstract void  agregarRutaAPaquete(String nomPaquete,String nomRuta,String tipoAsiento,int cantidad) throws RutaNoExisteException;

}
