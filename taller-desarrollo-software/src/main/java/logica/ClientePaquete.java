package logica;

import java.time.LocalDate;

public class ClientePaquete {
	//private LocalDate realizacion,vencimiento;
	//private float costo;
	private Paquete paquete;
	
	public ClientePaquete() {
		
	}

	public String obtenerNombrePaquete() {
		// TODO Auto-generated method stub
		return paquete.getNombre();
	}

}
