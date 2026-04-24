package logica;

public class Pasaje {
	private String nombre;
	private String apellido;
	private int num;
	public Pasaje(int num, String nombre, String apellido) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.num = num;
	}
	public int getNum() {
		return this.num;
	}
	public String getNombre() {
		return this.nombre;
	}
	public String getApellido() {
		return this.apellido;
	}
}
