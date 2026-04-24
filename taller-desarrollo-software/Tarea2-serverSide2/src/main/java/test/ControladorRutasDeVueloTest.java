package test;
import static org.junit.Assert.assertThrows;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import excepciones.VueloNoExisteException;
import excepciones.VueloYaExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.CiudadExistenteException;
import excepciones.DatosInvalidosException;
import excepciones.RutaNoExisteException;
import excepciones.AsientosInsuficientesException;
import excepciones.CategoriaExistenteException;

import java.time.LocalTime;

import logica.Aerolinea;
import logica.Categoria;
import logica.Ciudad;
import logica.DTAerolinea;
import logica.DTCiudad;
import logica.DTCliente;
import logica.DTRutaDeVuelo;
import logica.DTReserva;
import logica.DTUser;
import logica.DTVuelo;
import logica.DatosPasaje;
import logica.Fabrica;
import logica.IControladorRutasDeVuelo;
import logica.IControladorUsuarios;
import logica.RutaDeVuelo;
import logica.RutaDeVuelo.Estado;
import logica.TuplaDatosReserva;
import logica.TuplaDatosVuelo;
import logica.Vuelo;

public class ControladorRutasDeVueloTest {
	private static IControladorRutasDeVuelo icr;
    private static IControladorUsuarios icu;
	
	@BeforeAll
	public static void iniciar() {
		Fabrica fabrica = Fabrica.getInstancia();
		icr = fabrica.getIControladorRutasDeVuelo();
        icu = fabrica.getIControladorUsuarios();
	}
	
	@Test
	void altaCiudadExitosa() {
	    String nombre = "Montevideo";
	    String pais = "Uruguay";
	    String aeropuerto = "Aeropuerto de Carrasco";
	    String descripcion = "Capital de Uruguay";
	    String website = "https://montevideo.gub.uy/";
	    LocalDate fechaAlta = LocalDate.of(2021, 5, 1);
	    try {
	        icr.altaCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
	        String[] ciudades = icr.listarCiudades();
	        List<String> ciudadesList = Arrays.asList(ciudades);
	        assertTrue(ciudadesList.contains(nombre));
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}
	
	@Test
	void nuevoRutaConNombreVacio() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}
	
