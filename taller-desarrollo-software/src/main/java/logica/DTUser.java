package logica;

public class DTUser {
	private String nick,nombre,correo,con,foto;
	public DTUser(String nick,String nom, String cor, String con,String foto) {
		this.nick = nick;
		this.nombre = nom;
		this.correo = cor;
		this.con = con;
		this.foto = foto;
	}
	public String getNick() {
		return this.nick;
	}
	public String getName() {
		return this.nombre;
	}
	public String getCorreo() {
		return this.correo;
	}
	public String getContra() {
		return this.con;
	}
	public String getFoto() {
		return this.foto;
	}
}
