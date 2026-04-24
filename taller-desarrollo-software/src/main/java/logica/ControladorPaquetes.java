package logica;

import java.time.LocalDate;

import excepciones.DatosInvalidosException;
import excepciones.PaqueteYaExisteException;
import excepciones.RutaNoExisteException;

public class ControladorPaquetes implements IControladorPaquetes {
	private ManejadorCPaquetes mcp ;
	public ControladorPaquetes() {
		mcp = ManejadorCPaquetes.getInstancia();
	}
	@Override
	public void crearPaquete(String nombre, String descripcion, int periodoValidez, int descuento,
			LocalDate fechaAlta) throws PaqueteYaExisteException, DatosInvalidosException {
		// TODO Auto-generated method stub
		
			checkearDatosPaquete(nombre,descripcion,periodoValidez,descuento,fechaAlta);
		
		if(PaqueteYaExiste(nombre)) {
			throw new PaqueteYaExisteException("Ya existe un paquete con el nombre "+ nombre);
		}
		mcp.crearPaquete(nombre,descripcion,periodoValidez,descuento,fechaAlta);
	}
	private void checkearDatosPaquete(String nombre, String descripcion, int periodoValidez, int descuento,
			LocalDate fechaAlta) throws DatosInvalidosException {
		// TODO Auto-generated method stub
		if(!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$")) {
			throw new DatosInvalidosException("El nombre no es valido");
		}
		if(descripcion.isEmpty()) {
			throw new DatosInvalidosException("La descricpion no puede ser vacia");
		}
		if(periodoValidez < 1) {
			throw new DatosInvalidosException("El periodo de validez tiene que ser de al menos un dia");
		}
		if(descuento <= 0) {
			throw new DatosInvalidosException("El descuento tiene que ser mayor que 0");
		}
		if(descuento >= 100) {
			throw new DatosInvalidosException("El descuento tiene que ser menor que 100");
		}
		if(fechaAlta == null) {
			throw new DatosInvalidosException("La fecha no puede ser vacia");
		}
	}
	private boolean PaqueteYaExiste(String nombre) {
		// TODO Auto-generated method stub
		return mcp.PaqueteYaExiste(nombre);
	}
	@Override
	public String MostrarNombrePaquete(String string) {
		// TODO Auto-generated method stub
		return mcp.mostrarNombrePaquete(string);
		
	}
	@Override
	public String[] listarPaquetes() {
		// TODO Auto-generated method stub
		String[] paquetes = mcp.listarPaquetes();
		return paquetes;
	}
	@Override
	public void agregarRutaAPaquete(String nomPaquete, String nomRuta, String tipoAsiento, int cantidad) throws RutaNoExisteException {
		// TODO Auto-generated method stub
		Fabrica fabrica = Fabrica.getInstancia();
		IControladorRutasDeVuelo iCRV = fabrica.getIControladorRutasDeVuelo();
		RutaDeVuelo rut = iCRV.findRutaDeVuelo(nomRuta);//lo comento para que compile
		
		mcp.agregarRutaAPaquete(nomPaquete,rut,tipoAsiento,cantidad);//lo comento para que compile
		
	}
}
