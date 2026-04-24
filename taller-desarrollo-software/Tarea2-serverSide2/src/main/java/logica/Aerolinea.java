package logica;

import java.util.ArrayList;
import java.util.List;

public class Aerolinea extends Usuario {
	private String descripcion;
	private String link;
	private List<RutaDeVuelo> rutas;
	public Aerolinea(String nombre,String email,String nickName,String desc,String link,String contra,String foto) {
		super(nombre,nickName,email,contra,foto);
		this.descripcion=desc;
		this.link = link;
		rutas = new ArrayList<RutaDeVuelo>();

	}
	public String getDescripcion() {
		return descripcion;
	}
	public String getLink() {
		return link;
	}
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public DTUser obtenerDatos() {
		// TODO Auto-generated method stub
		return new DTAerolinea( this.getNombre() , this.getNickName(),this.getEmail() ,descripcion, link, this.getContra(),this.getFoto());
	}
	public List<String> obtenerNombresRutaDeVuelo(){
		String name;
		List<String> nombres = new ArrayList<String>();
		for(RutaDeVuelo r : rutas) {
			name =r.getName();
			nombres.add(name);
		}
		return nombres;
	}
	
	
	public List<String> getRutasIngresadas(){
		List<String> ruts = new ArrayList<String>();
		for(RutaDeVuelo ru: rutas){
			if (ru.estaIngresada()) {
				ruts.add(ru.getName());
			}	
		}
		for(String s : ruts) {
			System.out.println(ruts + " esta asociada a " + this.getNombre());
		}
		
		return ruts;
	}
	

	
	public int cuantasRutasTiene(){//prueba{
		return rutas.size();
	}
	public void cambiarDatosAerolinea(String nuevoNombre ,String nuevaDesc, String nuevoLink) {
		// TODO Auto-generated method stub
		this.setNombre(nuevoNombre);
		this.descripcion = nuevaDesc;
		this.link = nuevoLink;
	}
	public List<String> getRutas(){
		List<String> ruts = new ArrayList<String>();
		for(RutaDeVuelo ru: rutas){
			ruts.add(ru.getName());
		}
		for(String s : ruts) {
			System.out.println(ruts + " esta asociada a " + this.getNombre());
		}
		
		return ruts;
	}
    public void addRuta(RutaDeVuelo rut) {
        // TODO Auto-generated method stub
		rutas.add(rut);
		//for(RutaDeVuelo ru: rutas){
		//	System.out.println(ru.getName());
		//}
    }
}
