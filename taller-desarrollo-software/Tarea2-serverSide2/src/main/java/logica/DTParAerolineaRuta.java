package logica;

public class DTParAerolineaRuta {
	private String nickAero;
	private String nombreRuta;
	public DTParAerolineaRuta(String aerolinea,String ruta) {
		this.nickAero = aerolinea;
		this.nombreRuta = ruta;
	}
	public String getAero() {
		return nickAero;
		
	}
	public String getRuta() {
		return nombreRuta;
	}
}