	@Test
	void nuevoRutaConCostoEjecutivoNegativo() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, -2, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCostoExtraCero() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 0, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCostoTuristaNegativo() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), -10, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConDescripcionVacia() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0", "descorta","", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}
	
	@Test
	void nuevoRutaConDescripcionCortaVacia() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0", "","descripcion", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCiudadOrigenVacia() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida",LocalTime.of(2, 3), 300, 200, 20, "", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCiudadDestinoVacia() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConFechaNull() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", null, ca);
	    });
	}

	@Test
	void nuevoRutaConHoraNull() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", null, 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCategoriasNull() {
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), null);
	    });
	}

	@Test
	void nuevoRutaConCiudadOrigenInexistente() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melocoton", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCiudadDestinoInexistente() {
	    String[] ca = {"ruta de prueba"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0", "descorta","decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "PiedrasBlancas", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCategoriasInvalidas() {
	    String[] ca = {"", "hola"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCategoriaNull() {
	    String[] ca = {null};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0","descorta", "decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}

	@Test
	void nuevoRutaConCategoriaInexistente() {
	    String[] ca = {"categoriaInexistente"};
	    assertThrows(DatosInvalidosException.class, () -> {
	        icr.NuevoRutaDeVuelo("aerol", "RUTA0", "descorta","decripcion valida", LocalTime.of(2, 3), 300, 200, 20, "Melo", "Montevideo", LocalDate.of(2024, 9, 2), ca);
	    });
	}
	
  @Test
  void altaCiudadConNombreMelo() {
      String nombre = "Melo";
      String pais = "Uruguay";
      String aeropuerto = "Aeropuerto de Carrasco";
      String descripcion = "Capital de Uruguay";
      String website = "https://montevideo.gub.uy/";
      LocalDate fechaAlta = LocalDate.of(2021, 5, 1);

      try {
          icr.altaCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
          String[] ciudades = icr.listarCiudades();
          List<String> ciudadesList = Arrays.asList(ciudades);
          assertTrue(ciudadesList.contains(nombre));
      } catch (Exception e) {
          fail(e.getMessage());
      }
  }

  @Test
  void altaCiudadConNombreMeloArgentina() {
      String nombre = "melo";
      String pais = "Argentina";
      String aeropuerto = "Aeropuerto de Conchillas";
      String descripcion = "Capital de Argentina";
      String website = "https://argentina.gub.uy/";
      LocalDate fechaAlta = LocalDate.of(2021, 5, 2);

      try {
          icr.altaCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);
          String[] ciudades = icr.listarCiudades();
          List<String> ciudadesList = Arrays.asList(ciudades);
          assertTrue(ciudadesList.contains(nombre));
      } catch (Exception e) {
          fail(e.getMessage());
      }
  }

  @Test
  void altaCiudadConNombreMontevideoo() {
      String nombre = "Montevideoo";
      String pais = "Uruguay";
      String aeropuerto = "Aeropuerto Internacional de Carrasco";
      String descripcion = "Capital de Uruguay";
      String website = "https://montevideo.gub.uy/";
      LocalDate fechaAlta = LocalDate.now();

      try {
          icr.altaCiudad(nombre, pais, aeropuerto, descripcion, website, fechaAlta);

          // Verificar que la ciudad se haya creado correctamente
          DTCiudad ciudad = icr.obtenerDatosCiudad(nombre, pais);
          assertNotNull(ciudad);
          assertEquals(nombre, ciudad.getNombre());
          assertEquals(pais, ciudad.getPais());
          assertEquals(aeropuerto, ciudad.getAeropuerto());
          assertEquals(descripcion, ciudad.getDescripcion());
          assertEquals(website, ciudad.getWebsite());
          assertEquals(fechaAlta, ciudad.getFechaAlta());
      } catch (CiudadExistenteException e1) {
          fail("No debería lanzar excepción para datos válidos: " + e1.getMessage());
      } catch (DatosInvalidosException e) {
          fail("No debería lanzar excepción para datos válidos: " + e.getMessage());
      }
  }


  @Test
  void testAltaCiudadDatosVacios() {
      assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCiudad("", "Uruguay", "Aeropuerto", "Descripción", "http://web.com", LocalDate.now());
      });
      assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCiudad("Ciudad", "", "Aeropuerto", "Descripción", "http://web.com", LocalDate.now());
      });
      assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCiudad("Ciudad", "País", "", "Descripción", "http://web.com", LocalDate.now());
      });
      assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCiudad("Ciudad", "País", "Aeropuerto", "", "http://web.com", LocalDate.now());
      });
      assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCiudad("Ciudad", "País", "Aeropuerto", "Descripción", "", LocalDate.now());
      });
      assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCiudad("Ciudad", "País", "Aeropuerto", "Descripción", "http://web.com", null);
      });
  }

  @Test
  void testAltaCiudadConNombreInvalido() {
      assertThrows(DatosInvalidosException.class, () -> {
          icr.altaCiudad("Ciudad123", "País", "Aeropuerto", "Descripción", "http://web.com", LocalDate.now());
      });
  }

  @Test
  void testAltaCiudadConPaisInvalido() {
      assertThrows(DatosInvalidosException.class, () -> {
          icr.altaCiudad("Ciudad", "País123", "Aeropuerto", "Descripción", "http://web.com", LocalDate.now());
      });
  }

  @Test
  void testAltaCiudadExistente() {
      String nombre = "Buenos Aires";
      String pais = "Argentina";
      try {
      	icr.altaCiudad(nombre, pais, "Aeropuerto", "Descripción", "http://web.com", LocalDate.now());
      } catch (DatosInvalidosException | CiudadExistenteException e) {
          fail("La primera alta no debería fallar: " + e.getMessage());
      }

      assertThrows(CiudadExistenteException.class, () -> {
      	icr.altaCiudad(nombre, pais, "Otro Aeropuerto", "Otra Descripción", "http://otherweb.com", LocalDate.now());
      });
  }
  @Test
  void testAltaCategoriaOK() {
      String nombre = "VueloCaro";
      try {
          icr.altaCategoria(nombre);
          assertTrue(icr.categoriaYaExiste(nombre));
      } catch (CategoriaExistenteException | DatosInvalidosException e) {
          fail(e.getMessage());
      }
  }		
  
  @Test
  void testCategoriaExistente() {
      String nombre = "nombre DeCat";
     try {
          icr.altaCategoria(nombre);
     }
     catch (CategoriaExistenteException | DatosInvalidosException e) {
  	   fail("La primera alta no debería fallar: " + e.getMessage());
  }
  	assertThrows(CategoriaExistenteException.class, () -> {
      	icr.altaCategoria(nombre);
      });
  }		
  @Test
  void testCategoriaVacia() {
      String nombre = "";

  	assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCategoria(nombre);
      });
  }	
  @Test
  void testCategoriaInvalida() {
      String nombre = "ho%ce";

  	assertThrows(DatosInvalidosException.class, () -> {
      	icr.altaCategoria(nombre);
      });
  }
  
  @Test
  void testNuevoRutaDeVueloOK() throws VueloYaExisteException {
  	String nicka = "aerolii";
	String nombrea = "AerolineasUruguayas";
	String emaila = "aerssso@gmail.com";
	String descripciona = "es una aerolinea";
	String link = "www.aerolineas.com";
	String contra="contraseña";
	String foto="foto";
	try {
		icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contra,foto);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      String aerolinea = "aerolii";
      String nombre = "VueloaMontevideo";
      String descripcion = "Vuelo a Montevideo";
      LocalTime hora = LocalTime.of(10, 0);
      LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
      String nombreo = "Melpo";
      String pais = "Uruguay";
      String aeropuerto = "Aeropuerto de Carrasco";
      String descripciono = "Capital de Uruguay";
      String website = "https://montevideo.gub.uy/";
      LocalDate fechaAltao = LocalDate.of(2021, 5, 1);
      try {
          icr.altaCiudad(nombreo, pais, aeropuerto, descripciono, website, fechaAltao);
          String[] ciudades = icr.listarCiudades();
          List<String> ciudadesList = Arrays.asList(ciudades);
          //debe buscar nombre en ciudades de tipo String[]
          assertTrue(ciudadesList.contains(nombreo));
      } catch (Exception e) {
          fail(e.getMessage());
      } 
      String nombred = "BuenosAiressss";
      String paiss = "Argentina";
      String aeropuertos = "Aeropuerto de Conchillas";
      String desccorta="corta";
      String descripciond = "Capital de Argentina";
      String websites = "https://argentina.gub.uy/";
      LocalDate fechaAltad = LocalDate.of(2021, 5, 2);
      try {
          icr.altaCiudad(nombred, paiss, aeropuertos, descripciond, websites, fechaAltad);
          String[] ciudades = icr.listarCiudades();
          List<String> ciudadesList = Arrays.asList(ciudades);
          //debe buscar nombre en ciudades de tipo String[]
          assertTrue(ciudadesList.contains(nombred));
      } catch (Exception e) {
          fail(e.getMessage());
      }
      
      String nombrec = "VueloCaroo";
      try {
          icr.altaCategoria(nombrec);
          String[] categorias = icr.listarCategorias();
          List<String> categoriasList = Arrays.asList(categorias);
          assertTrue(categoriasList.contains(nombrec));
      } catch (Exception e) {
          fail(e.getMessage());
      }
      String[] categorias = {nombrec};
      
          try {
			icr.NuevoRutaDeVuelo(aerolinea,nombre,desccorta,descripcion,hora, 200,300,50,"Melpo","BuenosAiressss", fechaAlta, categorias);
			List<String> rutas;
			try {
				rutas = icr.listarRutasDeVuelo(aerolinea);
				assertTrue(rutas.contains(nombre));
			} catch (RutaNoExisteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
		} catch (DatosInvalidosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
	
  @Test
  void listarAerolineasTest() {
  
  	//creo dos aerolineas y llamo a listar y me fijo si estan contenidas
  	String nicka = "aeroliili";
	String nombrea = "AerolineasUruguayas";
	String emaila = "aersssol@gmail.com";
	String descripciona = "es una aerolinea";
	String link = "www.aerolineas.com";
	String contra="nuevacontra";
	String foto="nuevafoto";
	try {
		icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contra,foto);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
		assertEquals(dua.getContra(),contra);
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//ahora la segunda
	String nickb = "aeroliib";
	String nombreb = "AerolineasUruguayas";
	String emailb = "aersssob@gmail.com";
	String descripcionb = "es una aerolinea";
	String linkb = "www.aerolineas.com";
	String contraa="nuevacontra";
	String fotoa="nuevafoto";
	try {
		icu.NuevaAerolinea(nickb, nombreb, emailb, descripcionb, linkb,contraa,fotoa);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
		assertEquals(dua.getContra(),contraa);
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		List<String> as = icu.listarAerolineas();
		assertTrue(as.contains(nicka));
		assertTrue(as.contains(nickb));
		
	} catch (UsuarioNoExisteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
  }

  

  @Test
  void testReservarVueloOK() {
      String nick = "matiass2";
      String categoria = "Turista";
      String vuelo = "Vuelooo";
      LocalDate fecha = LocalDate.of(2021, 5, 1);
	String nombrec = "Matias";
	String apellidoc = "Ruiz";
	String emailc = "correoo2@gmail.com";
	String descripcionc = "es un cliente";
	String linkc = "www.cliente.com";
	String tipoDoc = "Cedula";
	String numDoc = "54401357";
	String contra="nuevacontra";
	String foto="nuevafoto";
	try {
	icu.NuevoCliente(nick, nombrec, apellidoc, emailc, fecha, "Uruguayo", tipoDoc, numDoc,contra,foto);
	DTUser du = icu.mostrarDatosUsuario(nick);
	
	assertEquals(du.getNick(),nick);
	assertEquals(du.getName(),nombrec);
	assertEquals(du.getCorreo(),emailc);
	DTCliente dc = (DTCliente) du;
	assertEquals(dc.getApellido(),apellidoc);
	assertEquals(dc.getFechaNac(),fecha);
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
	//creo el vuelo
	String aerolinea = "aeroliili";
	String ruta = "VueloaMontevideo";
	LocalDate fechaAlta = LocalDate.of(2021, 5, 1);
	String nombre = "Vuelooo";
	int asientosTurista = 100;
	int asientosEjecutivos = 50;
	int duracion = 120;
	try {
	icr.ingresarDatos(aerolinea, ruta, fecha, fechaAlta, nombre, asientosTurista, asientosEjecutivos, duracion);
	} catch (DatosInvalidosException | VueloYaExisteException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	};
      try {
          icr.ingresarDatosVuelo(nick, categoria, 0, fecha, vuelo);
          DTUser du = icu.mostrarDatosUsuario(nick);
          DTCliente dc = (DTCliente) du;
          List<String> pasajeros = icr.listarReservas(nombre);
          assertTrue(pasajeros.contains("Matias"));
      } catch (Exception e) {
          fail(e.getMessage());
      }
  }      
  

@Test
void AltaReservaConClienteVacio(){
	String cliente = "";
	String tipo = "Turista";
	Integer equipaje = 0;
	LocalDate f = LocalDate.of(2021, 5, 1);
	String vuelo = "Vuelooo";
	assertThrows(UsuarioNoExisteException.class,()->icr.ingresarDatosVuelo(cliente, tipo, equipaje, f, vuelo));
}
@Test
void AltaReservaConTipoVacio(){
	String cliente = "matias";
	String tipo = "";
	Integer equipaje = 0;
	LocalDate f = LocalDate.of(2021, 5, 1);
	String vuelo = "Vuelooo";
	assertThrows(DatosInvalidosException.class,()->icr.ingresarDatosVuelo(cliente, tipo, equipaje, f, vuelo));
}
@Test
void AltaReservaConEquipajeNegativo(){
	String cliente = "matias";
	String tipo = "Turista";
	Integer equipaje = -1;
	LocalDate f = LocalDate.of(2021, 5, 1);
	String vuelo = "Vuelooo";
	assertThrows(DatosInvalidosException.class,()->icr.ingresarDatosVuelo(cliente, tipo, equipaje, f, vuelo));
}
@Test
void AltaReservaConFechaVacia(){
	String cliente = "matias";
	String tipo = "Turista";
	Integer equipaje = 0;
	LocalDate f = null;
	String vuelo = "Vuelooo";
	assertThrows(DatosInvalidosException.class,()->icr.ingresarDatosVuelo(cliente, tipo, equipaje, f, vuelo));
}
@Test
void AltaReservaConVueloVacio(){
	String cliente = "matias";
	String tipo = "Turista";
	Integer equipaje = 0;
	LocalDate f = LocalDate.of(2021, 5, 1);
	String vuelo = "";
	assertThrows(VueloNoExisteException.class,()->icr.ingresarDatosVuelo(cliente, tipo, equipaje, f, vuelo));
}
  
@Test
public void testIngresarNombres_Turista_AsientosDisponibles() throws Exception {
String nick = "nomnom";
String nombre = "nomnom";
String email = "mati.escaaaaa@gmail.com";
String apellido = "Escarante";
LocalDate fecha =LocalDate.of(2022, 11, 17);
String nacionalidad = "Uruguay";
String tipoDoc = "Cedula";
String numDoc = "54401358";
String contra="contra";
String foto="foto";
try {
	icu.NuevoCliente(nick, nombre, apellido, email, fecha, nacionalidad, tipoDoc, numDoc,contra,foto);
	DTUser du = icu.mostrarDatosUsuario(nick);
}catch(UsuarioYaExisteException e) {
	fail(e.getMessage());
	e.printStackTrace();
} catch (DatosInvalidosException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
    String tipo = "Turista";
    int cantEqExtra = 2;
    String vuelo = "Vuelooo";
    int cantPasajeros = 2;
    Set<String[]> nombresApellidosPasajeros = new HashSet<>();
    nombresApellidosPasajeros.add(new String[]{"Carlos", "González"});
    nombresApellidosPasajeros.add(new String[]{"Ana", "López"});
    
    
    String aerolinea = "aeroliili";
    String ruta = "VueloaMontevideo";
    LocalDate fechaAlta = LocalDate.of(2021, 5, 1);
    int asientosTurista = 100;
    int asientosEjecutivos = 50;
    int duracion = 120;			        
    try {
        

        icr.ingresarNombres(nick, tipo, cantEqExtra, fechaAlta, vuelo, cantPasajeros, nombresApellidosPasajeros);
    } catch (VueloNoExisteException e) {
        fail("No se esperaba una excepción: " + e.getMessage());
    }
 catch (UsuarioNoExisteException e) {
      fail("No se esperaba una excepción: " + e.getMessage());
  }catch (AsientosInsuficientesException e) {
      fail("No se esperaba una excepción: " + e.getMessage());
  }
 catch (DatosInvalidosException e) {
      fail("No se esperaba una excepción: " + e.getMessage());
  }
}

@Test
public void testIngresarNombres_Ejecutivo_AsientosDisponibles() throws Exception {

String nick = "nombar";
String nombre = "nombar";
String email = "nombarrrrr@gmail.com";
String apellido = "Escarante";
LocalDate fecha =LocalDate.of(2022, 11, 17);
String nacionalidad = "Uruguay";
String tipoDoc = "Cedula";
String numDoc = "54401358";
String contra="contra";
String foto="foto";
try {
icu.NuevoCliente(nick, nombre, apellido, email, fecha, nacionalidad, tipoDoc, numDoc,contra,foto);
DTUser du = icu.mostrarDatosUsuario(nick);
}catch(UsuarioYaExisteException e) {
fail(e.getMessage());
e.printStackTrace();
//kvmdkvbfbfnhh
} catch (DatosInvalidosException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
  String tipo = "Ejecutivo";
  int cantEqExtra = 2;
  String vuelo = "Vuelooo";
  int cantPasajeros = 2;
  Set<String[]> nombresApellidosPasajeros = new HashSet<>();
  nombresApellidosPasajeros.add(new String[]{"Carlos", "González"});
  nombresApellidosPasajeros.add(new String[]{"Ana", "López"});
  
  
  String aerolinea = "aeroliili";
  String ruta = "VueloaMontevideo";
  LocalDate fechaAlta = LocalDate.of(2021, 5, 1);
  int asientosTurista = 100;
  int asientosEjecutivos = 50;
  int duracion = 120;			        
  try {
      

      icr.ingresarNombres(nick, tipo, cantEqExtra, fechaAlta, vuelo, cantPasajeros, nombresApellidosPasajeros);
  } catch (VueloNoExisteException e) {
      fail("No se esperaba una excepción: " + e.getMessage());
  }
catch (UsuarioNoExisteException e) {
    fail("No se esperaba una excepción: " + e.getMessage());
}catch (AsientosInsuficientesException e) {
    fail("No se esperaba una excepción: " + e.getMessage());
}
catch (DatosInvalidosException e) {
    fail("No se esperaba una excepción: " + e.getMessage());
}
}
@Test
void testGetCategorias() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    // Crear una RutaDeVuelo con algunas categorías
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                        Arrays.asList(new Categoria("Categoría1"), new Categoria("Categoría2")));

    List<Categoria> categorias = ruta.getCategorias();
    
    // Verificar que las categorías se obtienen correctamente
    assertEquals(2, categorias.size());
    assertEquals("Categoría1", categorias.get(0).getNombre());
    assertEquals("Categoría2", categorias.get(1).getNombre());
}
@Test
void testObtenerDatos() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.of(2024, 1, 1), 
                                        Arrays.asList(new Categoria("Categoría1")));

    List<String> datos = ruta.obtenerDatos();
    
    assertEquals("NombreRuta", datos.get(0)); // Nombre
    assertEquals("Descripción Corta", datos.get(1)); // Descripción corta
    assertEquals("Descripción", datos.get(2)); // Descripción
}
@Test
void testObtenerPocosDatos() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                        Arrays.asList(new Categoria("Categoría1")));
    List<String> pocosDatos = ruta.obtenerPocosDatos();
    
    assertEquals("NombreRuta", pocosDatos.get(0)); // Nombre
}
@Test
void testTieneCategoria() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                        Arrays.asList(new Categoria("Categoría1")));

    assertTrue(ruta.tieneCategoria("Categoría1")); // Debería devolver true
    assertFalse(ruta.tieneCategoria("Categoría2")); // Debería devolver false
}
@Test
void testEstaConfirmada() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                        Arrays.asList(new Categoria("Categoría1")));
    assertFalse(ruta.estaConfirmada()); // Asumiendo que por defecto no está confirmada

    ruta.setEstado(1); // Cambiamos el estado a confirmado
    assertTrue(ruta.estaConfirmada()); // Ahora debería ser true
}
@Test
void testGetEstado() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                        Arrays.asList(new Categoria("Categoría1")));
    assertEquals(Estado.ingresada, ruta.getEstado()); // Asumiendo que está en estado ingresado por defecto
}
@Test
void testGetCorigen() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                        Arrays.asList(new Categoria("Categoría1")));
    assertEquals(ciudadOrigen, ruta.getCorigen()); // Verifica que devuelva la ciudad de origen
}
@Test
void testGetCdestino() {
	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
    RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                        LocalTime.of(10, 0), 100, 200, 50, 
                                        ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                        Arrays.asList(new Categoria("Categoría1")));
    assertEquals(ciudadDestino, ruta.getCdestino()); // Verifica que devuelva la ciudad de destino
}
@Test
void testGetReserva() {
	DatosPasaje datosPasaje = new DatosPasaje("Reserva123", "Matias", "Escalante");
    assertEquals("Reserva123", datosPasaje.getReserva());
}

