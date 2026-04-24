package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManejadorCPaquetes {
	private static ManejadorCPaquetes instancia = null;
	private List<Paquete> paquetes;
	private ManejadorCPaquetes() {
		paquetes = new ArrayList<Paquete>();
	}
	public static ManejadorCPaquetes getInstancia() {
		// TODO Auto-generated method stub
		if(instancia == null) {
			instancia = new ManejadorCPaquetes();
		}
		return instancia;
	}
	public void crearPaquete(String nombre, String descripcion, int periodoValidez, int descuento,
			LocalDate fechaAlta) {
		// TODO Auto-generated method stub
		Paquete paq = new Paquete(nombre,descripcion,periodoValidez,descuento,fechaAlta);
		paquetes.add(paq);
	}
	public boolean PaqueteYaExiste(String nombre) {
		// TODO Auto-generated method stub
		for(Paquete p:paquetes) {
			if(p.getNombre().equals(nombre)) {
				return true;
			}
		}
		return false;
	}
	public String mostrarNombrePaquete(String string) {
		// TODO Auto-generated method stub
		int ite=0;
		while( ite < paquetes.size() && !paquetes.get(ite).getNombre().equals(string)) {
			ite++;
		}
		if(ite< paquetes.size()) {
			return paquetes.get(ite).getNombre();
		}
		return null;
	}
	public String[] listarPaquetes() {
		// TODO Auto-generated method stub
		List<String> packs = new ArrayList<String>();
		for(Paquete p : paquetes) {
			packs.add(p.getNombre());
		}
		return packs.toArray(new String[0]);
	}
	public void agregarRutaAPaquete(String nomPaquete, RutaDeVuelo rut, String tipoAsiento, int cantidad) {
		// TODO Auto-generated method stub
		int ite = 0;
		while(ite<paquetes.size() && !paquetes.get(ite).getNombre().equalsIgnoreCase(nomPaquete)) {
			ite++;
		}
		if(ite<paquetes.size() && paquetes.get(ite).getNombre().equalsIgnoreCase(nomPaquete)) {//lo encontro
			Paquete paq  = paquetes.get(ite);
			paq.agregarNuevaRuta(rut,tipoAsiento,cantidad);
		}
		
	}

}
