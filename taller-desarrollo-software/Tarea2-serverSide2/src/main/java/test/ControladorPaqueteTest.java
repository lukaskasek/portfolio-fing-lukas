package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import excepciones.DatosInvalidosException;
import excepciones.PaqueteYaExisteException;
import logica.Categoria;
import logica.Ciudad;
import logica.Fabrica;
import logica.IControladorPaquetes;
import logica.Paquete;
import logica.RutaDeVuelo;
import logica.RutaEnPaquete;

public class ControladorPaqueteTest {
	
	private static IControladorPaquetes icp;
	
	@BeforeAll
	public static void iniciar() {
		Fabrica fabrica = Fabrica.getInstancia();
		icp = fabrica.getIControladorPaquetes();
	}
	
	@Test
	 void crearPaqueteOK(){
		try {
			icp.crearPaquete("Nombre de paquete", "paqueteee", 10, 20, LocalDate.of(2024, 8, 2));
		} catch (PaqueteYaExisteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatosInvalidosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String np = icp.MostrarNombrePaquete("Nombre de paquete");
		assertEquals(np,"Nombre de paquete");
	};
	
	@Test
	void testCrearPaqueteNombreInvalido() {
	    assertThrows(DatosInvalidosException.class, () -> {
	        icp.crearPaquete("", "desc", 2, 3, LocalDate.of(2024, 4, 2));
	    });
	}

	@Test
	void testCrearPaqueteDescripcionInvalida() {
	    assertThrows(DatosInvalidosException.class, () -> {
	        icp.crearPaquete("paquete", "", 2, 3, LocalDate.of(2024, 4, 2));
	    });
	}

	@Test
	void testCrearPaqueteValidezMenorQueCero() {
	    assertThrows(DatosInvalidosException.class, () -> {
	        icp.crearPaquete("paquete", "desc", 0, 3, LocalDate.of(2024, 4, 2));
	    });
	}

	@Test
	void testCrearPaqueteDescuentoMenorOIgualCero() {
	    assertThrows(DatosInvalidosException.class, () -> {
	        icp.crearPaquete("paquete", "desc", 4, -2, LocalDate.of(2024, 4, 2));
	    });
	}

	@Test
	void testCrearPaqueteDescuentoMayor() {
	    assertThrows(DatosInvalidosException.class, () -> {
	        icp.crearPaquete("paquete", "desc", 3, 100, LocalDate.of(2024, 4, 2));
	    });
	}

	@Test
	void testCrearPaqueteFechaNull() {
	    assertThrows(DatosInvalidosException.class, () -> {
	        icp.crearPaquete("paquete", "desc", 5, 3, null);
	    });
	}

  @Test
  void testGetpValidez() {
  	Paquete paquete = new Paquete("Descripción de paquete","desc", 30, 10, LocalDate.now());
      assertEquals(30, paquete.getpValidez());
  }

  @Test
  void testGetDescuento() {
  	Paquete paquete = new Paquete("Descripción de paquete","desc", 30, 10, LocalDate.now());
      assertEquals(10, paquete.getDescuento());
  }

  @Test
  void testGetfechaAlta() {
  	Paquete paquete = new Paquete("Descripción de paquete","desc", 30, 10, LocalDate.now());
      assertEquals(LocalDate.now(), paquete.getfechaAlta());
  }

  @Test
  void testAgregarNuevaRuta() {
  	Paquete paquete = new Paquete("Descripción de paquete","desc", 30, 10, LocalDate.now());
  	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
  	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
      RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                          LocalTime.of(10, 0), 100, 200, 50, 
                                          ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                          Arrays.asList(new Categoria("Categoría1")));
      paquete.agregarNuevaRuta(ruta, "Turista", 2);
  }
  @Test
  void testGetRuta() {
  	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
  	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
      RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                          LocalTime.of(10, 0), 100, 200, 50, 
                                          ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                          Arrays.asList(new Categoria("Categoría1")));
  		RutaEnPaquete rutaEnPaquete = new RutaEnPaquete(ruta, "Turista", 2);
      assertEquals(ruta, rutaEnPaquete.getRuta());
  }

  @Test
  void testGetTipoAsiento() {
  	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
  	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
      RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                          LocalTime.of(10, 0), 100, 200, 50, 
                                          ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                          Arrays.asList(new Categoria("Categoría1")));
  		RutaEnPaquete rutaEnPaquete = new RutaEnPaquete(ruta, "Turista", 2);
      assertEquals("Turista", rutaEnPaquete.getTipoAsiento());
  }

  @Test
  void testGetCantidad() {
  	Ciudad ciudadOrigen = new Ciudad("Melo", "Uruguay", "Aeropuerto de Melo", "Ciudad en el norte de Uruguay", "www.melo.gub.uy", LocalDate.now());
  	Ciudad ciudadDestino = new Ciudad("Montevideo", "Uruguay", "Aeropuerto de Carrasco", "Capital de Uruguay", "www.montevideo.gub.uy", LocalDate.now());
      RutaDeVuelo ruta = new RutaDeVuelo("NombreRuta", "Descripción Corta", "Descripción", 
                                          LocalTime.of(10, 0), 100, 200, 50, 
                                          ciudadOrigen, ciudadDestino, LocalDate.now(), 
                                          Arrays.asList(new Categoria("Categoría1")));
  		RutaEnPaquete rutaEnPaquete = new RutaEnPaquete(ruta, "Turista", 2);
      assertEquals(2, rutaEnPaquete.getCantidad());
  }
		
	

}