@Test
void testListarRutasEstado() throws VueloYaExisteException, RutaNoExisteException {
	String nicka = "aerolii1";
	String nickaa = "aerolii2";
	String nombrea = "AerolineasUruguayas";
	String emaila = "aerssso1@gmail.com";
	String emaila2 = "aerssso2@gmail.com";
	String descripciona = "es una aerolinea";
	String link = "www.aerolineas.com";
	String contra="contraseña";
	String foto="foto";
	try {
		icu.NuevaAerolinea(nicka, nombrea, emaila, descripciona, link,contra,foto);
		icu.NuevaAerolinea(nickaa, nombrea, emaila2, descripciona, link,contra,foto);
		DTUser dua = icu.mostrarDatosUsuario(nicka);
		assertEquals(dua.getNick(),nicka);
		assertEquals(dua.getName(),nombrea);
		assertEquals(dua.getCorreo(),emaila);
		DTAerolinea da = (DTAerolinea) dua;
	}catch(UsuarioYaExisteException e) {
		fail(e.getMessage());
		e.getStackTrace();
	} catch (DatosInvalidosException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    String aerolinea = "aerolii1";
    String nombre = "VueloaMontevideoo";
    String descripcion = "Vuelo a Montevideo";
    LocalTime hora = LocalTime.of(10, 0);
    LocalDate fechaAlta = LocalDate.of(2024, 12, 12);
    String nombreo = "Melpoo";
    String pais = "Uruguay";
    String aeropuerto = "Aeropuerto de Carrasco";
    String descripciono = "Capital de Uruguay";
    String website = "https://montevideo.gub.uy/";
    LocalDate fechaAltao = LocalDate.of(2021, 5, 1);
    try {
        icr.altaCiudad(nombreo, pais, aeropuerto, descripciono, website, fechaAltao);
        String[] ciudades = icr.listarCiudades();
        List<String> ciudadesList = Arrays.asList(ciudades);
        //debe buscar nombre en ciudades de tipo String[]
        assertTrue(ciudadesList.contains(nombreo));
    } catch (Exception e) {
        fail(e.getMessage());
    } 
    String nombred = "BuenosAiresssss";
    String paiss = "Argentina";
    String aeropuertos = "Aeropuerto de Conchillas";
    String desccorta="corta";
    String descripciond = "Capital de Argentina";
    String websites = "https://argentina.gub.uy/";
    LocalDate fechaAltad = LocalDate.of(2021, 5, 2);
    try {
        icr.altaCiudad(nombred, paiss, aeropuertos, descripciond, websites, fechaAltad);
        String[] ciudades = icr.listarCiudades();
        List<String> ciudadesList = Arrays.asList(ciudades);
        //debe buscar nombre en ciudades de tipo String[]
        assertTrue(ciudadesList.contains(nombred));
    } catch (Exception e) {
        fail(e.getMessage());
    }
    
    String nombrec = "VueloCarooo";
    String nombrecc = "cat";
    try {
        icr.altaCategoria(nombrec);
        icr.altaCategoria(nombrecc);
        String[] categorias = icr.listarCategorias();
        List<String> categoriasList = Arrays.asList(categorias);
        assertTrue(categoriasList.contains(nombrec));
    } catch (Exception e) {
        fail(e.getMessage());
    }
    String[] categorias = {nombrec,nombrecc};
    String[] categorias2 = {nombrec};
    String[] categorias3 = {nombrecc};
    
    String nombre2 = "nombre2";
    String nombre3 = "nombre3";
    String nombre4 = "nombre4";
    String nombre5 = "nombre5";
    String nombre6 = "nombre6";
        try {
			icr.NuevoRutaDeVuelo(aerolinea,nombre,desccorta,descripcion,hora, 200,300,50,"Melpoo","BuenosAiresssss", fechaAlta, categorias);
			icr.setImagen(nombre, "ruta a imagen");
			icr.cambiarEstadoRuta(nombre, 0);
			assertTrue(icr.obtenerDatosRuta(nombre).get(15).equals("ruta a imagen"));
			icr.NuevoRutaDeVuelo(aerolinea,nombre2,desccorta,descripcion,hora, 200,300,50,"Melpoo","BuenosAiresssss", fechaAlta, categorias3);
			icr.cambiarEstadoRuta(nombre2, 0);
			icr.NuevoRutaDeVuelo(aerolinea,nombre3,desccorta,descripcion,hora, 200,300,50,"Melpoo","BuenosAiresssss", fechaAlta, categorias2);
			icr.cambiarEstadoRuta(nombre3, 2);
			icr.NuevoRutaDeVuelo(aerolinea,nombre4,desccorta,descripcion,hora, 200,300,50,"Melpoo","BuenosAiresssss", fechaAlta, categorias2);
			icr.NuevoRutaDeVuelo(nickaa,nombre5,desccorta,descripcion,hora, 200,300,50,"Melpoo","BuenosAiresssss", fechaAlta, categorias3);
			icr.NuevoRutaDeVuelo(nickaa,nombre6,desccorta,descripcion,hora, 200,300,50,"Melpoo","BuenosAiresssss", fechaAlta, categorias3);
			icr.cambiarEstadoRuta(nombre6, 2);
			List<String> rutas;
			
			
			List<List<String>> datos = icr.listarDatosRutasConfirmadas(aerolinea);
			List<String> nombres = new ArrayList<>();
			for (List<String> it : datos) {
				nombres.add(it.get(0));
			}
			
			List<List<String>> datoss = icr.listarDatosRutasIngresadas(aerolinea);
			List<String> nombress = new ArrayList<>();
			for (List<String> itt : datoss) {
				nombress.add(itt.get(0));
			}
			
			assertTrue(nombress.contains(nombre4));
			
			List<List<String>> datosss = icr.listarDatosRutasRechazadas(aerolinea);
			List<String> nombresss = new ArrayList<>();
			for (List<String> ittt : datosss) {
				nombresss.add(ittt.get(0));
			}
			
			assertTrue(nombresss.contains(nombre3));
			
			assertTrue(nombres.contains(nombre));
			assertTrue(nombres.contains(nombre2));
			
			List<List<String>> datosCon =icr.listarDatosRutasConfirmadas();
			List<String> nombrescon = new ArrayList<>();
			for (List<String> itcon : datosCon) {
				nombrescon.add(itcon.get(0));
			}
			
			assertTrue(icr.listarRutasDeVueloConfirmadas(aerolinea).contains(nombre));
			assertTrue(icr.listarRutasDeVueloConfirmadas(aerolinea).contains(nombre2));
			
			assertTrue(icr.findRutaDeVuelo(nombre6).getName().equalsIgnoreCase(nombre6));
			
			assertTrue(nombres.contains(nombre));
			assertTrue(nombres.contains(nombre2));
			
			List<List<String>> datosConn = icr.listarDatosRutasConfirmadasCategoria(nombrec);
			List<String> nombresconn = new ArrayList<>();
			for (List<String> itconn : datosConn) {
				nombresconn.add(itconn.get(0));
			}
			
			assertTrue(nombresconn.contains(nombre));
			
			List<DTRutaDeVuelo> datosConnn =  icr.obtenerRutasPorCategoria(nombrec);
			List<String> nombresdat = new ArrayList<>();
			for (DTRutaDeVuelo itda : datosConnn) {
				nombresdat.add(itda.getNombre());
			}
			assertTrue(nombresdat.contains(nombre));
			assertTrue(nombresdat.contains(nombre3));
			assertTrue(nombresdat.contains(nombre4));
			
			try {
				rutas = icr.listarRutasDeVuelo(aerolinea);
				assertTrue(rutas.contains(nombre));
			} catch (RutaNoExisteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
		} catch (DatosInvalidosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
        


@Test
void testGetNombre() {
	DatosPasaje datosPasaje = new DatosPasaje("Reserva123", "Matias", "Escalante");
    assertEquals("Matias", datosPasaje.getNombre());
}

@Test
void testGetApellido() {
	DatosPasaje datosPasaje = new DatosPasaje("Reserva123", "Matias", "Escalante");
    assertEquals("Escalante", datosPasaje.getApellido());
}

@Test
void testSetReserva() {
	DatosPasaje  datosPasaje = new DatosPasaje("Reserva123", "Matias", "Escalante");
    datosPasaje.setReserva("Reserva456");
    assertEquals("Reserva456", datosPasaje.getReserva());
}

@Test
void testSetNombre() {
	DatosPasaje datosPasaje = new DatosPasaje("Reserva123", "Matias", "Escalante");
    datosPasaje.setNombre("Juan");
    assertEquals("Juan", datosPasaje.getNombre());
}

@Test
void testSetApellido() {
		DatosPasaje datosPasaje = new DatosPasaje("Reserva123", "Matias", "Escalante");
    datosPasaje.setApellido("Perez");
    assertEquals("Perez", datosPasaje.getApellido());
}
@Test
void testToString() {
	DatosPasaje datosPasaje = new DatosPasaje("Reserva123", "Matias", "Escalante");
    String expected = "DatosPasaje{reserva='Reserva123', nombre='Matias', apellido='Escalante'}";
    assertEquals(expected, datosPasaje.toString());
}
@Test
void testGetRefAerolinea() {
		DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
		TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    assertEquals("Aero1", tuplaDatos.getRefAerolinea());
}

@Test
void testSetRefAerolinea() {
		DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
		TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    tuplaDatos.setRefAerolinea("Aero2");
    assertEquals("Aero2", tuplaDatos.getRefAerolinea());
}

@Test
void testGetRefRuta() {
		DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
		TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    assertEquals("Ruta1", tuplaDatos.getRefRuta());
}

@Test
void testSetRefRuta() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    tuplaDatos.setRefRuta("Ruta2");
    assertEquals("Ruta2", tuplaDatos.getRefRuta());
}

@Test
void testGetRefVuelo() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    assertEquals("Vuelo1", tuplaDatos.getRefVuelo());
}

@Test
void testSetRefVuelo() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    tuplaDatos.setRefVuelo("Vuelo2");
    assertEquals("Vuelo2", tuplaDatos.getRefVuelo());
}

@Test
void testGetRefCliente() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    assertEquals("Cliente1", tuplaDatos.getRefCliente());
}

