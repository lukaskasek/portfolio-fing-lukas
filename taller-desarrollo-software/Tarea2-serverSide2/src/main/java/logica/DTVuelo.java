package logica;

import java.time.LocalDate;
import java.util.List;

public class DTVuelo {
	private String nombre, imagen;
	private LocalDate fecha, fechaAlta;
	private int duracion, asientosTurista, asientosEjecutivo;
	private List<DTReserva> reservas;
	private Aerolinea aerolinea;
	public DTVuelo(String nom, LocalDate fecha, LocalDate fal, int dur, int ate, int aej, List<DTReserva> res,String imagen, Aerolinea aerolinea) {
		this.nombre = nom;
		this.fecha = fecha;
		this.fechaAlta = fal;
		this.duracion = dur;
		this.asientosTurista = ate;
		this.asientosEjecutivo = aej;
		this.reservas = res;
		this.imagen = imagen;
		this.aerolinea = aerolinea;
	}
	public String getNombre() {
		return this.nombre;
	}
	public Aerolinea getAerolinea() {
		return this.aerolinea;
	}
	void setAerolinea(Aerolinea aerolinea) {
		this.aerolinea = aerolinea;
	}
	public LocalDate getFecha() {
		return this.fecha;
	}
	public LocalDate getFechaAlta() {
		return this.fechaAlta;
	}
	public int getDuracion() {
		return this.duracion;
	}
	public int getAsientosTurista() {
		return this.asientosTurista;	
	}
	public int getAsientosEjecutivo() {
		return this.asientosEjecutivo;
	}
	public List<DTReserva> getReservas(){
		return this.reservas;
	}
	public String getImagen() {
		return this.imagen;
	}
}
