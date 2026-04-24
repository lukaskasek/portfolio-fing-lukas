package logica;

import logica.DTReserva;

public class TuplaDatosReserva {
    private String refAerolinea;
    private String refRuta;
    private String refVuelo;
    private String refCliente;
    private DTReserva dtReserva;
    private  int cantpasajeros;

    // Constructor
    public TuplaDatosReserva(String refAerolinea, String refRuta, String refVuelo, String refCliente,int cantPasajeros, DTReserva dtReserva) {
        this.refAerolinea = refAerolinea;
        this.refRuta = refRuta; 
        this.refVuelo = refVuelo;
        this.refCliente = refCliente;
        this.dtReserva = dtReserva;
        this.cantpasajeros = cantPasajeros; 
    }

    // Getters y Setters
    public int getcantPasajeros() {
    	return cantpasajeros;
    	
    }
    public String getRefAerolinea() {
        return refAerolinea;
    }

    public void setRefAerolinea(String refAerolinea) {
        this.refAerolinea = refAerolinea;
    }

    public String getRefRuta() {
        return refRuta;
    }

    public void setRefRuta(String refRuta) {
        this.refRuta = refRuta;
    }

    public String getRefVuelo() {
        return refVuelo;
    }

    public void setRefVuelo(String refVuelo) {
        this.refVuelo = refVuelo;
    }

    public String getRefCliente() {
        return refCliente;
    }

    public void setRefCliente(String refCliente) {
        this.refCliente = refCliente;
    }

    public DTReserva getDtReservas() {
        return dtReserva;
    }

    public void setDtReservas(DTReserva dtReservas) {
        this.dtReserva = dtReservas;
    }

    @Override
    public String toString() {
        return "TuplaDatosReservas{" +
               "refAerolinea='" + refAerolinea + '\'' +
               ", refRuta='" + refRuta + '\'' +
               ", refVuelo='" + refVuelo + '\'' +
               ", refCliente='" + refCliente + '\'' +
               ", dtReservas=" + dtReserva +
               '}';
    }
}

