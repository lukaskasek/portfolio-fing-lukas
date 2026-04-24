package excepciones;

public class VueloYaExisteException extends Exception {
	public VueloYaExisteException(String mensaje) {
		super(mensaje);
	}
}
