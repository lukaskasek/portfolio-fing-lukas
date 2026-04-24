package logica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import excepciones.VueloYaExisteException;
import excepciones.DatosInvalidosException;


import javax.swing.JOptionPane;

import excepciones.RutaNoExisteException;
import excepciones.VueloNoExisteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ManejadorCRutasDeVuelo {
	private static ManejadorCRutasDeVuelo instancia = null;
	private List<RutaDeVuelo> rutas;
	private List<Ciudad> ciudades;
	private List<Categoria> cts;
	private List<Vuelo> vuelos;
	private ManejadorCRutasDeVuelo() {
		rutas = new ArrayList<RutaDeVuelo>();
		cts = new ArrayList<Categoria>();
		ciudades = new ArrayList<Ciudad>();
		vuelos = new ArrayList<Vuelo>();
	}
	public static ManejadorCRutasDeVuelo getInstancia() {
		if (instancia == null) {
			instancia = new ManejadorCRutasDeVuelo();
		}
		return instancia;
	}
	
	public void agregarCiudad(String nombre, String pais, String aeropuerto, String descripcion, String website, LocalDate fechaAlta) {
		Ciudad nuevaCiudad = new Ciudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
        ciudades.add(nuevaCiudad);
        //JOptionPane.showMessageDialog(null,"SE AÑADE"+ nuevaCiudad.getPais()+ nuevaCiudad.getPais());
    }
	
	public boolean ciudadYaExiste(String nombre, String pais) {
		for (Ciudad c : ciudades) {
			//JOptionPane.showMessageDialog(null,"ENTRO AL FOR"+c.getPais()+ c.getPais());
			if (nombre.equalsIgnoreCase(c.getNombre()) && pais.equalsIgnoreCase(c.getPais())) {
				//JOptionPane.showMessageDialog(null,"RETORNO TRUE");
				return true;
			}
		}
		//JOptionPane.showMessageDialog(null,"RETORNO FALSE");
		return false;
	}
	
	public List<List<String>> listarDatosRutasConfirmadasCategoria(String categoria){
		List<List<String>> datosFiltrados = new ArrayList<>();
		for(RutaDeVuelo ruta: rutas) {
			if (ruta.tieneCategoria(categoria) && ruta.estaConfirmada()) {
				datosFiltrados.add(ruta.obtenerDatos());
			}
		}
		return datosFiltrados;
	}
	
	public List<List<String>> listarDatosRutasConfirmadas(){
		List<List<String>> datosFiltrados = new ArrayList<>();
		for(RutaDeVuelo ruta: rutas) {
			if (ruta.estaConfirmada()) {
				datosFiltrados.add(ruta.obtenerDatos());
			}
		}
		return datosFiltrados;
	}
	
	public List<String> listarRutasConfirmadas(String aerolinea){
		List<String> Rutas= new ArrayList<>();
		for(RutaDeVuelo ruta: rutas) {
			if (ruta.estaConfirmada() && ruta.getAerolinea().getNickName().equalsIgnoreCase(aerolinea)) {
				Rutas.add(ruta.getName());
			}
		}
		return Rutas;
	}
	
	
	public List<List<String>> listarDatosRutasConfirmadas(String aerolinea){
		List<List<String>> datosFiltrados = new ArrayList<>();
		for(RutaDeVuelo ruta: rutas) {
			if (ruta.estaConfirmada() && ruta.getAerolinea().getNickName().equalsIgnoreCase(aerolinea)) {
				datosFiltrados.add(ruta.obtenerPocosDatos());
			}
		}
		return datosFiltrados;
	}
	
	public List<List<String>> listarDatosRutasIngresadas(String aerolinea){
		List<List<String>> datosFiltrados = new ArrayList<>();
		for(RutaDeVuelo ruta: rutas) {
			if (ruta.estaIngresada() && ruta.getAerolinea().getNickName().equalsIgnoreCase(aerolinea)) {
				datosFiltrados.add(ruta.obtenerPocosDatos());
			}
		}
		return datosFiltrados;
	}
	
	public List<List<String>> listarDatosRutasRechazadas(String aerolinea){
		List<List<String>> datosFiltrados = new ArrayList<>();
		for(RutaDeVuelo ruta: rutas) {
			if (ruta.estaRechazada() && ruta.getAerolinea().getNickName().equalsIgnoreCase(aerolinea)) {
				datosFiltrados.add(ruta.obtenerPocosDatos());
			}
		}
		return datosFiltrados;
	}
	
	public void agregarCategoria(String nombre) {
		Categoria nuevaCategoria = new Categoria(nombre);
		cts.add(nuevaCategoria);
    }
	
	public boolean categoriaYaExiste(String nombre) {
		for (Categoria c: cts) {
			if (c.getNombre().equalsIgnoreCase(nombre)) {
				return true;
			}
		}
		return false;
	}
	
	public void NuevaRuta(Aerolinea aerolinea,String nombre, String desCorta, String descripcion, LocalTime hora, int costoTurista, int costoEjecutivo,int costoEExtra, String ciudadOrigen, String ciudadDestino, LocalDate fechaAlta, String[] categorias)throws VueloYaExisteException {

		// TODO Auto-generated method stub
		//tengo que buscar las ciudades que se suponen ya creadas, habria que hacer una excepcion en caso de que no existan, supongo que igual se puede verificar en la entrada
		for (RutaDeVuelo r : rutas) {
			if (r.getName().equalsIgnoreCase(nombre)) {
				throw new VueloYaExisteException("ya existe una ruta con ese nombre");
			}
		}
		

		Ciudad cOrigen = null;
		Ciudad cDestino = null;
		int ite = 0;
		while(ite < ciudades.size() && !ciudades.get(ite).getNombre().equalsIgnoreCase(ciudadOrigen)){
			ite++;
		}
		if(ciudades.get(ite).getNombre().equalsIgnoreCase(ciudadOrigen)) {
			 cOrigen = ciudades.get(ite);
		}
		else {System.out.println("no hay ciudda con ese nombre");}
		int jte = 0;
		 while(jte < ciudades.size() && !ciudades.get(jte).getNombre().equalsIgnoreCase(ciudadDestino)) {
			 jte++;//
		}
		if(ciudades.get(jte).getNombre().equalsIgnoreCase(ciudadDestino) ) {
			 cDestino = ciudades.get(jte);
		}
		List<Categoria> cats = new ArrayList<Categoria>();
		for(String ca : categorias) {
			int kkk = 0;
			while(kkk<cts.size() && !cts.get(kkk).getNombre().equalsIgnoreCase(ca)) {
				kkk++;
			}
			if(cts.get(kkk).getNombre().equalsIgnoreCase(ca)) {
				cats.add(cts.get(kkk));
			}
		}
		RutaDeVuelo rut = new RutaDeVuelo(nombre, desCorta, descripcion,hora,costoTurista,costoEjecutivo,costoEExtra,cOrigen,cDestino,fechaAlta,cats);
		rutas.add(rut);
		rut.setAerolinea(aerolinea);
		aerolinea.addRuta(rut);
		
	}
	public void setImagen(String ruta, String imag) {
		for (RutaDeVuelo rutass : rutas) {
	        if (rutass.getName().equalsIgnoreCase(ruta)) { 
	        	rutass.setImagen(imag);
	        }
	    }
	}

	
	

	public void crearVuelo(String ruta, String nombre, int cantmaxtur, int cantmaxejec, int dur, LocalDate fecha, LocalDate fechaAlta) {
		int ite = 0;
		RutaDeVuelo rut = null;
		while (ite < rutas.size() && !ruta.equalsIgnoreCase(rutas.get(ite).getName()) ) {
		ite++;
		}
		if(ite<rutas.size() &&  rutas.get(ite).getName().equalsIgnoreCase(ruta)) {
			rut = rutas.get(ite);
		}
		Vuelo vue =  new Vuelo(nombre, rut, fecha, dur, cantmaxtur, cantmaxejec,fechaAlta,null);
		vuelos.add(vue);
		rut.linkearVuelo(vue);
	}

	public List<String> obtenerVuelos(String ruta){
	int ite = 0;
	while(ite< rutas.size() && !ruta.equalsIgnoreCase(rutas.get(ite).getName())){
		ite++;
	}
	if(ite< rutas.size() && rutas.get(ite).getName().equalsIgnoreCase(ruta)) {
		return rutas.get(ite).getVuelos();
	}
	return null;
	}
	
	public List<String> obtenerImagenesVuelos(String ruta){
		int ite = 0;
		while(ite< rutas.size() && !ruta.equalsIgnoreCase(rutas.get(ite).getName())) {
			ite++;
		}
		if(ite< rutas.size() && rutas.get(ite).getName().equalsIgnoreCase(ruta)) {
			return rutas.get(ite).getImagenesVuelos();
		}
		return null;
		}
	
	public DTVuelo obtenerDatosDeVuelo(String vuelo) throws VueloNoExisteException{
	    int ite = 0;

	    // Buscar el vuelo en la lista
	    while (ite < vuelos.size() && !vuelos.get(ite).getNombre().equals(vuelo)) {
	        ite++;
	    }

	    // Verificar si se encontró el vuelo
	    if (ite == vuelos.size()) {
	        // Lanza una excepción o retorna null si no se encontró el vuelo
	        throw new VueloNoExisteException("No se encontró ningún vuelo con el nombre: " + vuelo);
	    }

	    // Obtener el vuelo encontrado
	    Vuelo vue = vuelos.get(ite);

	    // Extraer los datos del vuelo
	    LocalDate fecha = vue.getFecha();
	    LocalDate fal = vue.getFechaAlta();
	    int aej = vue.getAsientosEjecutivo();
	    int atu = vue.getAsientosTurista();
	    int dur = vue.getDuracion();
	    List<DTReserva> res = vue.getReservas();
	    RutaDeVuelo ruta = vue.getRuta();

	    // Crear y retornar el objeto DTVuelo
	    DTVuelo data = new DTVuelo(vuelo, fecha, fal, dur, atu, aej, res, vue.getImagen(),ruta.getAerolinea());
	    return data;
	}

	public DTRutaDeVuelo obtenerDatosDeRuta(String ruta) {
		int ite = 0;
		while(ite < rutas.size() && rutas.get(ite).getName() != ruta) {
			ite++;
		}
		RutaDeVuelo rut = rutas.get(ite);
		String desc = rut.getDescripcion();
		String desCorta = rut.getDescripcionCorta();

		int costoT = rut.getCostoBaseTurista();
		int costoE = rut.getCostoBaseEjecutivo();
		int costoEE = rut.getCostoEquipajeExtra();
		LocalTime hora = rut.getHora();
		LocalDate fecha = rut.getFecha();
		DTRutaDeVuelo res = new DTRutaDeVuelo(ruta,desCorta, desc,hora,costoT,costoE,costoEE,fecha);
		return res;
	}
	public boolean vueloYaExiste(String nombre) {
		for(Vuelo v : vuelos) {
			if(v.getNombre().equalsIgnoreCase(nombre)) {
				return true;
			}
		}
		return false;
	}
	public void cambiarEstadoRuta(String ruta, int estado) {
		int ite = 0;
		while(ite < rutas.size() && rutas.get(ite).getName() != ruta) {
			ite++;
		}
		RutaDeVuelo rut = rutas.get(ite);
		rut.setEstado(estado);
	}

    public String[] listarCiudades() {
        // TODO Auto-generated method stub
		List<String> nicks = new ArrayList<String>();
		if(ciudades.size() == 0 ) {
			return null;
		}
		for(Ciudad c : ciudades){
			String nick = c.getNombre();
			nicks.add(nick);
		}
		String[] nic = nicks.toArray(new String[0]);
		return nic;
    }
	public String[] listarCategorias() {
		// TODO Auto-generated method stub
		List<String> nuc = new ArrayList<String>();
		for(Categoria c: cts){
			String nca = c.getNombre();
			nuc.add(nca);
		}
		String[] nic = nuc.toArray(new String[0]);
		return nic;
	}
	

    public void crearReservaYPasaje(Cliente cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, Vuelo vuelo) {
    	 String tipoAsiento = tipo.equalsIgnoreCase("Ejecutivo") ? "Ejecutivo" : "Turista";
        int costo ;
		if(tipoAsiento.equalsIgnoreCase("Ejecutivo")) {
			costo = vuelo.getRuta().getCostoBaseEjecutivo() + vuelo.getRuta().getCostoEquipajeExtra() * cantEqExtra;
		}else {
			costo = vuelo.getRuta().getCostoBaseTurista() + vuelo.getRuta().getCostoEquipajeExtra() * cantEqExtra;
		}
        Reserva nuevaReserva = new Reserva(fechaDeReserva, tipoAsiento, cantEqExtra, costo, 1, vuelo);
        System.out.println("Se hizo la reserva del vuelo "+vuelo.getNombre());

        // Crear y asociar un pasaje a la reserva
        if(vuelo.getReservas() == null) {
            Pasaje nuevoPasaje = new Pasaje(1, cliente.getNombre(), cliente.getApellido());
            nuevaReserva.addPasaje(nuevoPasaje);
        }
        else {
        Pasaje nuevoPasaje = new Pasaje(nuevaReserva.getPasajes().size() + 1, cliente.getNombre(), cliente.getApellido());
        nuevaReserva.addPasaje(nuevoPasaje); }

        // Agregar la reserva al vuelo
        vuelo.addReserva(nuevaReserva, 1);

        // Agregar la reserva a la lista de reservas del cliente
        cliente.addReserva(nuevaReserva);
		
	}
    
    public void crearReservaConPasajeros(Cliente cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, Vuelo vuelo, Set<String[]> nombresApellidosPasajeros) {
    	String tipoAsiento = tipo.equalsIgnoreCase("ejecutivo") ? "ejecutivo" : "turista";
        int costo;
        int cantidadPasajes = nombresApellidosPasajeros.size() + 1;
        int numPasaje = 1;
        if (tipoAsiento.equalsIgnoreCase("ejecutivo")) {
            costo = vuelo.getRuta().getCostoBaseEjecutivo()  * cantidadPasajes  + vuelo.getRuta().getCostoEquipajeExtra() * cantEqExtra;
        } else {
            costo = vuelo.getRuta().getCostoBaseTurista() * cantidadPasajes  + vuelo.getRuta().getCostoEquipajeExtra() * cantEqExtra;
        }

        Reserva nuevaReserva = new Reserva(fechaDeReserva, tipoAsiento, cantEqExtra, costo, cantidadPasajes, vuelo);
        // Crear y asociar el pasaje del cliente inicial a la reserva
        Pasaje pasajeCliente = new Pasaje(numPasaje++, cliente.getNombre(), cliente.getApellido());
        nuevaReserva.addPasaje(pasajeCliente);

        // Crear y asociar pasajes para los demás pasajeros
        for (String[] nombreApellido : nombresApellidosPasajeros) {
            if (nombreApellido.length == 2) { 
                Pasaje nuevoPasaje = new Pasaje(numPasaje++, nombreApellido[0], nombreApellido[1]);
                nuevaReserva.addPasaje(nuevoPasaje);
            }
        }

        // Agregar la reserva al vuelo
        vuelo.addReserva(nuevaReserva, numPasaje);

        // Agregar la reserva a la lista de reservas del cliente
        cliente.addReserva(nuevaReserva);
    }    

	public Vuelo obtenerVuelo(String vuelo) {
		int ite = 0;
		
		while (ite < vuelos.size() && !vuelos.get(ite).getNombre().equals(vuelo)) {
			ite++;
		}
		if(ite < vuelos.size() && vuelos.get(ite).getNombre().equals(vuelo)) {
			 Vuelo vue = (Vuelo) vuelos.get(ite);
			 return vue;
		}else {
			return null; 
		}
	}

		
	public int cuantasRutasHay() {
		return rutas.size();
	}

 
    public List<String> listarReservas(Vuelo vueloObj) {
        // TODO Auto-generated method stub
// debe recorrer las reservas de getReservas, luego, recorrer los pasajes de cada una de las reservas y obtener los nombres de los pasajeros, luego, devolver una lista con los nombres de los pasajeros
		List<String> nombres = new ArrayList<String>();
		List<Reserva> res = vueloObj.getReservasList();
		for(Reserva r : res) {
			List<DTPasaje> pasajeros = r.getPasajes();
			for(DTPasaje p : pasajeros) {
				String nombre = p.getNombre();
				nombres.add(nombre);
			}
		}
		return nombres;
    }


	public List<String> obtenerDatosRuta(String nombre) {
		RutaDeVuelo rut = rutas.get(0);
		for (RutaDeVuelo rv: rutas) {
			if (rv.getName().equalsIgnoreCase(nombre)) {
				rut = rv;
			}
		}
		return rut.obtenerDatos() ;
	}

	
	public List<String> cambioVueloEnReserva(String vuelo, String ruta) {
		List<String> vuelos = this.obtenerVuelos(ruta);
		if(vuelos == null || vuelos.size() <= 1) {
			return null;
		}
		List<String> vuelosFiltrados = new ArrayList<String>();
		for(String v : vuelos) {
			if(!v.equals(vuelo)) {
				vuelosFiltrados.add(v);
			}
		}
		return vuelosFiltrados.isEmpty() ? null : vuelosFiltrados; // Si no quedan otros vuelos, retorna null


	}
	public RutaDeVuelo obtenerRuta(String ruta) {
	    int ite = 0;

	    // Usa .equals() en lugar de == para comparar Strings
	    while (ite < rutas.size() && !rutas.get(ite).getName().equals(ruta)) {
	        ite++;
	    }
	    
	    // También usa .equals() aquí para la verificación final
	    if (ite < rutas.size() && rutas.get(ite).getName().equals(ruta)) {
	        return rutas.get(ite);  // No necesitas hacer un cast aquí si ya es RutaDeVuelo
	    } else {
	        return null;
	    }
	}

	public Ciudad buscarCiudad(String nombreCiudad) {
        for (Ciudad ciudad : ciudades) {
            if (ciudad.getNombre().equalsIgnoreCase(nombreCiudad)) {
                return ciudad; // Devuelve la ciudad si coincide el nombre
            }
        }
        return null; // Devuelve null si no se encuentra la ciudad
    }
	public DTParAerolineaRuta getPar(String vueloSeleccionado) {
		// TODO Auto-generated method stub
		Vuelo vue= null;
		DTParAerolineaRuta dtpar = null;
		int ite=0;
		
		while(ite<vuelos.size() && !vuelos.get(ite).getNombre().equals(vueloSeleccionado)) {
			ite++;
		}
		if(vuelos.get(ite).getNombre().equalsIgnoreCase(vueloSeleccionado)) {
			vue = vuelos.get(ite);
			String ruta = vue.getRuta().getName();
			String aerolinea = vue.getRuta().getAerolinea().getNickName();
			dtpar = new DTParAerolineaRuta(aerolinea,ruta);
			
		}
		return dtpar;
	}
	public String obtenerAero(String nombreRuta) {
		// TODO Auto-generated method stub
		int ite = 0;
		while(ite<rutas.size() && !rutas.get(ite).getName().equalsIgnoreCase(nombreRuta)) {
			ite++;
		}
		if(ite< rutas.size() && rutas.get(ite).getName().equalsIgnoreCase(nombreRuta)) {
			return rutas.get(ite).getAerolinea().getNickName();
		}
		return null;
	}

	public DTCiudad obtenerDatosCiudad(String nombre, String pais) {
		DTCiudad DTC = null;

		for (Ciudad c: ciudades){
			if (c.getNombre().equalsIgnoreCase(nombre) && c.getPais().equalsIgnoreCase(pais)) {
				DTC = new DTCiudad(c.getNombre(),c.getPais(),c.getAeropuerto(),c.getDescripcion(),c.getWebSite(),c.getFechaAlta());
			}
		}
		return DTC;
	}
	public int getAsientosEjecutivoDisponibles(Vuelo vuelo) {
		return vuelo.getAsientosEjecutivoDisponibles();
	}
	public int getAsientosTuristaDisponibles(Vuelo vuelo) {
		return vuelo.getAsientosTuristaDisponibles();
	}
	public RutaDeVuelo findRutaDeVuelo(String nomRuta) throws RutaNoExisteException {
		// TODO Auto-generated method stub
		int ite = 0;
		while(ite<rutas.size() && !rutas.get(ite).getName().equalsIgnoreCase(nomRuta)) {
			ite++;
		}
		if(ite < rutas.size() && rutas.get(ite).getName().equalsIgnoreCase(nomRuta)) {
			return rutas.get(ite);
		}else {
			throw new RutaNoExisteException("no existe la ruta de nombre "+nomRuta);
		}
		
	}
	public List<RutaDeVuelo> getRutas() {
		return this.rutas;
	}
	public List<Vuelo> getVuelos(){
		return vuelos;
	}

	public List<RutaDeVuelo> listarRutasConfirmadas() {
	    List<RutaDeVuelo> rutasConfirmadas = new ArrayList<>();

	    // Recorremos todas las rutas de vuelo
	    for (RutaDeVuelo ruta : rutas) {
	        // Si la ruta está confirmada, la añadimos a la lista de confirmadas
	        if (ruta.getEstado() == RutaDeVuelo.Estado.confirmada) {
	            rutasConfirmadas.add(ruta);
	        }
	    }
	    return rutasConfirmadas;
	}
	public RutaDeVuelo obtenerRuta2(String nombre){
	    for (RutaDeVuelo ruta : rutas) {
	        if (ruta.getName().equalsIgnoreCase(nombre)) {  // Compara los nombres ignorando mayúsculas/minúsculas
	            return ruta;  // Si encontramos la ruta, la retornamos
	        }
	    }
	    return null;
	}
	public Vuelo obtenerVuelo2(String nombre){
	    for (Vuelo v : vuelos) {
	        if (v.getNombre().equalsIgnoreCase(nombre)) {  // Compara los nombres ignorando mayúsculas/minúsculas
	            return v;  // Si encontramos la ruta, la retornamos
	        }
	    }
	    return null;
	}
	

} 


