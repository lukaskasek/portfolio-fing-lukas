package excepciones;

public class UsuarioNoExisteException extends Exception {
	public UsuarioNoExisteException(String s) {
		super(s);
	}
}
