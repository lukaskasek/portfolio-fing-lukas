package test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import excepciones.AsientosInsuficientesException;
import excepciones.DatosInvalidosException;
import excepciones.PaqueteNoExisteException;
import excepciones.RutaNoExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VueloNoExisteException;
import excepciones.VueloYaExisteException;
import logica.Aerolinea;
import logica.DTAerolinea;
import logica.DTCliente;
import logica.DTPasaje;
import logica.DTReserva;
import logica.DTRutaDeVuelo;
import logica.DTUser;
import logica.DTVuelo;
import logica.Fabrica;
import logica.IControladorUsuarios;
import logica.IControladorRutasDeVuelo;

public class ControladorUsuarioTest {

    private static IControladorUsuarios icu;
    private static IControladorRutasDeVuelo icrv;

    @BeforeAll
    public static void iniciar() {
        Fabrica fabrica = Fabrica.getInstancia();
        icu = fabrica.getIControladorUsuarios();
        icrv = fabrica.getIControladorRutasDeVuelo();
    }

    @Test
    void testAltaDeUsuarioOK() {
    	String nick = "mati";
		String nombre = "Matias";
		String email = "mati.esca@gmail.com";
		String apellido = "Escalante";
		LocalDate fecha =LocalDate.of(2003, 11, 17);
		String nacionalidad = "Uruguay";
		String tipoDoc = "Cedula";
		String numDoc = "54401357";
        String contra = "password";
        String foto = "ruta/a/la/foto.jpg"; 
        try {
			icu.NuevoCliente(nick, nombre, apellido, email, fecha, nacionalidad, tipoDoc, numDoc,contra,foto);
			DTUser du = icu.mostrarDatosUsuario(nick);
			
			assertEquals(du.getNick(),nick);
			assertEquals(du.getName(),nombre);
			assertEquals(du.getCorreo(),email);
			DTCliente dc = (DTCliente) du;
			assertEquals(dc.getApellido(),apellido);
			assertEquals(dc.getFechaNac(),fecha);
			assertEquals(dc.getNacion(),nacionalidad);
			assertEquals(dc.getTDocumento(),tipoDoc);
			assertEquals(dc.getNDocumento(),numDoc);
			assertEquals(dc.getContra(),contra);
		

		}catch(UsuarioYaExisteException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (DatosInvalidosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ahora lo mismo pero con aerolinea
		String nicka = "aerol";
		String nombrea = "AerolineasUruguayas";
		String emaila = "aero@gmail.com";
		String descripciona = "es una aerolinea";
		String link = "www.aerolineas.com";
		String contras= "contrasena";
		String fot= "mifoto.png";
		try {
			icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contras,fot);
			DTUser dua = icu.mostrarDatosUsuario(nicka);
			assertEquals(dua.getNick(),nicka);
			assertEquals(dua.getName(),nombrea);
			assertEquals(dua.getCorreo(),emaila);
			DTAerolinea da = (DTAerolinea) dua;
			
			assertEquals(da.getDescr(),descripciona);
			assertEquals(da.getLink(),link);
			assertEquals(da.getContra(),contras);
			
		}catch(UsuarioYaExisteException e) {
			fail(e.getMessage());
			e.getStackTrace();
			
		} catch (DatosInvalidosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

@Test
void testUsuarioRepetido() {
	String nick = "matias";
	String nombre = "Matias";
	String email = "matias.esca@gmail.com";
	String apellido = "Escalante";
	LocalDate fecha =LocalDate.of(2003, 11, 17);
	String nacionalidad = "Uruguay";
	String tipoDoc = "Cedula";
	String numDoc = "54401357";
    String contra = "password";
    String foto = "ruta/a/la/foto.jpg"; 
	try {
		icu.NuevoCliente(nick,nombre,apellido,email,fecha,nacionalidad,tipoDoc,numDoc,contra,foto);
		
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.printStackTrace();
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	assertThrows(UsuarioYaExisteException.class, ()->{
		icu.NuevoCliente(nick, nombre, apellido, email, fecha, nacionalidad, tipoDoc, numDoc,contra,foto);
	});
	
	//ahora con aerolinea con el mismo nick del cliente
	assertThrows(UsuarioYaExisteException.class,()-> icu.NuevaAerolinea("matias", "Matias",  "mati7.esca@gmail.com",   "descripcion", "www.yahoo.com", "asasa", "fotoo"))
	;
	//ahora probando con el mismo mail que el cliente
	assertThrows(UsuarioYaExisteException.class,()-> icu.NuevaAerolinea("usuario1", "Matias",  "matias.esca@gmail.com",   "Cedula", "54401357","sasa","ft"))
	;
}
@Test
void TestMostrarUsuarios() {
	// primero prueba la exception de que no haya ningun usuario
	
		
	
	String nick1 = "user1",nick2 = "user2",nick3 = "user3";
	String nombre1 = "Juan";
	String a1 = "Rodriguez";
	String e1 = "matias.escalante1703@gmail.com",e2 = "rf@gmail.com",e3 = "feef@gmail.com";
	LocalDate f1 = LocalDate.of(1988,11,15);
	String n1 = "Uruguaya";
	String td= "Cedula";
	String nd = "12345678";
	String c1="clave";
	String f="foto";
	
	try {
		icu.NuevoCliente(nick1, nombre1, a1, e1, f1, n1, td, nd,c1,f);
		icu.NuevoCliente(nick2, nombre1, a1, e2 ,f1, n1, td, nd,c1,f);
		icu.NuevoCliente(nick3, nombre1, a1, e3 ,f1, n1, td, nd,c1,f);

	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.printStackTrace();
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		List<String> users = icu.listarUsuarios();
		assertTrue(users.contains(nick1));
		assertTrue(users.contains(nick2));
		assertTrue(users.contains(nick3));
	}catch(UsuarioNoExisteException e) {
		fail(e.getMessage());
		e.printStackTrace();
	}
	
	
}
@Test
void testAltaClienteNombreInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteNickInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteFechaInvalida() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", null, "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteApellidoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteEmailInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteNacionalidadInvalida() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteTipoDocInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteNumDocInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "","contra","foto");
    });
}

@Test
void testAltaClienteContraInvalida() {
	assertThrows(DatosInvalidosException.class, () -> {
		icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc", "", "foto");
	});
}

@Test
void testAltaClienteNombreFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteApellidoFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido123", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteEmailFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteNacionalidadFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad123", "tipoDoc", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteTipoDocFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc123", "numDoc","contra","foto");
    });
}