@Test
void testSetRefCliente() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    tuplaDatos.setRefCliente("Cliente2");
    assertEquals("Cliente2", tuplaDatos.getRefCliente());
}

@Test
void testGetDtReservas() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    assertEquals(dtReserva, tuplaDatos.getDtReservas());
}

@Test
void testSetDtReservas() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    DTReserva nuevaReserva = new DTReserva(2, 150, "Ejecutivo", LocalDate.of(2024, 11, 18), null);
    tuplaDatos.setDtReservas(nuevaReserva);
    assertEquals(nuevaReserva, tuplaDatos.getDtReservas());
}

@Test
void testGetCantPasajeros() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    assertEquals(2, tuplaDatos.getcantPasajeros());
}

@Test
void testSetCantPasajeros() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 3, dtReserva);
    assertEquals(3, tuplaDatos.getcantPasajeros());
}

@Test
void testDeToString() {
	DTReserva dtReserva = new DTReserva(1, 100, "Turista", LocalDate.of(2024, 10, 18), null);
	TuplaDatosReserva tuplaDatos = new TuplaDatosReserva("Aero1", "Ruta1", "Vuelo1", "Cliente1", 2, dtReserva);
    String expectedString = "TuplaDatosReservas{refAerolinea='Aero1', refRuta='Ruta1', refVuelo='Vuelo1', refCliente='Cliente1', dtReservas=" + dtReserva + "}";
    assertEquals(expectedString, tuplaDatos.toString());
}
@Test
void testGetRefAerolínea() {
	String refAerolínea = "Aerolínea XYZ";
  String refRuta = "Ruta 123";
  String nombreVuelo = "Vuelo 123";
  LocalDate fechaVuelo = LocalDate.of(2024, 5, 1);
  LocalDate fechaAlta = LocalDate.of(2024, 1, 1);
  int duracion = 120; // duración en minutos
  int asientosTurista = 100;
  int asientosEjecutivo = 50;
  
  // Lista de reservas (vacía en este caso)
  List<DTReserva> reservas = new ArrayList<>();
  
  // Imagen de prueba
  String imagen = "media/images/vuelo123.jpg";
  
  // Crear una instancia de Aerolinea (puedes usar una implementación adecuada)
  Aerolinea aerolinea = new Aerolinea("Aerolínea XYZ", "xyz@example.com", "xyz", "Descripción", "www.aerolineaxyz.com", "password", "media/images/logo.png");
  
  // Crear la instancia de DTVuelo
  DTVuelo dtVuelo = new DTVuelo(nombreVuelo, fechaVuelo, fechaAlta, duracion, asientosTurista, asientosEjecutivo, reservas, imagen, aerolinea);

  TuplaDatosVuelo tupla = new TuplaDatosVuelo(refAerolínea, refRuta, dtVuelo);
    assertEquals(refAerolínea, tupla.getRefAerolínea());
}

