package logica;

import java.time.LocalDate;

public class DTCliente extends DTUser {
	private String apellido,nacionalidad,tDocumento,nDocumento;
	private LocalDate fechanac;
	public DTCliente(String nic,String nom,String correo,String ape,String nacion,String tid,String nud,LocalDate fec, String con,String foto) {
		super(nic,nom,correo,con,foto);
		this.apellido = ape;
		this.fechanac = fec;
		this.nacionalidad = nacion;
		this.nDocumento = nud;
		this.tDocumento = tid;
	}
	public String getApellido() {
		return this.apellido;
	}
	public String getNacion() {
		return this.nacionalidad;
	}
	public String getTDocumento() {
		return this.tDocumento;
	}
	public String getNDocumento() {
		return this.nDocumento;
	}
	public LocalDate getFechaNac() {
		return this.fechanac;
	}

}
