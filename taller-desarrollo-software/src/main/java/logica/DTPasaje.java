package logica;

public class DTPasaje {
	private String nombre, apellido;
	private int num;
	public DTPasaje(String nom, String ape, int nnn) {
		this.apellido = ape;
		this.nombre = nom;
		this.num = nnn;
	}
	public String getNombre() {
		return this.nombre;
	}
	public String getApellido() {
		return this.apellido;
	}
	public int getNum() {
		return this.num;
	}
}