@Test
void testDeGetRefRuta() {
	String refAerolínea = "Aerolínea XYZ";
  String refRuta = "Ruta 123";
  String nombreVuelo = "Vuelo 123";
  LocalDate fechaVuelo = LocalDate.of(2024, 5, 1);
  LocalDate fechaAlta = LocalDate.of(2024, 1, 1);
  int duracion = 120; // duración en minutos
  int asientosTurista = 100;
  int asientosEjecutivo = 50;
  
  // Lista de reservas (vacía en este caso)
  List<DTReserva> reservas = new ArrayList<>();
  
  // Imagen de prueba
  String imagen = "media/images/vuelo123.jpg";
  
  // Crear una instancia de Aerolinea (puedes usar una implementación adecuada)
  Aerolinea aerolinea = new Aerolinea("Aerolínea XYZ", "xyz@example.com", "xyz", "Descripción", "www.aerolineaxyz.com", "password", "media/images/logo.png");
  
  // Crear la instancia de DTVuelo
  DTVuelo dtVuelo = new DTVuelo(nombreVuelo, fechaVuelo, fechaAlta, duracion, asientosTurista, asientosEjecutivo, reservas, imagen, aerolinea);

  TuplaDatosVuelo tupla = new TuplaDatosVuelo(refAerolínea, refRuta, dtVuelo);
    assertEquals(refRuta, tupla.getRefRuta());
}

