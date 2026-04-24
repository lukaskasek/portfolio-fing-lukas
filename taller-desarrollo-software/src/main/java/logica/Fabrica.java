package logica;

public class Fabrica {
	//tiene que ser singleton
	 static private Fabrica Instancia = null;
	 private   Fabrica() {
	 }
	 public static Fabrica getInstancia() {
		 if (Instancia == null) {
			 Instancia= new Fabrica();
			 return Instancia;
		 }else {
			return Instancia; 
		 }
	 }
	 public IControladorUsuarios getIControladorUsuarios() {
		 return new ControladorUsuarios();
	 }
	 
	 public IControladorRutasDeVuelo getIControladorRutasDeVuelo() {
		 return new ControladorRutaDeVuelo();
	 }
	public IControladorPaquetes getIControladorPaquetes() {
		// TODO Auto-generated method stub
		return new ControladorPaquetes();
	}
	
}
