package CSVs;

import excepciones.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import excepciones.CiudadExistenteException;
import excepciones.DatosInvalidosException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VueloNoExisteException;
import excepciones.VueloYaExisteException;
import logica.DTAerolinea;
import logica.DTCategoria;
import logica.DTCiudad;
import logica.DTCliente;
import logica.DTReserva;
import logica.DTRutaDeVuelo;
import logica.DTUser;
import logica.DTVuelo;
import logica.DatosPasaje;
import logica.Fabrica;
import logica.IControladorPaquetes;
import logica.IControladorRutasDeVuelo;
import logica.IControladorUsuarios;
import logica.TuplaDatosReserva;
import logica.TuplaDatosRutaDeVuelo;
import logica.TuplaDatosVuelo;
import logica.Vuelo;
import excepciones.CategoriaExistenteException;
import excepciones.AsientosInsuficientesException;

public class CargarDatos {
    // Crear instancias de los controladores
    Fabrica fab = Fabrica.getInstancia();
    IControladorUsuarios iCU = fab.getIControladorUsuarios();
    IControladorRutasDeVuelo iCRV = fab.getIControladorRutasDeVuelo();
    IControladorPaquetes icp = fab.getIControladorPaquetes();
    String separador=";";
    String linea="";
    
    // Variables para almacenar los datos cargados
    private Map<String, DTUser> usuarios = new HashMap<>();
    private Map<String, DTCliente> clientes = new HashMap<>();
    private Map<String, DTAerolinea> aerolineas = new HashMap<>();
    private Map<String, DTCiudad> ciudades ;
    private Map<String, DTCategoria> categorias ;
	private Map<String, TuplaDatosRutaDeVuelo> rutasDeVuelo = new HashMap<>();
    private  Map<String,TuplaDatosVuelo> vuelos = new HashMap<>();
    private Map<String,TuplaDatosReserva> reservas;
    private final String rutaBase = "Escritorio/serverside/Tarea2-serverSide2/src/main/java/CSVs/";

    public void cargar() {
        cargarUsuarios();
        cargarClientes();
        cargarAerolineas();
        ciudades=cargarCiudades();
        cargarCiudadesSistema();
        
        categorias = cargarCategorias();
        cargarCategoriasSistema();
        

        cargarRutasVuelos();
        cargarVuelos();
        cargarPasajes();
        
    }