@Test
void testGetDtVuelo() {
	String refAerolínea = "Aerolínea XYZ";
  String refRuta = "Ruta 123";
  String nombreVuelo = "Vuelo 123";
  LocalDate fechaVuelo = LocalDate.of(2024, 5, 1);
  LocalDate fechaAlta = LocalDate.of(2024, 1, 1);
  int duracion = 120; // duración en minutos
  int asientosTurista = 100;
  int asientosEjecutivo = 50;
  
  // Lista de reservas (vacía en este caso)
  List<DTReserva> reservas = new ArrayList<>();
  
  // Imagen de prueba
  String imagen = "media/images/vuelo123.jpg";
  
  // Crear una instancia de Aerolinea (puedes usar una implementación adecuada)
  Aerolinea aerolinea = new Aerolinea("Aerolínea XYZ", "xyz@example.com", "xyz", "Descripción", "www.aerolineaxyz.com", "password", "media/images/logo.png");
  
  // Crear la instancia de DTVuelo
  DTVuelo dtVuelo = new DTVuelo(nombreVuelo, fechaVuelo, fechaAlta, duracion, asientosTurista, asientosEjecutivo, reservas, imagen, aerolinea);

  TuplaDatosVuelo tupla = new TuplaDatosVuelo(refAerolínea, refRuta, dtVuelo);
    assertEquals(dtVuelo, tupla.getDtVuelo());
}

