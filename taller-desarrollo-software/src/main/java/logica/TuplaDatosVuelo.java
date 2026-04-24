package logica;

import logica.DTVuelo;

public class TuplaDatosVuelo {
    private String refAerolínea;
    private String refRuta;
    private DTVuelo dtVuelo;

    // Constructor
    public TuplaDatosVuelo(String refAerolínea, String refRuta, DTVuelo dtVuelo) {
        this.refAerolínea = refAerolínea;
        this.refRuta = refRuta;
        this.dtVuelo = dtVuelo;
    }

    // Getters y Setters
    public String getRefAerolínea() {
        return refAerolínea;
    }

    public void setRefAerolínea(String refAerolínea) {
        this.refAerolínea = refAerolínea;
    }

    public String getRefRuta() {
        return refRuta;
    }

    public void setRefRuta(String refRuta) {
        this.refRuta = refRuta;
    }

    public DTVuelo getDtVuelo() {
        return dtVuelo;
    }

    public void setDtVuelo(DTVuelo dtVuelo) {
        this.dtVuelo = dtVuelo;
    }

    @Override
    public String toString() {
        return "TuplaDatosVuelo{" +
               "refAerolínea='" + refAerolínea + '\'' +
               ", refRuta='" + refRuta + '\'' +
               ", dtVuelo=" + dtVuelo +
               '}';
    }
}

