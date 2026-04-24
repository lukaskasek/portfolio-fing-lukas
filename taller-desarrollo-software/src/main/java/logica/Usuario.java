package logica;

public abstract class Usuario {
	private String Nombre;
	private String nickName;
	private String Email;
	private String Contraseña,foto;
	public Usuario(String Nombre,String nickName,String Email,String contra,String foto) {
		this.Nombre=Nombre;
		this.nickName = nickName;
		this.Email = Email;
		this.Contraseña=contra;
		this.foto = foto;
	}
	public String getFoto() {
		return foto;
	}
	public String getNombre() {
		return Nombre;
	}
	public String getContra() {
		return Contraseña;
	}
	public String getNickName() {
		return nickName;
	}
	public String getEmail() {
		return Email;
	}
	public void setNombre(String nom) {
		Nombre = nom;
	}
	public void setNickName(String nnn) {
		nickName = nnn;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public abstract DTUser obtenerDatos(); 
}