@Test
void testAltaClienteNumDocFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevoCliente("nombre", "nick", "apellido", "email@example.com", LocalDate.of(2000, 1, 1), "nacionalidad", "tipoDoc", "numDoc*","contra","foto");
    });
}

@Test
void testAltaAerolineaNickInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevaAerolinea("", "Aerolinea", "email@example.com", "descripcion", "link","contra","foto");
    });
}

@Test
void testAltaAerolineaNombreInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevaAerolinea("nick", "", "email@example.com", "descripcion", "link","contra","foto");
    });
}

@Test
void testAltaAerolineaEmailInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevaAerolinea("nick", "Aerolinea", "", "descripcion", "link","contra","foto");
    });
}

@Test
void testAltaAerolineaDescripcionInvalida() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevaAerolinea("nick", "Aerolinea", "email@example.com", "", "link","contra","foto");
    });
}

@Test
void testAltaAerolineaContraInvalida() {
	assertThrows(DatosInvalidosException.class, () -> {
		icu.NuevaAerolinea("nick", "Aerolinea", "email@example.com","descripcion","link","", "foto");
	});
}

@Test
void testAltaAerolineaNombreFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevaAerolinea("nick", "*34", "email@example.com", "descripcion", "link","contra","foto");
    });
}

@Test
void testAltaAerolineaEmailFormatoInvalido() {
    assertThrows(DatosInvalidosException.class, () -> {
        icu.NuevaAerolinea("nick", "Aerolinea", "email@invalid", "descripcion", "link","contra","foto");
    });
}

