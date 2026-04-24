package logica;

public class DatosPasaje {
    private String reserva;
    private String nombre;
    private String apellido;

    // Constructor
    public DatosPasaje(String reserva, String nombre, String apellido) {
        this.reserva = reserva;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Getters
    public String getReserva() {
        return reserva;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    // Setters
    public void setReserva(String reserva) {
        this.reserva = reserva;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Método toString para representación
    @Override
    public String toString() {
        return "DatosPasaje{" +
                "reserva='" + reserva + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                '}';
    }
}

