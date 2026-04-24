package logica;

import java.time.LocalDate;

public class DTCiudad {

    private String nombre;
    private String pais;
    private String aeropuerto;
    private String descripcion;
    private String website;
    private LocalDate fechaAlta;

    // Constructor con todos los atributos
    public DTCiudad(String nombre, String pais, String aeropuerto, String descripcion, String website, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.pais = pais;
        this.aeropuerto = aeropuerto;
        this.descripcion = descripcion;
        this.website = website;
        this.fechaAlta = fechaAlta;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }

    public String getPais() {
        return pais;
    }

//    public void setPais(String pais) {
//        this.pais = pais;
//    }

    public String getAeropuerto() {
        return aeropuerto;
    }

//    public void setAeropuerto(String aeropuerto) {
//        this.aeropuerto = aeropuerto;
//    }

    public String getDescripcion() {
        return descripcion;
    }

//    public void setDescripcion(String descripcion) {
//        this.descripcion = descripcion;
//    }

    public String getWebsite() {
        return website;
    }

//    public void setWebsite(String website) {
//        this.website = website;
//    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

//    public void setFechaAlta(LocalDate fechaAlta) {
//        this.fechaAlta = fechaAlta;
//    }
//
//    @Override
//    public String toString() {
//        return "DTCiudad{" +
//                "nombre='" + nombre + '\'' +
//                ", pais='" + pais + '\'' +
//                ", aeropuerto='" + aeropuerto + '\'' +
//                ", descripcion='" + descripcion + '\'' +
//                ", website='" + website + '\'' +
//                ", fechaAlta=" + fechaAlta +
//                '}';
//    }
//}
}