@Test
void testSetRefAerolínea() {
	String refAerolínea = "Aerolínea XYZ";
  String refRuta = "Ruta 123";
  String nombreVuelo = "Vuelo 123";
  LocalDate fechaVuelo = LocalDate.of(2024, 5, 1);
  LocalDate fechaAlta = LocalDate.of(2024, 1, 1);
  int duracion = 120; // duración en minutos
  int asientosTurista = 100;
  int asientosEjecutivo = 50;
  
  // Lista de reservas (vacía en este caso)
  List<DTReserva> reservas = new ArrayList<>();
  
  // Imagen de prueba
  String imagen = "media/images/vuelo123.jpg";
  
  // Crear una instancia de Aerolinea (puedes usar una implementación adecuada)
  Aerolinea aerolinea = new Aerolinea("Aerolínea XYZ", "xyz@example.com", "xyz", "Descripción", "www.aerolineaxyz.com", "password", "media/images/logo.png");
  
  // Crear la instancia de DTVuelo
  DTVuelo dtVuelo = new DTVuelo(nombreVuelo, fechaVuelo, fechaAlta, duracion, asientosTurista, asientosEjecutivo, reservas, imagen, aerolinea);

  TuplaDatosVuelo tupla = new TuplaDatosVuelo(refAerolínea, refRuta, dtVuelo);
    String newRefAerolínea = "Aerolínea ABC";
    tupla.setRefAerolínea(newRefAerolínea);
    assertEquals(newRefAerolínea, tupla.getRefAerolínea());
}

