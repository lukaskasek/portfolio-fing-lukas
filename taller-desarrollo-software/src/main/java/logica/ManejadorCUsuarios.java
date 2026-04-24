package logica;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ManejadorCUsuarios {
	//es singleton
	private static ManejadorCUsuarios instancia = null;
	private List<Usuario> usuarios;
	private ManejadorCUsuarios() {
		this.usuarios = new ArrayList<Usuario>();
	}
	public static ManejadorCUsuarios getInstancia() {
		if(instancia == null) {
			instancia= new ManejadorCUsuarios();
			return instancia;
			
		}else {
			return instancia;
		}
	}
	public void addUser(Cliente cli) {
		
		usuarios.add(cli);
	}
	public void addUser(Aerolinea aer) {
		usuarios.add(aer);
	}
	public boolean usuarioYaExiste(String nickName) {
		for(Usuario u : usuarios) {
			
			if(u.getNickName().equalsIgnoreCase(nickName)) {
				return true;
			}
		}
		return false;
	}
	public List<String> obtenerRutasDeVueloIngresadas(String Aerolinea){
		int ite=0;
		while(ite< usuarios.size() && usuarios.get(ite).getNickName()!= Aerolinea) {
			ite++;
		}
		if( ite < usuarios.size() && usuarios.get(ite).getNickName() == Aerolinea) {
			Aerolinea aer = (Aerolinea) usuarios.get(ite);
			return aer.getRutasIngresadas();
		}else{
			return null;
		}
	}

	public int cuantasRutasHay(String hhh){
		for (Usuario u : usuarios) {
			if (u.getNickName().equals(hhh)) {
				return ((Aerolinea) u).cuantasRutasTiene();
			}
		}
		return 52;
	}
	
	public List<String> darNickNamesUsuarios() {
		List<String> nicks = new ArrayList<String>();
		for(Usuario u : usuarios) {
			String nick = u.getNickName();
			nicks.add(nick);
		}
		Collections.sort(nicks);
		return nicks;
	}
	public DTUser obtenerUser(String nickName) {
		
		int ite = 0;
		
		while (ite < usuarios.size() && !usuarios.get(ite).getNickName().equalsIgnoreCase(nickName)) {
			ite++;
		}
		if(usuarios.get(ite).getNickName().equalsIgnoreCase(nickName)) {
			 return usuarios.get(ite).obtenerDatos();
		}else {
			return null;
		}
	}
	public List<String> obtenerNombresRutasDeVuelo(String nickName){
		
		int ite = 0;
		
		while (ite < usuarios.size() && usuarios.get(ite).getNickName() != nickName) {
			ite++;
		}
		if(usuarios.get(ite).getNickName() == nickName) {
			
			 Aerolinea aer = (Aerolinea) usuarios.get(ite);
			 return aer.obtenerNombresRutaDeVuelo();
			
		}else {
			return new ArrayList<>();
		}
	}
	public List<String> obtenerVuelosReservados(String nickName) {
		// TODO Auto-generated method stub
		int ite = 0;
		
		while (ite < usuarios.size() && !usuarios.get(ite).getNickName().equalsIgnoreCase(nickName) ) {
			ite++;
		}
		if(ite < usuarios.size() && usuarios.get(ite).getNickName().equalsIgnoreCase(nickName)) {
			 Cliente aer = (Cliente) usuarios.get(ite);
			 return aer.obtenerReservasDeVuelo();
		}else {
			return null;
		}

	}
	public List<String> obtenerNombresPaquetes(String nickName) {
		// TODO Auto-generated method stub
		int ite = 0;
		
		while (ite < usuarios.size() && usuarios.get(ite).getNickName() != nickName) {
			ite++;
		}
		if(usuarios.get(ite).getNickName() == nickName) {
			 Cliente cli = (Cliente) usuarios.get(ite);
			 return cli.obtenerNombresPaquete();
		}else {
			return null;
		}
	}
	public void cambiarDatosCliente(String nickName, String nuevoNombre,String nuevoApellido, LocalDate nuevaFechaNac,
			String nuevaNacionalidad, String nuevoTipoDoc, String nuevoNUmDoc) {
		// TODO Auto-generated method stub
		int ite = 0;
		
		while (ite < usuarios.size() && usuarios.get(ite).getNickName() != nickName) {
			ite++;
		}
		if(usuarios.get(ite).getNickName() == nickName) {
			 Cliente cli = (Cliente) usuarios.get(ite);
			 cli.cambiarDatos(nuevoApellido,nuevoNombre,nuevaFechaNac,nuevaNacionalidad,nuevoTipoDoc,nuevoNUmDoc);
		}
		
	}
	public void cambiarDatosAerolinea(String nickName,String nuevoNombre, String nuevaDesc, String nuevoLink) {
		// TODO Auto-generated method stub
		int ite = 0;
		
		while (ite < usuarios.size() && usuarios.get(ite).getNickName() != nickName) {
			ite++;
		}
		if(usuarios.get(ite).getNickName() == nickName) {
			 Aerolinea aer = (Aerolinea) usuarios.get(ite);
			 aer.cambiarDatosAerolinea(nuevoNombre,nuevaDesc,nuevoLink);
		}
	}
	public List<String> obtenerNickNamesAerolineas() {
		// TODO Auto-generated method stub
		List<String> nicks = new ArrayList<String>();
		for(Usuario a : usuarios) {
			if(a  instanceof Aerolinea) {
				nicks.add(a.getNickName());
			}
		}
		Collections.sort(nicks);
		return nicks;
	}
	public Aerolinea obtenerAerolinea(String aerolinea) {
		// TODO Auto-generated method stub
		int ite = 0;
		
		while (ite < usuarios.size() && !usuarios.get(ite).getNickName().equals(aerolinea) ) {
			ite++;
		}
		if (ite < usuarios.size() && usuarios.get(ite).getNickName().equals(aerolinea)) {
		    Aerolinea aer = (Aerolinea) usuarios.get(ite);
		    System.out.println("Encontre a " + aer.getNickName());
		    return aer;
		} else {
		    return null;  // Retorna null si no la encuentras
		}
		}
	public Cliente obtenerCliente(String cliente) {
		// TODO Auto-generated method stub
		int ite = 0;
		
		while (ite < usuarios.size() && !usuarios.get(ite).getNickName().trim().equalsIgnoreCase(cliente.trim()) ) {
			ite++;
		}
		if(ite< usuarios.size() && usuarios.get(ite).getNickName().equalsIgnoreCase(cliente) ) {
			 Cliente cli = (Cliente) usuarios.get(ite);
			 return cli;
		}else {
			return null;
		}
	}
	public List<String> obtenerRutasDeVuelo(String Aerolinea){
		int ite=0;
		while(ite< usuarios.size() && usuarios.get(ite).getNickName()!= Aerolinea) {
			ite++;
		}
		if( ite < usuarios.size() && usuarios.get(ite).getNickName() == Aerolinea) {
			Aerolinea aer = (Aerolinea) usuarios.get(ite);
			return aer.getRutas();
		}else{
			return null;
		}
	}
	public List<String> obtenerClientes(){
		List<String> nicks = new ArrayList<String>();
		if(usuarios.size() == 0) {
			return nicks;
		}
		for(Usuario a : usuarios) {
			if(a  instanceof Cliente) {
				nicks.add(a.getNickName());
			}
		}
		Collections.sort(nicks);
		return nicks;
	}
    

    
	public List<String> cambioAerolineaEnReserva(String aerolineaActual) {
        List<String> aerolineas = this.obtenerNickNamesAerolineas();
        if (aerolineas == null || aerolineas.size() <= 1) {
            return null; // Si no hay otras aerolíneas, retornar null
        }

        List<String> aerolineasFiltradas = new ArrayList<>();
        for (String aerolinea : aerolineas) {
            if (!aerolinea.equals(aerolineaActual)) {
                aerolineasFiltradas.add(aerolinea);
            }
        }

        return aerolineasFiltradas.isEmpty() ? null : aerolineasFiltradas;
    }
	
    public List<String> cambioClienteEnReserva(String clienteActual) {
        List<String> clientes = this.obtenerClientes();
        if (clientes == null || clientes.size() <= 1) {
            return null; // Si no hay otros clientes, retornar null
        }

        List<String> clientesFiltrados = new ArrayList<>();
        for (String cliente : clientes) {
            if (!cliente.equals(clienteActual)) {
                clientesFiltrados.add(cliente);
            }
        }

        return clientesFiltrados.isEmpty() ? null : clientesFiltrados;
    }

    
    public List<String> cambioRutaEnReserva(String rutaActual, String aerolinea) {
        List<String> rutas = this.obtenerRutasDeVuelo(aerolinea);
        if (rutas == null || rutas.size() <= 1) {
            return null; // Si no hay otras rutas, retornar null
        }

        List<String> rutasFiltradas = new ArrayList<>();
        for (String ruta : rutas) {
            if (!ruta.equals(rutaActual)) {
                rutasFiltradas.add(ruta);
            }
        }

        return rutasFiltradas.isEmpty() ? null : rutasFiltradas;
    }
	public boolean usuarioYaExisteMail(String email) {
		// TODO Auto-generated method stub
		for(Usuario u : usuarios) {
			
			if(u.getEmail().equalsIgnoreCase(email)) {
				return true;
			}
		}
		return false;
	}
}