@Test
void MostrarRutasReservasYPaquetesVacios() {
	//primero creo un usuario sin rutas ni reservas ni paquetes
	String nicka = "aerolaaaaa";
	String nombrea = "AerolineasUruguayas";
	String emaila = "aeroaaaa@gmail.com";
	String descripciona = "es una aerolinea";
	String link = "www.aerolineas.com";
	String contra=	"contraseña";
	String foto= "mifoto.jpg";
	
	try {
		icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contra,foto);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
		
		assertEquals(da.getDescr(),descripciona);
		assertEquals(da.getLink(),link);
		assertEquals(da.getContra(),contra);
		
		
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
		
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//pruebo mostrar rutas sin rutas asociadas
	assertThrows(RutaNoExisteException.class,()->{
		icu.mostrarNombreRutasDeVuelo(nicka);
	});
	//ahora mostrar Reservas de un cliente sin reservas
	String nick = "matiiii";
	String nombre = "Matias";
	String email = "matiiiii.esca@gmail.com";
	String apellido = "Escalante";
	LocalDate fecha =LocalDate.of(2003, 11, 17);
	String nacionalidad = "Uruguay";
	String tipoDoc = "Cedula";
	String numDoc = "54401357";
	String contras= "micontra";
	String fotoo= "miFoto";
	
	try {
		icu.NuevoCliente(nick, nombre, apellido, email, fecha, nacionalidad, tipoDoc, numDoc,contras,fotoo);
		DTUser du = icu.mostrarDatosUsuario(nick);
		
		assertEquals(du.getNick(),nick);
		assertEquals(du.getName(),nombre);
		assertEquals(du.getCorreo(),email);
		DTCliente dc = (DTCliente) du;
		assertEquals(dc.getApellido(),apellido);
		assertEquals(dc.getFechaNac(),fecha);
		assertEquals(dc.getNacion(),nacionalidad);
		assertEquals(dc.getTDocumento(),tipoDoc);
		assertEquals(dc.getNDocumento(),numDoc);
		assertEquals(dc.getContra(),contras);

	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.printStackTrace();
	} catch (DatosInvalidosException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	};
	
	assertThrows(VueloNoExisteException.class, ()->{
		icu.mostrarReservasVuelos(nick);
	});
	
	assertThrows(PaqueteNoExisteException.class,()->{
		icu.mostrarPaquetes(nick);
	});
};