@Test
void testDeSetRefRuta() {
	String refAerolínea = "Aerolínea XYZ";
  String refRuta = "Ruta 123";
  String nombreVuelo = "Vuelo 123";
  LocalDate fechaVuelo = LocalDate.of(2024, 5, 1);
  LocalDate fechaAlta = LocalDate.of(2024, 1, 1);
  int duracion = 120; // duración en minutos
  int asientosTurista = 100;
  int asientosEjecutivo = 50;
  
  // Lista de reservas (vacía en este caso)
  List<DTReserva> reservas = new ArrayList<>();
  
  // Imagen de prueba
  String imagen = "media/images/vuelo123.jpg";
  
  // Crear una instancia de Aerolinea (puedes usar una implementación adecuada)
  Aerolinea aerolinea = new Aerolinea("Aerolínea XYZ", "xyz@example.com", "xyz", "Descripción", "www.aerolineaxyz.com", "password", "media/images/logo.png");
  
  // Crear la instancia de DTVuelo
  DTVuelo dtVuelo = new DTVuelo(nombreVuelo, fechaVuelo, fechaAlta, duracion, asientosTurista, asientosEjecutivo, reservas, imagen, aerolinea);

  TuplaDatosVuelo tupla = new TuplaDatosVuelo(refAerolínea, refRuta, dtVuelo);
    String newRefRuta = "Ruta 456";
    tupla.setRefRuta(newRefRuta);
    assertEquals(newRefRuta, tupla.getRefRuta());
}

@Test
void testSetDtVuelo() {
	String refAerolínea = "Aerolínea XYZ";
  String refRuta = "Ruta 123";
  String nombreVuelo = "Vuelo 123";
  LocalDate fechaVuelo = LocalDate.of(2024, 5, 1);
  LocalDate fechaAlta = LocalDate.of(2024, 1, 1);
  int duracion = 120; // duración en minutos
  int asientosTurista = 100;
  int asientosEjecutivo = 50;
  
  // Lista de reservas (vacía en este caso)
  List<DTReserva> reservas = new ArrayList<>();
  
  // Imagen de prueba
  String imagen = "media/images/vuelo123.jpg";
  
  // Crear una instancia de Aerolinea (puedes usar una implementación adecuada)
  Aerolinea aerolinea = new Aerolinea("Aerolínea XYZ", "xyz@example.com", "xyz", "Descripción", "www.aerolineaxyz.com", "password", "media/images/logo.png");
  
  // Crear la instancia de DTVuelo
  DTVuelo dtVuelo = new DTVuelo(nombreVuelo, fechaVuelo, fechaAlta, duracion, asientosTurista, asientosEjecutivo, reservas, imagen, aerolinea);

  TuplaDatosVuelo tupla = new TuplaDatosVuelo(refAerolínea, refRuta, dtVuelo);
    tupla.setDtVuelo(dtVuelo);
    assertEquals(dtVuelo, tupla.getDtVuelo());
}

@Test
void testdeToString() {
	String refAerolínea = "Aerolínea XYZ";
  String refRuta = "Ruta 123";
  String nombreVuelo = "Vuelo 123";
  LocalDate fechaVuelo = LocalDate.of(2024, 5, 1);
  LocalDate fechaAlta = LocalDate.of(2024, 1, 1);
  int duracion = 120; // duración en minutos
  int asientosTurista = 100;
  int asientosEjecutivo = 50;
  
  // Lista de reservas (vacía en este caso)
  List<DTReserva> reservas = new ArrayList<>();
  
  // Imagen de prueba
  String imagen = "media/images/vuelo123.jpg";
  
  // Crear una instancia de Aerolinea (puedes usar una implementación adecuada)
  Aerolinea aerolinea = new Aerolinea("Aerolínea XYZ", "xyz@example.com", "xyz", "Descripción", "www.aerolineaxyz.com", "password", "media/images/logo.png");
  
  // Crear la instancia de DTVuelo
  DTVuelo dtVuelo = new DTVuelo(nombreVuelo, fechaVuelo, fechaAlta, duracion, asientosTurista, asientosEjecutivo, reservas, imagen, aerolinea);

  TuplaDatosVuelo tupla = new TuplaDatosVuelo(refAerolínea, refRuta, dtVuelo);
    String expected = "TuplaDatosVuelo{refAerolínea='Aerolínea XYZ', refRuta='Ruta 123', dtVuelo=" + dtVuelo + "}";
    assertEquals(expected, tupla.toString());
}
}