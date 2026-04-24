package logica;

import java.util.List;

import logica.DTRutaDeVuelo;

public class TuplaDatosRutaDeVuelo {
    private String refAerolinea;
    private String refOrigen;
    private String refDestino;
    private List<String> refCategorias;
    private DTRutaDeVuelo dtRutaDeVuelo;

    // Constructor
    public TuplaDatosRutaDeVuelo(String refAerolinea, String refOrigen, String refDestino, 
                                 List<String> refCategorias, DTRutaDeVuelo dtRutaDeVuelo) {
        this.refAerolinea = refAerolinea;
        this.refOrigen = refOrigen;
        this.refDestino = refDestino;
        this.refCategorias = refCategorias;
        this.dtRutaDeVuelo = dtRutaDeVuelo;
    }

    // Getters y Setters
    public String getRefAerolinea() {
        return refAerolinea;
    }

    public void setRefAerolinea(String refAerolinea) {
        this.refAerolinea = refAerolinea;
    }

    public String getRefOrigen() {
        return refOrigen;
    }

    public void setRefOrigen(String refOrigen) {
        this.refOrigen = refOrigen;
    }

    public String getRefDestino() {
        return refDestino;
    }

    public void setRefDestino(String refDestino) {
        this.refDestino = refDestino;
    }

    public List<String> getRefCategorias() {
        return refCategorias;
    }

    public void setRefCategorias(List<String> refCategorias) {
        this.refCategorias = refCategorias;
    }

    public DTRutaDeVuelo getDtRutaDeVuelo() {
        return dtRutaDeVuelo;
    }

    public void setDtRutaDeVuelo(DTRutaDeVuelo dtRutaDeVuelo) {
        this.dtRutaDeVuelo = dtRutaDeVuelo;
    }

    @Override
    public String toString() {
        return "TuplaDatosRutaDeVuelo [refAerolinea=" + refAerolinea + ", refOrigen=" + refOrigen + 
               ", refDestino=" + refDestino + ", refCategorias=" + refCategorias + 
               ", dtRutaDeVuelo=" + dtRutaDeVuelo + "]";
    }
}

