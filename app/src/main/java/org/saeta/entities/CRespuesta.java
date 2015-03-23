package org.saeta.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CRespuesta {

    @Expose
    public int IdRespuesta;
    @Expose
    public int IdEncuesta;
    @Expose
    public String Respuesta ;
    @Expose
    public String Seleccionado;
    @Expose
    public boolean IndicadoraPAN ;
    @Expose
    public int OcasionesSeleccionada;
    @Expose
    public  String Domicilios ;
    @Expose
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
