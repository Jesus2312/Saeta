package org.saeta.entities;

/**
 * Created by jlopez on 3/30/2015.
 */
public class SaetaLocation {

    public  SaetaLocation()
    {
        this.latitud ="0.0";
        this.longitud="0.0";

    }
    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    private String latitud;

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    private String longitud;

}
