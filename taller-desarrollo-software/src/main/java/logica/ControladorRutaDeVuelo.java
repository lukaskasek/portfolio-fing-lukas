package logica;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import excepciones.RutaNoExisteException;
import excepciones.AsientosInsuficientesException;
import excepciones.CategoriaExistenteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VueloYaExisteException;
import excepciones.VueloNoExisteException;
import excepciones.CiudadExistenteException;
import excepciones.DatosInvalidosException;
public class ControladorRutaDeVuelo implements IControladorRutasDeVuelo {
    private ManejadorCUsuarios mcu;
    private ManejadorCRutasDeVuelo mcr;

    public ControladorRutaDeVuelo() {
		mcr = ManejadorCRutasDeVuelo.getInstancia();
		mcu = ManejadorCUsuarios.getInstancia();
		}
	
	public void altaCategoria(String nombre)  throws CategoriaExistenteException, DatosInvalidosException {
		if(nombre.trim().isEmpty() || !nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ1234567890\\s]+$" )) {
			throw new DatosInvalidosException("El nombre es invalido, no puede ser vacio.");
		}
		if (mcr.categoriaYaExiste(nombre)) {
			throw new CategoriaExistenteException("ya existe una categoria con ese nombre");
		}
		else {
			mcr.agregarCategoria(nombre);
		}
	}
	
	public boolean categoriaYaExiste(String nombre){
		return mcr.categoriaYaExiste(nombre);
	}
	public  List<String> listarRutasDeVueloConfirmadas(String aerolinea){
		return mcr.listarRutasConfirmadas(aerolinea);
	}

	public List<List<String>> listarDatosRutasConfirmadasCategoria(String categoria){
		return mcr.listarDatosRutasConfirmadasCategoria( categoria);
	}
	
	public List<List<String>> listarDatosRutasConfirmadas(){
		return mcr.listarDatosRutasConfirmadas();
	}
	public List<List<String>> listarDatosRutasConfirmadas(String aerolinea){
		return mcr.listarDatosRutasConfirmadas( aerolinea);
	}
	public List<List<String>> listarDatosRutasIngresadas(String aerolinea){
		return mcr.listarDatosRutasIngresadas( aerolinea);
		}
	public List<List<String>> listarDatosRutasRechazadas(String aerolinea){
		return mcr.listarDatosRutasRechazadas( aerolinea);
		}
	

	@Override
	public void NuevoRutaDeVuelo(String aerolinea, String nombre, String desCorta,String descripcion, LocalTime hora, int costoTurista,	int costoEjecutivo, int costoEExtra, String ciudadOrigen, String ciudadDestino, LocalDate fechaAlta,String[] categorias)throws DatosInvalidosException,VueloYaExisteException {
		// TODO Auto-generated method stub
		checkearDatosRuta(aerolinea, nombre,  desCorta,descripcion,  hora,costoTurista,costoEjecutivo, costoEExtra,ciudadOrigen,ciudadDestino,fechaAlta, categorias);
		Aerolinea aer = mcu.obtenerAerolinea(aerolinea);
		if(aer == null) {
			throw new DatosInvalidosException("No existe la aerolinea ingresada");
		}
		
		mcr.NuevaRuta(aer,nombre,descripcion,desCorta,hora,costoTurista,costoEjecutivo,costoEExtra,ciudadOrigen,ciudadDestino,fechaAlta,categorias);		
		
	}
	public void cambiarEstadoRuta(String ruta, int estado) {
		mcr.cambiarEstadoRuta(ruta, estado) ;
		
	}
	@Override
	public List<String> listarRutasDeVueloIngresadas(String aerolinea) throws RutaNoExisteException {
		// TODO Auto-generated method stub
		List<String> rutas = mcu.obtenerRutasDeVueloIngresadas(aerolinea.trim());
	
		if(rutas == null) {
			throw new RutaNoExisteException("No hay rutas asociadas a esta aerolinea");
		}
		Collections.sort(rutas);
		return rutas;
	}
	public void setImagen(String ruta, String aero) {
		mcr.setImagen(ruta, aero);
	}


	
	private void checkearDatosRuta(String aerolinea, String nombre, String desCorta,String descripcion, LocalTime hora, int costoTurista,
			int costoEjecutivo, int costoEExtra, String ciudadOrigen, String ciudadDestino, LocalDate fechaAlta,
			String[] categorias) throws DatosInvalidosException {
		
		if (nombre.trim().isEmpty()) {
	        throw new DatosInvalidosException("El nombre no puede estar vacío.");
	    }
		if (desCorta.trim().isEmpty()) {
	        throw new DatosInvalidosException("La descripcion corta no puede estar vacía.");
	    }

		if(costoEjecutivo < 1) {
			throw new DatosInvalidosException("dato invalido para costo de los asientos de tipo ejecutivo");
		}
		if(costoEExtra < 1 ) {
			throw new DatosInvalidosException("dato invalido para costo de equipaje extra");
		}
		if(costoTurista < 1) {
			throw new DatosInvalidosException("dato invalido para costo de los asientos de tipo TURISTA");
		}
		if (descripcion.trim().isEmpty()) {
	        throw new DatosInvalidosException("La descripcion no puede estar vacía.");
	    }
		if (ciudadOrigen.trim().isEmpty()) {
	        throw new DatosInvalidosException("ciudad origen no puede estar vacío.");
	    }
		if (ciudadDestino.trim().isEmpty()) {
	        throw new DatosInvalidosException("ciudad destino no puede estar vacío.");
	    }
		if (fechaAlta == null) {
	        throw new DatosInvalidosException("La fecha de alta no puede ser vacía.");
	    }
		if (hora == null) {
	        throw new DatosInvalidosException("La hora no puede ser vacía.");
	    }
		 if (categorias == null ) {
		        throw new DatosInvalidosException("Debe haber al menos una categoría.");
		}
		if(mcr.buscarCiudad(ciudadOrigen.trim()) == null) {
			throw new DatosInvalidosException("La ciudad de origen ingresada no existe.");
			}
	
		if(mcr.buscarCiudad(ciudadDestino.trim()) == null) {
			throw new DatosInvalidosException("La ciudad de destino ingresada no existe.");
		}
		
		for (String categoria : categorias) {
		        if (categoria == null || categoria.trim().isEmpty()) {
		            throw new DatosInvalidosException("Cada categoría debe ser válida y no estar vacía.");
		        }
		        if (!mcr.categoriaYaExiste(categoria)) {
		            throw new DatosInvalidosException("La categoría '" + categoria + "' no existe en el sistema.");
		        }
		}
	}
	@Override
	public List<String> listarRutasDeVuelo(String aerolinea) throws RutaNoExisteException {
		// TODO Auto-generated method stub
		List<String> rutas = mcu.obtenerRutasDeVuelo(aerolinea.trim());
	
		if(rutas == null) {
			throw new RutaNoExisteException("No hay rutas asociadas a esta aerolinea");
		}
		Collections.sort(rutas);
		return rutas;
	}
	
	public List<String> obtenerDatosRuta(String nombre){
		return mcr.obtenerDatosRuta(nombre);
	}
	
	@Override
	public List<String> listarVuelos(String ruta) throws VueloNoExisteException{
		// TODO Auto-generated method stub
		List<String> vuelos = mcr.obtenerVuelos(ruta);
		if(vuelos == null) {
			throw new VueloNoExisteException("No existen vuelos asociados a la ruta ingresada");
		}else {
			//Collections.sort(vuelos);
		return vuelos;
		}
	}
	@Override
	public List<String> imagenesVuelos(String ruta) throws VueloNoExisteException{
		// TODO Auto-generated method stub
		List<String> vuelos = mcr.obtenerImagenesVuelos(ruta);
		if(vuelos == null || vuelos.isEmpty()) {
			throw new VueloNoExisteException("No existen imagenes de vuelos asociados a la ruta ingresada");
		}else {
		return vuelos;
		}
	}
	@Override
	public DTVuelo mostrarDatosDeVuelo(String vuelo) throws VueloNoExisteException {
	    // Simplemente llamas al método obtenerDatosDeVuelo y permites que la excepción se propague
	    return mcr.obtenerDatosDeVuelo(vuelo);
	}


	@Override
	public void ingresarDatos(String Aerolinea, String RutaVuelo, LocalDate fecha, LocalDate fechaAlta, String nombre,
			int asientosTurista, int asientosEjecutivos, int duracion) throws DatosInvalidosException {
		// TODO Auto-generated method stub
			System.out.println("La aerolinea ingresada es "+Aerolinea);
			System.out.println("La ruta ingresada es "+RutaVuelo);
			System.out.println("La fecha de alta ingresada es "+fechaAlta);
			System.out.println("La fecha ingresada es "+fecha);
			checkearDatosVuelo(Aerolinea, RutaVuelo, fecha, fechaAlta,  nombre, asientosTurista,asientosEjecutivos,duracion);
			mcr.crearVuelo(RutaVuelo, nombre, asientosTurista, asientosEjecutivos, duracion, fecha, fechaAlta);
	}
	private boolean vueloYaExiste(String nombre) {
		ManejadorCRutasDeVuelo mcr = ManejadorCRutasDeVuelo.getInstancia();
		return mcr.vueloYaExiste(nombre);
	}
	private boolean noExisteAerolinea(String Aerolinea) {
		ManejadorCUsuarios mcu = ManejadorCUsuarios.getInstancia();
		Aerolinea aero = mcu.obtenerAerolinea(Aerolinea);
		if(aero == null) {
			return true;
		}
		return false;
	}
	private boolean noExisteRuta(String ruta) {
		ManejadorCRutasDeVuelo mcr = ManejadorCRutasDeVuelo.getInstancia();
		RutaDeVuelo rut = mcr.obtenerRuta(ruta.trim());
		System.out.println("el nombre de la ruta es " +rut);
		if(rut == null) {
			return true;
		}else {
			return false;
		}
	}
	@Override
	public String[] listarCiudades() throws UsuarioNoExisteException {
		// TODO Auto-generated method stub
		String[] nciudades;
		nciudades = mcr.listarCiudades();
		if(nciudades == null){
			throw new UsuarioNoExisteException("no hay ciudades");
		}
		Arrays.sort(nciudades);
		return nciudades;
		
	}

	@Override
	public String[] listarCategorias() throws UsuarioNoExisteException {
		// TODO Auto-generated method stub
		String[] ncat = mcr.listarCategorias();
		if(ncat == null){
			throw new UsuarioNoExisteException("no hay categorias");
		}
		Arrays.sort(ncat);
		return ncat;
	}
	
    public void ingresarDatosVuelo(String cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, String vuelo) throws VueloNoExisteException, UsuarioNoExisteException, DatosInvalidosException, AsientosInsuficientesException{
        // Obtener el cliente
        Cliente clienteObj = mcu.obtenerCliente(cliente);
        if (clienteObj == null) {
        	throw new UsuarioNoExisteException("El cliente con el nombre ingresado no existe" );  
        }
        if(cantEqExtra < 0) {
	    	throw new DatosInvalidosException("La cantidad de equipaje extra no puede ser negativa.");
	    }
        if(fechaDeReserva == null) {
	    	throw new DatosInvalidosException("la fecha no puede ser vacia");
	    }
        if (!tipo.equalsIgnoreCase("Turista") && !tipo.equalsIgnoreCase("Ejecutivo")) {
	        throw new DatosInvalidosException("El tipo debe ser 'Turista' o 'Ejecutivo'.");
	    }
        System.out.println("EL NOMBRE QUE LLEGA ES:" +vuelo);
        // Obtener el vuelo
        Vuelo vueloObj = mcr.obtenerVuelo(vuelo.trim());
        if (vueloObj == null) {
        	throw new VueloNoExisteException("El vuelo con el nombre ingresado no existe"+vuelo );  
        }
		if(vueloObj.getFechaAlta().isAfter(fechaDeReserva)) {
			throw new DatosInvalidosException("La fecha de reserva no puede ser anterior a la fecha de alta del vuelo");}
		if(tipo.equalsIgnoreCase("turista")){
        	int asientosDisp = mcr.getAsientosTuristaDisponibles(vueloObj);
        	if(asientosDisp == 0) {
				throw new AsientosInsuficientesException("No hay asientos disponibles en clase turista");
			}
		}else {
			int asientosDisp = mcr.getAsientosEjecutivoDisponibles(vueloObj);
			if(asientosDisp == 0) {
				throw new AsientosInsuficientesException("No hay asientos disponibles en clase ejecutiva");}}
        
        // Crear y agregar la reserva y pasajer
        mcr.crearReservaYPasaje(clienteObj, tipo, cantEqExtra, fechaDeReserva, vueloObj);
        
    }

    // Nueva función para múltiples pasajeros
	public void ingresarNombres(String cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, String vuelo, int cantPasajeros, Set<String[]> nombresApellidosPasajeros) throws VueloNoExisteException, UsuarioNoExisteException, DatosInvalidosException, AsientosInsuficientesException {
		// Obtener el cliente
		checkearDatosReserva(cliente, tipo,cantEqExtra,fechaDeReserva,vuelo,cantPasajeros,nombresApellidosPasajeros);
		Cliente clienteObj = mcu.obtenerCliente(cliente);
		int cantidadAsientos = nombresApellidosPasajeros.size() + 1;
		// Obtener el vuelo
		Vuelo vueloObj = mcr.obtenerVuelo(vuelo);
		if(vueloObj.getFechaAlta().isAfter(fechaDeReserva)) {
			throw new DatosInvalidosException("La fecha de reserva no puede ser anterior a la fecha de alta del vuelo");}

		// Crear y agregar la reserva y pasajeros
		if(tipo.equalsIgnoreCase("Turista")){
        	int asientosDisp = mcr.getAsientosTuristaDisponibles(vueloObj);
        	if(asientosDisp < cantidadAsientos ) {
				throw new AsientosInsuficientesException("No hay asientos disponibles en clase turista");
			}
		}else {
			int asientosDisp = mcr.getAsientosEjecutivoDisponibles(vueloObj);
			if(asientosDisp < cantidadAsientos) {
				throw new AsientosInsuficientesException("No hay asientos disponibles en clase ejecutiva");}
        
		mcr.crearReservaConPasajeros(clienteObj, tipo, cantEqExtra, fechaDeReserva, vueloObj, nombresApellidosPasajeros); 		
	}}
	private void checkearDatosReserva(String cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, String vuelo, int cantPasajeros, Set<String[]> nombresApellidosPasajeros) throws DatosInvalidosException {
		if (cliente.trim().isEmpty()) {
	        throw new DatosInvalidosException("El cliente no puede estar vacío.");
	    }
		if (tipo.trim().isEmpty()) {
	        throw new DatosInvalidosException("El tipo no puede estar vacío.");
	    }
	    if(cantEqExtra < 0) {
	    	throw new DatosInvalidosException("La cantidad de equipaje extra no puede ser negativa.");
	    }
	    if(cantPasajeros < 0) {
	    	throw new DatosInvalidosException("La cantidad de pasajeros no puede ser negativa.");
	    }
	    if(fechaDeReserva == null) {
	    	throw new DatosInvalidosException("la fecha no puede ser vacia");
	    }
	    if (vuelo.trim().isEmpty()) {
	        throw new DatosInvalidosException("El vuelo no puede estar vacío.");
	    }
	    if (!tipo.equalsIgnoreCase("turista") && !tipo.equalsIgnoreCase("ejecutivo")) {
	        throw new DatosInvalidosException("El tipo debe ser 'turista' o 'ejecutivo'.");
	    }
	    for (String[] nombreApellido : nombresApellidosPasajeros) {
	        String nombre = nombreApellido[0].trim();
	        String apellido = nombreApellido[1].trim();
	        
	        // Validar que el nombre no sea vacío y solo contenga letras
	        if (nombre.trim().isEmpty() || !nombre.matches("[\\p{L}]+")) {
	            throw new DatosInvalidosException("El nombre '" + nombre + "' es inválido. Debe contener solo letras.");
	        }
	        
	        // Validar que el apellido no sea vacío y solo contenga letras
	        if (apellido.trim().isEmpty() || !apellido.matches("[\\p{L}]+")) {
	            throw new DatosInvalidosException("El apellido '" + apellido + "' es inválido. Debe contener solo letras.");
	        }
	    }
	    if(mcr.obtenerVuelo(vuelo) == null) {
			throw new DatosInvalidosException("El vuelo ingresado no existe");
		}
	    if(mcu.obtenerCliente(cliente) == null) {
			throw new DatosInvalidosException("El cliente ingresado no existe");
		}
	}
	public List<String> listarReservas(String vuelo) {
		Vuelo  vueloObj = mcr.obtenerVuelo(vuelo);
		List<String> res = mcr.listarReservas(vueloObj);
		Collections.sort(res);
		return res;
		}
    // Nueva función para múltiples pasajeros

	
	public int cuantasRutasHay() {
		return mcr.cuantasRutasHay();
	}
    
	public List<String> cambiarVuelo(String vuelo, String ruta){
		return mcr.cambioVueloEnReserva(vuelo, ruta);
	}
	public void altaCiudad(String nombre, String pais, String aeropuerto, String descripcion, String website, LocalDate fechaAlta) throws CiudadExistenteException, DatosInvalidosException{
		checkearDatosCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
		if (mcr.ciudadYaExiste(nombre, pais)) {
			throw new CiudadExistenteException("ya existe una ciudad con ese nombre en ese pais");
        }
        else {
        	mcr.agregarCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
        }
    }
	
	public void checkearDatosCiudad(String nombre, String pais, String aeropuerto, String descripcion, 
			String website, LocalDate fechaAlta) throws DatosInvalidosException {
		
		if(nombre.trim().isEmpty()) {
			throw new DatosInvalidosException("El nombre es invalido");
		}
		if(!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ \\s]+$" )) {

			throw new DatosInvalidosException("El nombres es invalido, solo puede contener letras");
		}
		if(pais.trim().isEmpty()) {
			throw new DatosInvalidosException("El pais es invalido, no puede ser vacio.");
		}
		if(!pais.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ \\s]+$" )) {

			throw new DatosInvalidosException("El pais es invalido, solo puede contener letras");
		}
		if(aeropuerto.trim().isEmpty()) {
			throw new DatosInvalidosException("El aeropuerto es invalido, no puede ser vacio.");
		}

		if(descripcion.trim().isEmpty()) {
			throw new DatosInvalidosException("El descripcion es invalido, no puede ser vacio ");
		}
		if(fechaAlta == null) {
			throw new DatosInvalidosException("La fechaAlta no puede ser vacia");
		}
		if(website.trim().isEmpty()) {
			throw new DatosInvalidosException("La website es invalido, no puede ser vacia");
		}
		
		}
	private void checkearDatosVuelo(String Aerolinea, String RutaVuelo, LocalDate fecha, LocalDate fechaAlta, String nombre,
			int asientosTurista, int asientosEjecutivos, int duracion) throws DatosInvalidosException {
		if(Aerolinea.trim().isEmpty()) {
			throw new DatosInvalidosException("La aerolinea es invalida, no puede ser vacío");
		}
		if(RutaVuelo.trim().isEmpty()) {
			throw new DatosInvalidosException("La ruta de vuelo es invalida, no puede ser vacía");
		}
		if(fechaAlta == null) {
			throw new DatosInvalidosException("La fechaAlta no puede ser vacia");
		}
		if(fecha == null) {
			throw new DatosInvalidosException("La fecha no puede ser vacia");
		}

		if(fechaAlta.isAfter(fecha)) {
			throw new DatosInvalidosException("La fecha de alta no puede ser posterior a la fecha del vuelo");}
		if(nombre.trim().isEmpty()) {
			throw new DatosInvalidosException("El nombres es invalido, no puede ser vacío");
		}
		if ((asientosTurista + asientosEjecutivos) < 1) {
	       throw new DatosInvalidosException("La cantidad de asientos debe ser mayor que 0.");
	    }
		if (asientosEjecutivos < 0) {
			throw new DatosInvalidosException("La cantidad de asientos ejecutivos debe ser mayor o igual que 0.");
	    }
		if (asientosTurista < 0) {
			throw new DatosInvalidosException("La cantidad de asientos turista debe ser mayor o igual que 0.");
	    }
		if (duracion < 1) {
	        throw new DatosInvalidosException("La duración del vuelo debe ser mayor que 0.");
	    }
		if(noExisteRuta(RutaVuelo)) {
			throw new DatosInvalidosException("La ruta de vuelo ingresada no existe");
		}
		System.out.println("SE CHEQUEA TODO BIEN");
		if(vueloYaExiste(nombre)) {
			throw new DatosInvalidosException("El vuelo con el nombre ingresado ya existe" );
		}if(noExisteAerolinea(Aerolinea)) {
			throw new DatosInvalidosException("La aerolinea ingresada no existe");
		
		}
		
	}

	public boolean chequearReserva(String cliente, String vuelo) {
	    List<String> reservas = mcu.obtenerVuelosReservados(cliente);
	    // Verificar si la reserva ya existe
	    for (String reserva : reservas) {
	        if (reserva.equalsIgnoreCase(vuelo)) {
	            return true;
	        }
	    }
	    return false;
	}

	
	
	@Override
	public DTParAerolineaRuta getPar(String vueloSeleccionado) {
		// TODO Auto-generated method stub
		DTParAerolineaRuta dtpar = mcr.getPar(vueloSeleccionado);
		return dtpar;
	}
	
	public DTRutaDeVuelo obtenerInfoRuta(String ruta)throws RutaNoExisteException {
		return mcr.obtenerDatosDeRuta(ruta);
	}

	@Override
	public String getAerolinea(String nombreRuta) {
		// TODO Auto-generated method stub
		
		return mcr.obtenerAero(nombreRuta);
	}


	public DTCiudad obtenerDatosCiudad(String nombre, String pais ){
		return mcr.obtenerDatosCiudad(nombre,pais);
	}

	@Override
	public RutaDeVuelo findRutaDeVuelo(String nomRuta) throws RutaNoExisteException {
		// TODO Auto-generated method stub
		return mcr.findRutaDeVuelo(nomRuta);
	}
	@Override
	public List<DTRutaDeVuelo> obtenerRutasPorCategoria(String categoria) {
	    List<DTRutaDeVuelo> rutasPorCategoria = new ArrayList<>();
	    
	    // Obtenemos la lista de rutas desde el manejador
	    List<RutaDeVuelo> todasLasRutas = mcr.getRutas(); // Método que devuelve todas las rutas

	    // Recorremos la lista de rutas para filtrar por categoría
	    for (RutaDeVuelo ruta : todasLasRutas) {
	        // Verificamos si la categoría está en la lista de categorías de la ruta
	        if (ruta.getCategorias().stream().anyMatch(cat -> cat.getNombre().equalsIgnoreCase(categoria))) {
	            // Convertimos la RutaDeVuelo a DTRutaDeVuelo para devolver en la lista
	            DTRutaDeVuelo dtRuta = new DTRutaDeVuelo(
	                ruta.getName(), 
	                ruta.getDescripcionCorta(), 
	                ruta.getDescripcion(), 
	                ruta.getHora(), 
	                (int) ruta.getCostoBaseTurista(), 
	                (int) ruta.getCostoBaseEjecutivo(), 
	                (int) ruta.getCostoEquipajeExtra(), 
	                ruta.getFecha()
	            );
	            dtRuta.setImagen(ruta.getImagen());
	            rutasPorCategoria.add(dtRuta);
	        }
	    }

	    return rutasPorCategoria;
	}
	public List<Vuelo> listarVueloss(){
		List<Vuelo> vuel = mcr.getVuelos();
		return vuel;
	}
	public List<RutaDeVuelo> obtenerRutasConfirmadas(){
		return mcr.listarRutasConfirmadas();
	}
	public RutaDeVuelo obtenerRuta2(String nombre) {
		return mcr.obtenerRuta2(nombre);
	}
	public Vuelo obtenerVuelo(String nom) {
		return mcr.obtenerVuelo2(nom);
	}
}
