package logica;

import java.time.LocalDate;

public class Ciudad {
    private String nombre;
    private String pais;
    private String aeropuerto;
    private String descripcion;
    private String webSite;
    private LocalDate fechaAlta;

    public Ciudad(String nombre, String pais, String aeropuerto, String descripcion, String webSite, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.pais = pais;
        this.aeropuerto = aeropuerto;
        this.descripcion = descripcion;
        this.webSite = webSite;
        this.fechaAlta = fechaAlta;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public String getAeropuerto() {
        return aeropuerto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getWebSite() {
        return webSite;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }}

    // Setters
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }
//
//    public void setPais(String pais) {
//        this.pais = pais;
//    }
//
//    public void setAeropuerto(String aeropuerto) {
//        this.aeropuerto = aeropuerto;
//    }
//
//    public void setDescripcion(String descripcion) {
//        this.descripcion = descripcion;
//    }
//
//    public void setWebSite(String webSite) {
//        this.webSite = webSite;
//    }
//
//    public void setFechaAlta(LocalDate fechaAlta) {
//        this.fechaAlta = fechaAlta;
//    }
//}