@Test 
void mostrarRutasDeVueloTest() {
	//primero creo una aerolinea
	String nicka = "aerolaaaaaattaa";
	String nombrea = "AerolinsUruguayas";
	String emaila = "aeroaaaattaaa@gmail.com";
	String descripciona = "es una aerolinea";
	String link = "www.aerolineas.com";
	String contra= "contraseña";
	String foto= "foto";
	try {
		icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contra,foto);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
		
		assertEquals(da.getDescr(),descripciona);
		assertEquals(da.getLink(),link);
		assertEquals(da.getContra(),contra);
		
		
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
		
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//ahora creo una ruta asociada a esta aerolinea
	//para eso necesito crear dos ciudades y al menos una categoria
	 String nombre = "Melooo";
        String pais = "Uruguay";
        String aeropuerto = "Aeropuerto de Carrasco";
        String descripcion = "Capital de Uruguay";
        String website = "https://montevideo.gub.uy/";
        LocalDate fechaAlta = LocalDate.of(2021, 5, 1);
        try {
            icrv.altaCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
            String[] ciudades = icrv.listarCiudades();
            List<String> ciudadesList = Arrays.asList(ciudades);
            //debe buscar nombre en ciudades de tipo String[]
            assertTrue(ciudadesList.contains(nombre));
        } catch (Exception e) {
            fail(e.getMessage());
        } 
        String nombree = "BuenosAiresss";
        String paiss = "Argentina";
        String aeropuertoo = "Aeropuerto de Conchillas";
        String descripcionn = "Capital de Argentina";
        String websitee = "https://argentina.gub.uy/";
        LocalDate fechaAltaa = LocalDate.of(2021, 5, 2);
        try {
            icrv.altaCiudad(nombree, paiss, aeropuertoo, descripcionn, websitee, fechaAltaa);
            String[] ciudades = icrv.listarCiudades();
            List<String> ciudadesList = Arrays.asList(ciudades);
            //debe buscar nombre en ciudades de tipo String[]
            assertTrue(ciudadesList.contains(nombre));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        //creo una categoria
        try {
            icrv.altaCategoria(nombre);
            String[] categorias = icrv.listarCategorias();
            List<String> categoriasList = Arrays.asList(categorias);
            assertTrue(categoriasList.contains(nombre));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        //ahora si creo la ruta de vuelo y chequeo la funcion listar viendo si esta contenida o no 
        String[]cats = {nombre};
        assertThrows(DatosInvalidosException.class, () -> {
        	icrv.NuevoRutaDeVuelo("ssss","Rutaa","ruta de testing ","descripcioncorta",LocalTime.of(12, 30), 200,300,50,nombre,nombree, LocalDate.of(2024, 8, 2),cats );
	    });
        try {
            icrv.NuevoRutaDeVuelo(nicka,"Rutaaa","ruta de testing ","desccorta",LocalTime.of(12, 30), 200,300,50,nombre,nombree, LocalDate.of(2024, 8, 2),cats );
            List<String> rutas = icu.mostrarNombreRutasDeVuelo(nicka);
            assertTrue(rutas.contains("Rutaaa"));
            DTRutaDeVuelo datar = icrv.obtenerInfoRuta("Rutaaa");
            assertEquals(datar.getCostoEExtra(),50);
            assertEquals(datar.getCostoEjecutivo(),300);
            assertEquals(datar.getCostoTurista(),200);
            assertEquals(datar.getDescripcion(),"ruta de testing ");
            assertEquals(datar.getFechaAlta(),LocalDate.of(2024, 8, 2));
            assertEquals(datar.getNombre(),"Rutaaa");
            assertEquals(datar.getHora(),LocalTime.of(12, 30));
        } catch (VueloYaExisteException e) {
            fail(e.getMessage());							
        }catch (DatosInvalidosException e1) {
        	fail(e1.getMessage());
        }catch (RutaNoExisteException e2) {
        	fail(e2.getMessage());
        }
        String nombrev = "vuelo";
        LocalDate f = LocalDate.of(2024, 12, 12);
        LocalDate fa = LocalDate.of(2024, 12, 12);
        int at = 1;
        int ae = 2;
        int dur = 800;
        try {
        	icrv.ingresarDatos(nicka, "Rutaa", f, fa, nombrev, at, ae, dur);
        	List<String> vuelos;
        	DTVuelo data;
        	try {
        		vuelos = icrv.listarVuelos("Rutaa");
        		assertTrue(vuelos.contains(nombrev));
        		
        	}catch(VueloNoExisteException e){
        		e.printStackTrace();
        	}
        	try {
        		data = icrv.mostrarDatosDeVuelo(nombrev);
        		assertEquals(data.getAsientosEjecutivo(),ae);
        		assertEquals(data.getAsientosTurista(),at);
        		assertEquals(data.getDuracion(),dur);
        		assertEquals(data.getFecha(),f);
        		assertEquals(data.getFechaAlta(),fa);
        		assertEquals(data.getNombre(),nombrev);
        		DatosInvalidosException thrownException = assertThrows(DatosInvalidosException.class, () -> {
                    icrv.ingresarDatos(nicka, "Rutaa", f, fa, nombrev, at, ae, dur);
                });
                assertEquals("El vuelo con el nombre ingresado ya existe", thrownException.getMessage());
        	}catch(VueloNoExisteException e) {
        		e.printStackTrace();
        	}
        	
        }catch(DatosInvalidosException e) {
        	e.printStackTrace();
        }catch(VueloYaExisteException e) {
        	e.printStackTrace();
        } 
        //fallas en ingresarDatosVuelo
        assertThrows(DatosInvalidosException.class, () -> {
	        icrv.ingresarDatosVuelo("matiiii","Turista", -1,f, nombrev);
	    });
        assertThrows(DatosInvalidosException.class, () -> {
	        icrv.ingresarDatosVuelo("matiiii","Turista", 3,null, nombrev);
	    });
        assertThrows(DatosInvalidosException.class, () -> {
	        icrv.ingresarDatosVuelo("matiiii","fasfafa", 4,f, nombrev);
	    });
        assertThrows(VueloNoExisteException.class, () -> {
	        icrv.ingresarDatosVuelo("matiiii","Turista", 4,f, "asdasfasf");
	    });
        try {
    	     icrv.ingresarDatosVuelo("matiiii", "Turista", 3,f, nombrev);
    	     List<String> reservas = icrv.listarReservas(nombrev);
    	        assertNotNull(reservas);  // Aseguro que la lista de reservas no es null
    	        //assertTrue(reservas.contains("matiiii"));  
        }catch( VueloNoExisteException e) {
        	e.printStackTrace();
        }catch(DatosInvalidosException e) {
        	e.printStackTrace();
        }catch(UsuarioNoExisteException e) {
        	e.printStackTrace();
        }catch(AsientosInsuficientesException e) {
        	e.printStackTrace();
        }
}

@Test
void testAerolíneaVacía() {
    String aerolinea = ""; // Aerolínea inválida
    String rutaVuelo = "VueloaMontevideo";
    LocalDate fecha = LocalDate.of(2024, 12, 12);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreVuelo = "vueloNuevo";
    int asientosTurista = 10;
    int asientosEjecutivos = 5;
    int duracion = 180;

    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
}
@Test
void testRutaVacía() {
    String aerolinea = "aero";
    String rutaVuelo = ""; // Ruta inválida
    LocalDate fecha = LocalDate.of(2024, 12, 12);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreVuelo = "vueloNuevo";
    int asientosTurista = 10;
    int asientosEjecutivos = 5;
    int duracion = 180;

    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
}
@Test
void testAsientosInvalidos() {
    String aerolinea = "aero";
    String rutaVuelo = "VueloaMontevideo";
    LocalDate fecha = LocalDate.of(2024, 12, 12);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreVuelo = "vueloNuevo";
    int asientosTurista = 0;  // Número de asientos inválido
    int asientosEjecutivos = 0;  // Número de asientos inválido
    int duracion = 180;

    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, nombreVuelo, 3, -1, duracion);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, nombreVuelo, -1, 3, duracion);
    });
}
@Test
void testFechasYNombreInvalidas() {
    String aerolinea = "aero";
    String rutaVuelo = "VueloaMontevideo";
    LocalDate fecha = LocalDate.of(2024, 12, 12);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreVuelo = "vueloNuevo";
    int asientosTurista = 1;  // Número de asientos inválido
    int asientosEjecutivos = 0;  // Número de asientos inválido
    int duracion = 180;

    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, null, fechaAlta, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, null, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, "", asientosTurista, asientosEjecutivos, duracion);
    });
}
@Test
void testDuracionInvalida() {
    String aerolinea = "aero";
    String rutaVuelo = "VueloaMontevideo";
    LocalDate fecha = LocalDate.of(2024, 12, 12);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreVuelo = "vueloNuevo";
    int asientosTurista = 10;
    int asientosEjecutivos = 5;
    int duracion = 0; // Duración inválida

    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
}
@Test
void testAerolíneaNoExiste() {
    String aerolinea = "aeroInexistente"; // Aerolínea no registrada
    String rutaVuelo = "VueloaMontevideo";
    LocalDate fecha = LocalDate.of(2024, 12, 12);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreVuelo = "vueloNuevo";
    int asientosTurista = 10;
    int asientosEjecutivos = 5;
    int duracion = 180;

    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos("jajaja", "Rutaa", fecha, fechaAlta, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
}
@Test
void testRutaVueloNoExiste() {
    String aerolinea = "aero";
    String rutaVuelo = "rutaInexistente"; // Ruta de vuelo no registrada
    LocalDate fecha = LocalDate.of(2024, 12, 12);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreVuelo = "vueloNuevo";
    int asientosTurista = 10;
    int asientosEjecutivos = 5;
    int duracion = 180;

    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarDatos(aerolinea, rutaVuelo, fecha, fechaAlta, nombreVuelo, asientosTurista, asientosEjecutivos, duracion);
    });
}
@Test
void mostrarVuelosReservadosTest() {
	//primero creo un cliente 
	String nick = "matiiiio";
	String nombrec = "Matias";
	String email = "matiiiiio.esca@gmail.com";
	String apellido = "Escalante";
	LocalDate fecha =LocalDate.of(2027, 11, 17);
	String nacionalidad = "Uruguay";
	String tipoDoc = "Cedula";
	String numDoc = "54401357";
	String contra="contra";
	String foto="ft";
	
	try {
		icu.NuevoCliente(nick, nombrec, apellido, email, fecha, nacionalidad, tipoDoc, numDoc,contra,foto);
		DTUser du = icu.mostrarDatosUsuario(nick);
		
		assertEquals(du.getNick(),nick);
		assertEquals(du.getName(),nombrec);
		assertEquals(du.getCorreo(),email);
		DTCliente dc = (DTCliente) du;
		assertEquals(dc.getApellido(),apellido);
		assertEquals(dc.getFechaNac(),fecha);
		assertEquals(dc.getNacion(),nacionalidad);
		assertEquals(dc.getTDocumento(),tipoDoc);
		assertEquals(dc.getNDocumento(),numDoc);
		assertEquals(dc.getContra(),contra);
	

	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.printStackTrace();
		//kvmdkvbfbfnhh
	} catch (DatosInvalidosException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	};
	//para crear un vuelo necesito aerolinea y ruta 
	String nicka = "aerolaaaaaaa";
	String nombrea = "AerolineasUruguayas";
	String emaila = "aeroaaaaaa@gmail.com";
	String descripciona = "es una aerolinea";
	String link = "www.aerolineas.com";
	String contraseña="contras";
	String fotoo="foto";
	try {
		icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contraseña,foto);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
		
		assertEquals(da.getDescr(),descripciona);
		assertEquals(da.getLink(),link);
		assertEquals(da.getContra(),contraseña);
		
		
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
		
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//ahora creo una ruta asociada a esta aerolinea
	//para eso necesito crear dos ciudades y al menos una categoria
	 String nombre = "Meloo";
        String pais = "Uruguay";
        String aeropuerto = "Aeropuerto de Carrasco";
        String descripcion = "Capital de Uruguay";
        String website = "https://montevideo.gub.uy/";
        LocalDate fechaAlta = LocalDate.of(2021, 5, 1);
        try {
            icrv.altaCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
            String[] ciudades = icrv.listarCiudades();
            List<String> ciudadesList = Arrays.asList(ciudades);
            //debe buscar nombre en ciudades de tipo String[]
            assertTrue(ciudadesList.contains(nombre));
        } catch (Exception e) {
            fail(e.getMessage());
        } 
        String nombree = "BuenosAiress";
        String paiss = "Argentina";
        String aeropuertoo = "Aeropuerto de Conchillas";
        String descripcionn = "Capital de Argentina";
        String websitee = "https://argentina.gub.uy/";
        LocalDate fechaAltaa = LocalDate.of(2021, 5, 2);
        try {
            icrv.altaCiudad(nombree, paiss, aeropuertoo, descripcionn, websitee, fechaAltaa);
            String[] ciudades = icrv.listarCiudades();
            List<String> ciudadesList = Arrays.asList(ciudades);
            //debe buscar nombre en ciudades de tipo String[]
            assertTrue(ciudadesList.contains(nombre));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        //creo una categoria
        try {
            icrv.altaCategoria(nombre);
            String[] categorias = icrv.listarCategorias();
            List<String> categoriasList = Arrays.asList(categorias);
            assertTrue(categoriasList.contains(nombre));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        //ahora si creo la ruta de vuelo y chequeo la funcion listar viendo si esta contenida o no 
        String[]cats = {nombre};
        try {
            icrv.NuevoRutaDeVuelo(nicka,"Rutaa","ruta de testing ","confirmada",LocalTime.of(12, 30), 200,300,50,nombre,nombree, LocalDate.of(2024, 8, 2),cats );
            List<String> rutas = icu.mostrarNombreRutasDeVuelo(nicka);
            assertTrue(rutas.contains("Rutaa"));
            String aerolineaObtenida = icrv.getAerolinea("Rutaa");
            assertEquals(nicka, aerolineaObtenida);
        } catch (Exception e) {
            fail(e.getMessage());
        }
	
	try {
		icrv.ingresarDatos("aerolaaaaaaa", "Rutaa", LocalDate.of(2024, 8, 2), LocalDate.of(2023, 5, 2),"vuelotest", 10, 15, 100);
	}catch(VueloYaExisteException e) {
		e.printStackTrace();
	}catch(DatosInvalidosException ee) {
		ee.printStackTrace();
	}

	//ahora hay que realizar la reserva 
	try {
		icrv.ingresarDatosVuelo(nick,"Turista", 2, fecha, "vuelotest");
	} catch (VueloNoExisteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UsuarioNoExisteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch(AsientosInsuficientesException e) {
		e.printStackTrace();
	}
	try {
		List<String> reservas = icu.mostrarReservasVuelos(nick);
		assertTrue(reservas.contains("vuelotest"));
	} catch (VueloNoExisteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	boolean reservaExistente = icrv.chequearReserva(nick, "vuelotest");
    assertTrue(reservaExistente);  // Asegurarse de que la reserva fue encontrada
    
    boolean reservaInexistente = icrv.chequearReserva(nick, "vueloinexistente");
    assertFalse(reservaInexistente);  // Asegurarse de que un vuelo inexistente no da como resultado una reserva encontrada
	

}
@Test
void editarDatosClienteTest() {
	//primero creamos un cliente
	String nick = "matiiiioa";
	String nombrec = "Matias";
	String email = "matiiiiioa.esca@gmail.com";
	String apellido = "Escalante";
	LocalDate fecha =LocalDate.of(2026, 11, 17);
	String nacionalidad = "Uruguay";
	String tipoDoc = "Cedula";
	String numDoc = "54401357";
	String contra= "contraseña";
	String foto= "foto";
	
	try {
		icu.NuevoCliente(nick, nombrec, apellido, email, fecha, nacionalidad, tipoDoc, numDoc,contra,foto);
		DTUser du = icu.mostrarDatosUsuario(nick);
		
		assertEquals(du.getNick(),nick);
		assertEquals(du.getName(),nombrec);
		assertEquals(du.getCorreo(),email);
		DTCliente dc = (DTCliente) du;
		assertEquals(dc.getApellido(),apellido);
		assertEquals(dc.getFechaNac(),fecha);
		assertEquals(dc.getNacion(),nacionalidad);
		assertEquals(dc.getTDocumento(),tipoDoc);
		assertEquals(dc.getNDocumento(),numDoc);
		assertEquals(dc.getContra(),contra);

	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.printStackTrace();
		//kvmdkvbfbfnhh
	} catch (DatosInvalidosException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	};
	//le modificamos todos los atributos, eso cubre todos los casos
	icu.editarDatosCliente(nick, "Francisco", "Piloni", LocalDate.of(2003, 2, 1), "Española", "Pasaporte", "12345678");
	//ahora checkeamos, usando la op mostrar datos de usuario que los datos del usuario sean los editados
	DTUser edit = icu.mostrarDatosUsuario(nick);
	DTCliente ec = (DTCliente) edit;
	assertEquals(ec.getName(),"Francisco");
	assertEquals(ec.getApellido(),"Piloni");
	assertEquals(ec.getFechaNac(),LocalDate.of(2003, 2, 1));
	assertEquals(ec.getNacion(),"Española");
	assertEquals(ec.getTDocumento(),"Pasaporte");
	assertEquals(ec.getNDocumento(),"12345678");
	
	
	//ahora con aerolinea
	String nicka = "aerolaaaaaaae";
	String nombrea = "AerolineasUruguayas";
	String emaila = "aeroaaaaaae@gmail.com";
	String descripciona = "es una aerolinea";
	String link = "www.aerolineas.com";
	String contraseña="contra";
	String fotoo="foto";
	try {
		icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contraseña,foto);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
		
		assertEquals(da.getDescr(),descripciona);
		assertEquals(da.getLink(),link);
		assertEquals(da.getContra(),contraseña);
		
		
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
		
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//hacemos lo mismo que con cliente
	icu.editarDatosAerolinea(nicka, "aerolineaEdit", "desc edit", "linki");
	DTUser du = icu.mostrarDatosUsuario(nicka);
	DTAerolinea edita = (DTAerolinea) du;
	assertEquals(edita.getName(),"aerolineaEdit");
	assertEquals(edita.getDescr(),"desc edit");
	assertEquals(edita.getLink(),"linki");
	
}
@Test
void DatosInvalidosReserva() {
    String ruta = "Rutaa";
    String cliente = "mati";
    String tipo = "Turista";
    int equipajeExtra = 1;
    LocalDate fechaReserva = LocalDate.of(2003, 2, 1);
    int cantPasajeros = 2;

    // Inicializar el Set con un solo nombre y apellido
    Set<String[]> nombresApellidosPasajeros = new HashSet<>();
    nombresApellidosPasajeros.add(new String[]{"Juan", "Pérez"});
    
    Set<String[]> a = new HashSet<>();
    a.add(new String[] {"",""});
    
    Set<String[]> b = new HashSet<>();
    a.add(new String[] {"w",""});

    // Verificar si lanza la excepción DatosInvalidosException
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres("", tipo, equipajeExtra, fechaReserva, "vuelotest", cantPasajeros, nombresApellidosPasajeros);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, "", equipajeExtra, fechaReserva, "vuelotest", cantPasajeros, nombresApellidosPasajeros);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, tipo, -1, fechaReserva, "vuelotest", cantPasajeros, nombresApellidosPasajeros);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, tipo, equipajeExtra, fechaReserva, "vuelotest", -1, nombresApellidosPasajeros);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, tipo, equipajeExtra,null, "vuelotest", cantPasajeros, nombresApellidosPasajeros);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, tipo, equipajeExtra,fechaReserva, "", cantPasajeros, nombresApellidosPasajeros);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, "jaja", equipajeExtra,fechaReserva, "vuelotest", cantPasajeros, a);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, tipo, equipajeExtra,fechaReserva, "vuelotest", cantPasajeros, a);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, tipo, equipajeExtra,fechaReserva, "vuelotest", cantPasajeros, b);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres(cliente, tipo, equipajeExtra,fechaReserva, "jujt", cantPasajeros, nombresApellidosPasajeros);
    });
    assertThrows(DatosInvalidosException.class, () -> {
        icrv.ingresarNombres("asfasf", tipo, equipajeExtra,fechaReserva, "vuelotest", cantPasajeros, nombresApellidosPasajeros);
    });
   
    //ingresarDatosVuelo(String cliente, String tipo, int cantEqExtra, LocalDate fechaDeReserva, String vuelo) throws VueloNoExisteException, UsuarioNoExisteException, DatosInvalidosException
    assertThrows(UsuarioNoExisteException.class, () -> {
        icrv.ingresarDatosVuelo("asfasf", tipo, equipajeExtra,fechaReserva, "vuelotest");
    });
   
}
@Test
void TestErrorListar() {
	assertThrows(VueloNoExisteException.class, () -> {
		icrv.listarVuelos("safasfasfs");
	});
	
	assertThrows(RutaNoExisteException.class, () -> {
		icrv.listarRutasDeVuelo("safasfasfs");
	});
}
@Test
void testGetFecha() {
    LocalDate fecha = LocalDate.of(2024, 10, 18);
    DTReserva reserva = new DTReserva(0, 100, "Turista", fecha, null);
    assertEquals(fecha, reserva.getFecha());
}

