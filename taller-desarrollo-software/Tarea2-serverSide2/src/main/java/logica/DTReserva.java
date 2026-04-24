package logica;

import java.time.LocalDate;
import java.util.List;

public class DTReserva {
	private String tipoAsiento;
	private int equipajeExtra, costo;
	private LocalDate fecha;
	private List<DTPasaje> pasajes;
	public DTReserva(int equip, int costo, String tipo, LocalDate fecha, List<DTPasaje> pasajes) {
		this.costo = costo;
		this.tipoAsiento = tipo;
		this.fecha = fecha;
		this.pasajes = pasajes;
		this.equipajeExtra = equip;
	}
	public LocalDate getFecha() {
		return this.fecha;
	}
	public String getTipoAsiento() {
		return this.tipoAsiento;
	}
	public int getEquipajeExtra() {
		return this.equipajeExtra;
	}
	public int getCosto() {
		return this.costo;
	}
	public List<DTPasaje> getPasajes(){
		return this.pasajes;
	}
}
