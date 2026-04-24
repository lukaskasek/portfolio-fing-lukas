package logica;

import java.time.LocalDate;
import java.time.LocalTime;

public class DTRutaDeVuelo {
    private String nombre;
    private String descripcion;
    private LocalTime hora;
    private int costoTurista;
    private int costoEjecutivo;
    private String desCorta;
    private String imagen;

    private int costoEExtra;
    LocalDate fechaAlta;
   // private Aerolinea aerolinea;

    // Constructor
    public DTRutaDeVuelo(String nombre, String desCorta, String descripcion, LocalTime hora, int costoTurista,int costoEjecutivo, int costoEExtra,LocalDate fechaAlta) {
    	this.desCorta = desCorta;
    	this.nombre = nombre;
        this.descripcion = descripcion;
        this.hora = hora;
        this.costoTurista = costoTurista;
        this.costoEjecutivo = costoEjecutivo;
        this.costoEExtra = costoEExtra;
        this.fechaAlta = fechaAlta;
    }
    public String getImagen() {
    	return this.imagen;
    }
    public void setImagen(String imagen) {
    	this.imagen = imagen;
    }
    // Getters y Setters
    public LocalDate getFechaAlta() {
    	return fechaAlta;
    }
    public String getNombre() {
        return nombre;
    }

    public String getDescripcionCorta() {
        return desCorta;
    }


    public String getDescripcion() {
        return descripcion;
    }

   

    public LocalTime getHora() {
        return hora;
    }

  
    public int getCostoTurista() {
        return costoTurista;
    }



    public int getCostoEjecutivo() {
        return costoEjecutivo;
    }

    

    public int getCostoEExtra() {
        return costoEExtra;
    }

 
}