    private void cargarUsuarios() {
        String archivousuarios = rutaBase + "2024Usuarios.csv";
        
        String linea;
        String separador = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(archivousuarios))) {
           String l= br.readLine(); // Leer encabezado
            while ((linea = br.readLine()) != null) {
            	
                String[] valores = linea.split(separador);
                
                DTUser u = new DTUser(valores[2], valores[3], valores[4], valores[5].trim(), valores[6]);
                usuarios.put(valores[0], u);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void cargarClientes() {
        String archivoClientes = rutaBase + "2024Usuarios-Clientes.csv";
        String linea;
        String separador = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(archivoClientes))) {
            br.readLine(); // Leer encabezado
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(separador);
                String fecha = campos[2];
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    LocalDate ff = LocalDate.parse(fecha, format);
                    DTCliente c = new DTCliente(
                    		usuarios.get(campos[0]).getNick(),usuarios.get(campos[0]).getName(), usuarios.get(campos[0]).getCorreo(), campos[1], campos[3], campos[4], campos[5], ff,usuarios.get(campos[0]).getContra() ,usuarios.get(campos[0]).getFoto());
                    clientes.put(campos[0], c);
                    // Agregar cliente al sistema
                    iCU.NuevoCliente(c.getNick(), c.getName(), c.getApellido(), c.getCorreo(), c.getFechaNac(),c.getNacion(), c.getTDocumento(), c.getNDocumento(), c.getContra(), c.getFoto());
                } catch (DateTimeParseException | UsuarioYaExisteException | DatosInvalidosException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }
    
    private void cargarAerolineas() {
        String archivoAerolineas = rutaBase + "2024Usuarios-Aerolineas.csv";
        Map<String, DTAerolinea> aerolineas = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAerolineas))){
            br.readLine();

        	while((linea = br.readLine())!= null) {
        		String[] campos = linea.split(separador);
      
        			DTAerolinea a = new DTAerolinea(usuarios.get(campos[0]).getName(),usuarios.get(campos[0]).getNick(),usuarios.get(campos[0]).getCorreo(),campos[1],campos[2],usuarios.get(campos[0]).getContra(),usuarios.get(campos[0]).getFoto());
        			aerolineas.put(campos[0], a);
        	}
        		
        }catch(IOException e6) {
        	e6.printStackTrace();
        }
        for(Map.Entry<String, DTAerolinea> entry : aerolineas.entrySet()) {
        	DTAerolinea a = entry.getValue();
        	try {
        		iCU.NuevaAerolinea(a.getNick(), a.getName(), a.getCorreo(), a.getDescr(), a.getLink(),a.getContra(),a.getFoto());
        		
        	}catch(UsuarioYaExisteException e6) {
        		e6.printStackTrace();
        	} catch (DatosInvalidosException e6) {
				// TODO Auto-generated catch block
				e6.printStackTrace();
			}
        }
    }
    
    private Map<String, DTCiudad> cargarCiudades() {
    	String archivoCiudades = rutaBase + "2024Ciudades.csv";
        Map<String, DTCiudad> ciudades = new HashMap<>();
        

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCiudades))) {
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String pl = br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(separador);
                
                // Parsing de la fecha
                try {
                	 // Creación del objeto DTCiudad
                	 LocalDate fechaAlta = LocalDate.parse(campos[6], formatter);
                    DTCiudad ciudad = new DTCiudad(campos[1],campos[2], campos[3], campos[4], campos[5], fechaAlta);
                    System.out.println("se pusp en el map la ciudad: "+campos[0]);
                    ciudades.put(campos[0].trim(),ciudad);  // Usamos el nombre de la ciudad como clave
                }catch(DateTimeParseException e7) {
                	e7.printStackTrace();
                }
               
            }

        } catch (IOException e8) {
            System.err.println("Error leyendo el archivo de ciudades: " + archivoCiudades);
            e8.printStackTrace();
        }
        return ciudades;
    }
    
    private void cargarCiudadesSistema() {// Proceso de creación de ciudades en el sistema
        for (Map.Entry<String, DTCiudad> entry : ciudades.entrySet()) {
            DTCiudad ciudad = entry.getValue();
            try {
                iCRV.altaCiudad(ciudad.getNombre(), ciudad.getPais(), ciudad.getAeropuerto(), ciudad.getDescripcion(), ciudad.getWebsite(), ciudad.getFechaAlta());
            } catch (CiudadExistenteException e9) {
                System.err.println("La ciudad " + ciudad.getNombre() + " ya existe.");
                e9.printStackTrace();
            } catch (DatosInvalidosException e1) {
            	System.err.println(e1.getMessage());
                e1.printStackTrace();
			}
        }
    } 
   
    private Map<String, DTCategoria> cargarCategorias() {
    	//creacion de categorias
        String archivoCategorias = rutaBase + "2024Categorias.csv";
        Map<String, DTCategoria> categorias = new HashMap<>();
        

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCategorias))) {
        	String lin = br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(separador);

                // Creación del objeto DTCategoria
                DTCategoria categoria = new DTCategoria(campos[1]);
                categorias.put(campos[0].trim(), categoria);  // Usamos el nombre de la categoría como clave
                System.out.println("se cargo en el map la categoria:"+campos[0].trim()+")");
            }

        } catch (IOException e10) {
            System.err.println("Error leyendo el archivo de categorías: " + archivoCategorias);
            e10.printStackTrace();
        }
        return categorias;
    }
    
   private void cargarCategoriasSistema() {
	   // Proceso de creación de categorías en el sistema
       for (Map.Entry<String, DTCategoria> entry : categorias.entrySet()) {
           DTCategoria categoria = entry.getValue();
          
               try {
					iCRV.altaCategoria(categoria.getNombre());
				} catch (CategoriaExistenteException e1) {
					System.err.println("La categoria " + categoria.getNombre() + " ya existe.");
                   e1.printStackTrace();
				} catch (DatosInvalidosException e1) {
					System.err.println(e1.getMessage());
					e1.printStackTrace();
				}
                  
       }
   }

  private void cargarRutasVuelos() {
      String archivoRutas = rutaBase + "2024RutasVuelos.csv";
  	 Map<String, List<String>> estadoImagen = new HashMap<>();

      try (BufferedReader br = new BufferedReader(new FileReader(archivoRutas))) {
     	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
     	 DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
     	 List<String> estImAgregar = new ArrayList<String>();;
     	 String p = br.readLine();
         while ((linea = br.readLine()) != null) {
             String[] campos = linea.split(separador);
             try {
             	
             	String catsss = campos[11];
                 List<String> lista_cats = Arrays.asList(catsss.split(","));
                 LocalDate fechaAlta = LocalDate.parse(campos[10], formatter);
                 LocalTime hour = LocalTime.parse(campos[4],formatter1);
                 DTRutaDeVuelo drv = new DTRutaDeVuelo(campos[2],campos[13],campos[3],hour,Integer.parseInt(campos[5]),Integer.parseInt(campos[6]),Integer.parseInt(campos[7]), fechaAlta);
                 TuplaDatosRutaDeVuelo second = new TuplaDatosRutaDeVuelo(campos[1],campos[8].trim(),campos[9].trim(),lista_cats,drv);
                 rutasDeVuelo.put(campos[0], second);
                 
                 estImAgregar = Arrays.asList(campos[12],campos[14]);
                 estadoImagen.put(campos[0], estImAgregar);
             }catch(DateTimeParseException e7) {
             	e7.printStackTrace();
             }
         }


      } catch (IOException e10) {
          System.err.println("Error leyendo el archivo de ruas: " + archivoRutas);
          e10.printStackTrace();
      }

                  for (Map.Entry<String, TuplaDatosRutaDeVuelo> entry : rutasDeVuelo.entrySet()) {
                	  TuplaDatosRutaDeVuelo data = entry.getValue();
                	  List<String> aArr = new ArrayList<String>();
                	  
                	  String rutaActual = entry.getKey();
                	  List<String> estima = estadoImagen.get(rutaActual);
                	  
                	  
              	for(String c :data.getRefCategorias() ) {
              			String ca = categorias.get(c.trim()).getNombre();
                  		aArr.add(ca);
              		
              		
              	}
              	String[] catego = aArr.toArray(new String[0]);
              	try{
              		iCRV.NuevoRutaDeVuelo(usuarios.get(data.getRefAerolinea()).getNick() , data.getDtRutaDeVuelo().getNombre(), data.getDtRutaDeVuelo().getDescripcionCorta(), data.getDtRutaDeVuelo().getDescripcion(), data.getDtRutaDeVuelo().getHora(), data.getDtRutaDeVuelo().getCostoTurista(), data.getDtRutaDeVuelo().getCostoEjecutivo(), data.getDtRutaDeVuelo().getCostoEExtra(),ciudades.get(data.getRefOrigen().trim()).getNombre() , ciudades.get(data.getRefDestino().trim()).getNombre().trim(),data.getDtRutaDeVuelo().getFechaAlta() ,catego);
              		System.out.println("la ruta es " + estima.get(0));
              		if (estima.get(0).equalsIgnoreCase("Confirmada")) {
              			iCRV.cambiarEstadoRuta(data.getDtRutaDeVuelo().getNombre(), 0);
              	  }
              		else if (estima.get(0).equalsIgnoreCase("Rechazada")) {
              			iCRV.cambiarEstadoRuta(data.getDtRutaDeVuelo().getNombre(), 2);
              	  }
              		else {System.out.println("la ruta la pongo como ingresada");}
              		if (!estima.get(1).equalsIgnoreCase("(Sin Imagen)")) {
              			iCRV.setImagen(data.getDtRutaDeVuelo().getNombre(), estima.get(1));
              			
              		}
              		else {}
//              		iCRV.setImagen(data.getDtRutaDeVuelo().getNombre(), archivoRutas);
//              		iCRV.cambiarEstadoRuta(data.getDtRutaDeVuelo().getNombre(), 0);
              	}catch(DatosInvalidosException ex){
              		ex.printStackTrace();
              	}
                 catch(VueloYaExisteException ex){
                		ex.printStackTrace();
                		
                			
                		}
                 }
  }
  
  private void cargarVuelos() {
	    String archivoVuelos = rutaBase + "2024Vuelos.csv";
	    Map<String, List<String>> estadoImagen = new HashMap<>();
	    
	    try (BufferedReader br = new BufferedReader(new FileReader(archivoVuelos))) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        String linea;
	        
	        while ((linea = br.readLine()) != null) {
	            String[] campos = linea.split(separador);
	            try {
	                // Verificar que el usuario exista en el mapa
	                DTUser usuario = usuarios.get(campos[1]);
	                if (usuario == null) {
	                    System.err.println("Usuario con ID " + campos[1] + " no encontrado.");
	                    continue; // Saltar esta línea si el usuario no existe
	                }
	                
	                String aero = usuario.getNick();
	                
	                // Verificar si la ruta de vuelo existe
	                DTRutaDeVuelo rutaDeVuelo = rutasDeVuelo.get(campos[2]).getDtRutaDeVuelo();
	                if (rutaDeVuelo == null) {
	                    System.err.println("Ruta de vuelo con ID " + campos[2] + " no encontrada.");
	                    continue; // Saltar esta línea si la ruta no existe
	                }
	                
	                String ruta = rutaDeVuelo.getNombre();
	                LocalDate fecha = LocalDate.parse(campos[4], formatter);
	                LocalDate fechaAlta = LocalDate.parse(campos[8], formatter);
	                String[] partes = campos[5].split(":");
	                int horas = Integer.parseInt(partes[0]);
	                int minutos = Integer.parseInt(partes[1]);
	                int duracion = (horas * 60) + minutos;
	                
	                // Crear el objeto DTVuelo
	                DTVuelo dtvuelo = new DTVuelo(campos[3], fecha, fechaAlta, duracion, 
	                                              Integer.parseInt(campos[6]), Integer.parseInt(campos[7]), null, null,null);
	                TuplaDatosVuelo tdv = new TuplaDatosVuelo(aero, ruta, dtvuelo);
	                vuelos.put(campos[0], tdv);
	                
	                // Guardar la imagen en el mapa
	                estadoImagen.put(campos[0], Arrays.asList(campos[9]));
	                
	            } catch (DateTimeParseException e7) {
	                e7.printStackTrace();
	            }
	        }

	    } catch (IOException e10) {
	        System.err.println("Error leyendo el archivo de vuelos: " + archivoVuelos);
	        e10.printStackTrace();
	    }
	    
	    // Procesar los vuelos y asignar la imagen correspondiente
	    for (Map.Entry<String, TuplaDatosVuelo> entry : vuelos.entrySet()) {
	        TuplaDatosVuelo tuplaDatosVuelo = entry.getValue();
	        
	        try {
	            System.out.println("Se va a crear el vuelo de nombre : " + tuplaDatosVuelo.getDtVuelo().getNombre());
	            
	            // Crear el vuelo usando el controlador
	            iCRV.ingresarDatos(tuplaDatosVuelo.getRefAerolínea(), tuplaDatosVuelo.getRefRuta(), 
	                               tuplaDatosVuelo.getDtVuelo().getFecha(), tuplaDatosVuelo.getDtVuelo().getFechaAlta(),
	                               tuplaDatosVuelo.getDtVuelo().getNombre(), tuplaDatosVuelo.getDtVuelo().getAsientosTurista(),
	                               tuplaDatosVuelo.getDtVuelo().getAsientosEjecutivo(), tuplaDatosVuelo.getDtVuelo().getDuracion());
	            
	            // Obtener el vuelo recién creado y asignar la imagen
	            Vuelo v = iCRV.obtenerVuelo(tuplaDatosVuelo.getDtVuelo().getNombre());
	            v.setImagen(estadoImagen.get(entry.getKey()).get(0)); 
	            System.out.println("la imagen se guarda como: "+estadoImagen.get(entry.getKey()).get(0));// Asignar la imagen del vuelo
	            
	        } catch (VueloYaExisteException e1) {
	            e1.printStackTrace();
	        } catch (DatosInvalidosException ex) {
	            ex.printStackTrace();
	        }
	    }
	}




 private Map<String, TuplaDatosReserva> cargarReservas() {
	 reservas = new HashMap<String,TuplaDatosReserva>();
	 String archivoReservas = rutaBase + "2024Reservas.csv";
     try (BufferedReader br = new BufferedReader(new FileReader(archivoReservas))) {
       	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
       	 String p = br.readLine();
           while ((linea = br.readLine()) != null) {
               String[] campos = linea.split(separador);
               try {                                    
                   String aerolinea = usuarios.get(campos[1]).getNick();
                   String ruta = rutasDeVuelo.get(campos[2]).getDtRutaDeVuelo().getNombre();
                   String vuelo = vuelos.get(campos[3]).getDtVuelo().getNombre();
                   String cliente = (campos[4]);
                   LocalDate fecha = LocalDate.parse(campos[8], formatter);

                   DTReserva dtreserva = new DTReserva(Integer.parseInt(campos[7]),Integer.parseInt(campos[9]),campos[5],fecha,null);
                   int cantpasajes = Integer.parseInt(campos[6]);
                   TuplaDatosReserva tdr = new TuplaDatosReserva(aerolinea,ruta,vuelo,cliente,cantpasajes,dtreserva);
                   reservas.put(campos[0], tdr);
                   
               }catch(DateTimeParseException e7) {
               	e7.printStackTrace();
               }
           }
           
           
       } catch (IOException e10) {
           System.err.println("Error leyendo el archivo de reservas: " + archivoReservas);
           e10.printStackTrace();
       }
     return reservas;
 }
 private void cargarPasajes() {
	 reservas = cargarReservas();
	 String archivoPasajes = rutaBase + "2024Pasajes.csv";
     Map<String,DatosPasaje> pasajes = new HashMap<>();
     try(BufferedReader br = new BufferedReader(new FileReader(archivoPasajes))){
     	String llll = br.readLine();
     	while((linea = br.readLine()) != null) {
     		String[] campos = linea.split(separador);
     		DatosPasaje dp = new DatosPasaje(campos[1],campos[2],campos[3]);
     		pasajes.put(campos[0],dp);
     		
     	}
     }catch(IOException e10) {
     	System.err.println("Error leyendo el archivo de pasajes: "+archivoPasajes);
     	e10.printStackTrace();
     }
       for(Map.Entry<String, TuplaDatosReserva> entry : reservas.entrySet()) {
     	  TuplaDatosReserva data = entry.getValue();
     	  if(data.getcantPasajeros()==1) {
     		  
     	  try {
				iCRV.ingresarDatosVuelo(usuarios.get(data.getRefCliente()).getNick(),data.getDtReservas().getTipoAsiento(),data.getDtReservas().getEquipajeExtra(), data.getDtReservas().getFecha(),data.getRefVuelo());
				System.out.println("se creo la reserva del vuelo " +data.getRefVuelo());
			} catch (VueloNoExisteException | UsuarioNoExisteException|DatosInvalidosException | AsientosInsuficientesException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
     		  
     	  }else if(data.getcantPasajeros()>1) {
     		  
     		  Set<String[]> pasajess = new HashSet<>();
     		  for(Map.Entry<String, DatosPasaje> entrada:pasajes.entrySet()) {
     			  DatosPasaje p_i = entrada.getValue();
     			  if(p_i.getReserva().equalsIgnoreCase(entry.getKey()) && !usuarios.get(data.getRefCliente()).getName().equalsIgnoreCase(p_i.getNombre())) {
     				  String name =p_i.getNombre();
     				  String surname = p_i.getApellido();
     				  String[] pas_i = new String[2];
     				  pas_i[0]=name;
     				  pas_i[1]=surname;
     				  pasajess.add(pas_i);
     				  System.out.println("Se puso en el map el pasaje con nombre :" +pas_i[0]+ " y apellido :"+pas_i[1] );

     			  }
     		  }
     		  

     		  try {
					iCRV.ingresarNombres(usuarios.get(data.getRefCliente()).getNick(), data.getDtReservas().getTipoAsiento(),data.getDtReservas().getEquipajeExtra() , data.getDtReservas().getFecha(), data.getRefVuelo(),data.getcantPasajeros(), pasajess);
					
					System.out.println("Se hizo la reserva del vuelo " + data.getRefVuelo());
					
     		  } catch (VueloNoExisteException | UsuarioNoExisteException | DatosInvalidosException | AsientosInsuficientesException  e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
     	  }
       }
 }
}

