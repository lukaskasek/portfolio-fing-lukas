package logica;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
	private String apellido;
	private LocalDate fechaNac;
	private String nacionalidad;
	private String tipoDoc;
	private String numdoc;
	private List<Reserva> reservas;
	private List<ClientePaquete> clientePaquetes;
	public Cliente(String nombre,String apellido,String nacionalidad,String tipoDoc,String nickName,String Email,LocalDate fechaNac, String numDoc,String contra,String foto){
		super(nombre,nickName,Email,contra,foto);
		this.apellido = apellido;
		this.nacionalidad=nacionalidad;
		
		this.fechaNac = fechaNac;
		this.numdoc = numDoc;
		this.tipoDoc = tipoDoc;
		this.clientePaquetes = new ArrayList<ClientePaquete>();
		this.reservas = new ArrayList<Reserva>();
	}
	public String getApellido() {
		return apellido;
	}
	public LocalDate getFechaNac() {
		return fechaNac;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public String getTipoDoc() {
		return tipoDoc;
	}
	public String getNumDoc() {
		return numdoc;
	}
	public void setApellido(String ape) {
		this.apellido = ape;
	}
	public void setFechaNac(LocalDate fec) {
		this.fechaNac = fec;
	}
	public void setNacionalidad(String nac) {
		this.nacionalidad=nac;
	}
	public void setTipoDoc(String tid) {
		this.tipoDoc = tid;
	}
	public void setNumDoc(String nud) {
		this.numdoc = nud;
	}
    public void addReserva(Reserva nuevaReserva) {
        reservas.add(nuevaReserva);
    }

	public DTUser obtenerDatos() {
		
		return new DTCliente(this.getNickName(),this.getNombre(),this.getEmail(),this.apellido,this.nacionalidad,this.tipoDoc,this.numdoc,this.fechaNac, this.getContra(),this.getFoto());
	}
	public List<String> obtenerReservasDeVuelo() {
		// TODO Auto-generated method stub
		List<String> nombres = new ArrayList<String>();
		for(Reserva r : reservas) {
			String nombre = r.obtenerNombreVuelo();
			nombres.add(nombre);
		}
		return nombres;
	}
	
	public List<String> obtenerNombresPaquete() {
		// TODO Auto-generated method stub
		List<String> nombres = new ArrayList<String>();
		for(ClientePaquete cp : clientePaquetes) {
			String name = cp.obtenerNombrePaquete();
			nombres.add(name);
		}
		return nombres;
	}
	public void cambiarDatos(String nuevoApellido,String nuevoNombre, LocalDate nuevaFechaNac, String nuevaNac, String nuevoTipoDoc, String nuevoNUmDoc) {
		// TODO Auto-generated method stub
		this.setNombre(nuevoNombre);
		this.apellido = nuevoApellido;
		this.fechaNac =nuevaFechaNac;
		this.tipoDoc = nuevoTipoDoc;
		this.numdoc = nuevoNUmDoc;
		this.nacionalidad = nuevaNac;
	}

}



	