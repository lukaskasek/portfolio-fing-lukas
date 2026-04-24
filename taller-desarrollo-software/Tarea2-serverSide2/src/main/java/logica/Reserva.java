package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
	private LocalDate fecha;
	private String tipoAsiento;
	private int equipajeExtra,costo;//,cantidadPasajes;
	private Vuelo vuelo;
	private List<Pasaje> pasajes;
	public Reserva(LocalDate fec,String tAs,int eEx, int costoTotal, int cantidadPasajeros, Vuelo vue) {
		this.fecha = fec;
		this.tipoAsiento = tAs;
		this.equipajeExtra = eEx;
		this.costo = costoTotal;
		//this.cantidadPasajes = cantidadPasajeros;
		this.vuelo = vue;
		this.pasajes = new ArrayList<Pasaje>();}

	public String obtenerNombreVuelo() {
		// TODO Auto-generated method stub
		return vuelo.getNombre();
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
		List<DTPasaje> res = new ArrayList<DTPasaje>();

		if(this.pasajes.isEmpty()){
			return res;
			
		}else {
			for(Pasaje pasa : pasajes) {
				String nom = pasa.getNombre();
				String ape = pasa.getApellido();
				int num = pasa.getNum();
				DTPasaje pas = new DTPasaje(nom,ape, num);
				res.add(pas);
			}
	 		return res;
		}
		
	}
	public void addPasaje(Pasaje pas) {
		pasajes.add(pas);
	}

}
