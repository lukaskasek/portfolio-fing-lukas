package logica;

import java.util.ArrayList;
import java.util.List;

public class DTAerolinea extends DTUser {
	private String descr,link;
	//private List<RutaDeVuelo> rutas;
	public DTAerolinea(String nombre,String nick,String correo,String descr,String link, String con,String foto) {
		super(nick,nombre,correo, con,foto);
		this.descr = descr;
		this.link = link;
		//rutas = new ArrayList<RutaDeVuelo>();
		
	}
	public String getDescr() {
		return this.descr;
	}
	public String getLink() {
		return this.link;
	}
}
