package logica;

public class RutaEnPaquete {
	private RutaDeVuelo ruta;
	private String tipoAsiento;
	private int cantidad;
	public RutaEnPaquete(RutaDeVuelo rut,String tAa,int cant) {
		this.ruta =rut;
		this.tipoAsiento = tAa;
		this.cantidad = cant;
	}
	public RutaDeVuelo getRuta() {
		return ruta;
	}
	public String getTipoAsiento() {
		return tipoAsiento;
	}
	public int getCantidad() {
		return cantidad;
	}
}
