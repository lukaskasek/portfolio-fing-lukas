package logica;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RutaDeVuelo {
	private String nombre,descripcion,descripcionCorta;
	private LocalDate fechaAlta;
	private LocalTime hora;
	private float costoBase,costoBaseTurista,costoBaseEjecutivo,costoEquipajeExtra;
	private Ciudad cOrigen,cDestino;
	private List<Vuelo> vuelos;
	private List<Categoria> categorias;
	private Aerolinea aero;
	public enum Estado {
		ingresada, confirmada, rechazada
    }

    // Variable for estado
    private Estado estado;
    private String imagen;

    public RutaDeVuelo(String nombre2,String descCorta2, String descripcion, LocalTime hora2, int costoTurista, int costoEjecutivo,			int costoEExtra, Ciudad ciudadOrigen, Ciudad ciudadDestino, LocalDate fechaAlta2, List<Categoria> cats) {
		// TODO Auto-generated constructor stub
		this.categorias = new ArrayList<Categoria>();
		this.vuelos = new ArrayList<Vuelo>();
		this.nombre = nombre2;
		this.descripcion = descripcion;
		this.hora =hora2;
		this.costoBaseTurista = costoTurista;
		this.costoBaseEjecutivo = costoEjecutivo;
		this.costoEquipajeExtra = costoEExtra;
		this.fechaAlta = fechaAlta2;
		this.cOrigen = ciudadOrigen;
		this.descripcionCorta = descCorta2;
		this.estado = Estado.ingresada;

		this.cDestino = ciudadDestino;
		for(Categoria c : cats) {
			categorias.add(c);
		}
		this.imagen = "sinImagen";

	}
    public void setImagen(String sss) {
		this.imagen = sss;
	}
    public String getImagen() {
    	return this.imagen;
    }
    public boolean estaIngresada() {
		return estado == Estado.ingresada;
	}
    public boolean estaRechazada() {
		return estado == Estado.rechazada;
	}
	public void setEstado(int est) {
		if (est == 2) {
			estado = Estado.rechazada; }
		else {
			estado = Estado.confirmada; }
	}
	public String getDescripcionCorta() {
		return descripcionCorta;
	}

	public String getName() {
		return nombre;
	}
	public void linkearVuelo(Vuelo vue) {
		vuelos.add(vue);
	}
	public List<String> getVuelos(){
		List<String> vss = new ArrayList<String>();
		for(Vuelo v : vuelos) {
			vss.add(v.getNombre());
		}
		return vss;
	}
	
	public List<String> getImagenesVuelos(){
		List<String> vss = new ArrayList<String>();
		for(Vuelo v : vuelos) {
			vss.add(v.getImagen());
		}
		return vss;
	}

	public int getCostoBaseTurista() {
		return (int) costoBaseTurista;
	}
	public int getCostoBaseEjecutivo() {
		return (int) costoBaseEjecutivo;
	}
	public int getCostoEquipajeExtra() {
		return (int) costoEquipajeExtra;
	}
	public void setAerolinea(Aerolinea aer) {
		this.aero = aer;
	}
	public Aerolinea getAerolinea() {
		return this.aero;
	}
	public String getDescripcion() {
		return this.descripcion;
	}
	public LocalTime getHora() {
		return this.hora;
	}
	public LocalDate getFecha() {
		return this.fechaAlta;
	}
	public List<Categoria> getCategorias(){
		return this.categorias;
	}
	public List<String> obtenerDatos() {
	
        List<String> datos = new ArrayList<>();

        // Añadir nombre y descripción
        datos.add(nombre);
        datos.add(descripcionCorta);

        datos.add(descripcion);
        String strBase = String.valueOf(costoBase);
        String strBaseTurista = String.valueOf(costoBaseTurista);
        String strBaseEjecutivo = String.valueOf(costoBaseEjecutivo);
        String strEquipajeExtra = String.valueOf(costoEquipajeExtra);

        datos.add(strBase);
        datos.add(strBaseTurista);
        datos.add(strBaseEjecutivo);
        datos.add(strEquipajeExtra);
        
        if (cOrigen != null) {
            datos.add(cOrigen.getNombre());
        }
        if (cDestino != null) {
            datos.add(cDestino.getNombre());
        }

        // Añadir fecha (día, mes y año por separado)
        datos.add(String.valueOf(fechaAlta.getDayOfMonth()));
        datos.add(String.valueOf(fechaAlta.getMonthValue()));
        datos.add(String.valueOf(fechaAlta.getYear()));

        // Añadir hora y minutos por separado
        datos.add(String.valueOf(hora.getHour()));
        datos.add(String.valueOf(hora.getMinute()));

        // Añadir nombres de las ciudades de origen y destino
        datos.add(String.valueOf(estado));
        datos.add(imagen);


        // Añadir nombres de las categorías
        for (Categoria categoria : categorias) {
            datos.add(categoria.getNombre());
        }

        return datos;
	}
	
	public List<String> obtenerPocosDatos() {
		
        List<String> datos = new ArrayList<>();
        datos.add(nombre);
        String ima = imagen;
        if (ima.equalsIgnoreCase("sinImagen")) {
        	ima = null;
        }
        datos.add(ima);
        return datos;
	}
	
	public boolean tieneCategoria(String cat) {
		for (Categoria categoria : categorias) {
			if (categoria.getNombre().equalsIgnoreCase(cat)) {
				return true;
			}
		}
		return false;		
	}
	
	public boolean estaConfirmada(){
		if (estado == Estado.confirmada) {
			return true;
		}
		else {
			return false;
		}
	}
	public Estado getEstado() {
	    return this.estado;
	}
	public Ciudad getCorigen() {
		return this.cOrigen;
	}
	public Ciudad getCdestino() {
		return this.cDestino;
	}

}