@Test
void testGetTipoAsiento() {
    DTReserva reserva = new DTReserva(0, 100, "Turista", LocalDate.now(), null);
    assertEquals("Turista", reserva.getTipoAsiento());
}

@Test
void testGetEquipajeExtra() {
    DTReserva reserva = new DTReserva(2, 100, "Turista", LocalDate.now(), null);
    assertEquals(2, reserva.getEquipajeExtra());
}

@Test
void testGetCosto() {
    DTReserva reserva = new DTReserva(0, 150, "Turista", LocalDate.now(), null);
    assertEquals(150, reserva.getCosto());
}

@Test
void testGetPasajes() {
    List<DTPasaje> pasajes = new ArrayList<>();
    pasajes.add(new DTPasaje(null, null, 0));
    DTReserva reserva = new DTReserva(0, 100, "Turista", LocalDate.now(), pasajes);
    
    assertEquals(pasajes, reserva.getPasajes());
}
@Test
void testGetDescripcion() {
	Aerolinea aerolinea = new Aerolinea("Aerolinea Test", "test@example.com", "testNick", "Descripción inicial", "link.com", "contraseña", "foto.jpg");
    assertEquals("Descripción inicial", aerolinea.getDescripcion());
}

@Test
void testGetLink() {
	Aerolinea aerolinea = new Aerolinea("Aerolinea Test", "test@example.com", "testNick", "Descripción inicial", "link.com", "contraseña", "foto.jpg");
    assertEquals("link.com", aerolinea.getLink());
}

@Test
void testSetDescripcion() {
	Aerolinea aerolinea = new Aerolinea("Aerolinea Test", "test@example.com", "testNick", "Descripción inicial", "link.com", "contraseña", "foto.jpg");
    aerolinea.setDescripcion("Nueva descripción");
    assertEquals("Nueva descripción", aerolinea.getDescripcion());
}

@Test
void testSetLink() {
	Aerolinea aerolinea = new Aerolinea("Aerolinea Test", "test@example.com", "testNick", "Descripción inicial", "link.com", "contraseña", "foto.jpg");
    aerolinea.setLink("nuevo-link.com");
    assertEquals("nuevo-link.com", aerolinea.getLink());
}
}