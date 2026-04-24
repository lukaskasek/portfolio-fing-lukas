package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paquete {
	private String nombre;
	private  String descripcion;
	private int pValidez,descount;
	private LocalDate fechaAlta;
	private List<RutaEnPaquete> rutas;
	public Paquete(String nombre2, String descripcion, int periodoValidez, int descuento, LocalDate fechaAlta) {
		// TODO Auto-generated constructor stub
		this.nombre = nombre2;
		this.descripcion = descripcion;
		this.pValidez = periodoValidez;
		this.descount = descuento;
		this.fechaAlta = fechaAlta;
		rutas = new ArrayList<RutaEnPaquete>();
	}
	public String getNombre() {
		return nombre;
	}
	public String getDescr() {
		return descripcion;
	}
	public int getpValidez() {
		return pValidez;
	}
	public int getDescuento() {
		return this.descount;
	}
	public LocalDate getfechaAlta() {
		return fechaAlta;
	}
	public void agregarNuevaRuta(RutaDeVuelo rut, String tipoAsiento, int cantidad) {
		// TODO Auto-generated method stub
		RutaEnPaquete rep = new RutaEnPaquete(rut,tipoAsiento,cantidad);
		rutas.add(rep);
	}
}
