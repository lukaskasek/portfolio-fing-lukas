package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Vuelo {
	private String nombre;
	private RutaDeVuelo ruta;
	private LocalDate fecha;
	private Integer duracion;
	private Integer asientosTurista;
	private Integer asientosEjecutivo;
	private LocalDate fechaAlta;
	private List<Reserva> reservas;
	private Integer asientosTuristaDisponibles;
	private Integer asientosEjecutivoDisponibles;
	private String imagen;
	public Vuelo(String nom, RutaDeVuelo ruta, LocalDate fecha, Integer duracion, Integer asientosTurista, Integer asientosEjecutivo, LocalDate fechaAlta, String foto) {
		this.nombre = nom;
		this.asientosEjecutivo = asientosEjecutivo;
		this.asientosTurista = asientosTurista;
		this.asientosEjecutivoDisponibles = asientosEjecutivo;
		this.asientosTuristaDisponibles = asientosTurista;
		this.ruta = ruta;
		this.fechaAlta = fechaAlta;
		this.fecha = fecha;
		this.duracion = duracion;
		this.reservas = new ArrayList<Reserva>();
		this.imagen = foto;
	}
	public String getNombre() {
		return this.nombre;
	}
	public RutaDeVuelo getRuta() {
		return this.ruta;
	}
	public LocalDate getFecha() {
		return this.fecha;
	}
	public int getAsientosTurista() {
		return this.asientosTurista;
	}
	public int getAsientosEjecutivo() {
		return this.asientosEjecutivo;
	}
	public LocalDate getFechaAlta() {
		return this.fechaAlta;
	}
	public int getDuracion() {
		return this.duracion;
	}
	public int getAsientosTuristaDisponibles() {
		return this.asientosTuristaDisponibles;
	}
	public int getAsientosEjecutivoDisponibles() {
		return this.asientosEjecutivoDisponibles;
	}
	public List<DTReserva> getReservas(){
		List<DTReserva> res = new ArrayList<DTReserva>();
		for(Reserva rrr : reservas ) {
			LocalDate fec = rrr.getFecha();
			int eee = rrr.getEquipajeExtra();
			int cos = rrr.getCosto();
			String tipo = rrr.getTipoAsiento();
			List<DTPasaje> pasajes = rrr.getPasajes();
			DTReserva aaa = new DTReserva(eee,cos,tipo,fec,pasajes);
			res.add(aaa);
		}
		return res;
	}
	
	public List<Reserva> getReservasList(){
		return reservas;}

	
    public void addReserva(Reserva nuevaReserva, Integer cantPasajeros) {
        reservas.add(nuevaReserva);
		if (nuevaReserva.getTipoAsiento().equalsIgnoreCase("Turista")) {
			asientosTuristaDisponibles -= cantPasajeros;
		} else {
			asientosEjecutivoDisponibles -=  cantPasajeros;
		}
    }
    public void setImagen(String foto) {
    	this.imagen = foto;
    }
    public String getImagen() {
    	return this.imagen;
    }

}
