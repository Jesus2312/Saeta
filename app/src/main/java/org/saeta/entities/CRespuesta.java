package org.saeta.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CRespuesta {

    private int IdRespuesta;
    private int IdEncuesta;
    private String Respuesta ;
    private String Seleccionado;
    private boolean IndicadoraPAN ;
    private int OcasionesSeleccionada;
    private  String Domicilios ;

    public String getDomicilios(){return  Domicilios;}
    public int getOcasionesSeleccionada(){return OcasionesSeleccionada;}
    public boolean isIndicadoraPAN(){return IndicadoraPAN;}
    public String getSeleccionado(){return  Seleccionado;}
    public String getRespuesta(){return Respuesta;}
    public int getIdEncuesta(){return IdEncuesta;}
    public int getIdRespuesta(){return IdRespuesta;}
    public  CRespuesta(){}



}
