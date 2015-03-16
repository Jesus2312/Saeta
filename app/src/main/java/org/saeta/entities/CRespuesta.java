package org.saeta.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CRespuesta {

    public int IdRespuesta;
    public int IdEncuesta;
    public String Respuesta ;
    public String Seleccionado;
    public boolean IndicadoraPAN ;
    public int OcasionesSeleccionada;
    public  String Domicilios ;
    public int IdPregunta;

    public String getDomicilios(){return  Domicilios;}
    public int getOcasionesSeleccionada(){return OcasionesSeleccionada;}
    public boolean isIndicadoraPAN(){return IndicadoraPAN;}
    public String getSeleccionado(){return  Seleccionado;}
    public String getRespuesta(){return Respuesta;}
    public int getIdEncuesta(){return IdEncuesta;}
    public int getIdRespuesta(){return IdRespuesta;}
    public  CRespuesta(){}



}
