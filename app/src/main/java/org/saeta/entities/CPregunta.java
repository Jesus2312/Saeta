package org.saeta.entities;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CPregunta {
    @Expose
    public   int IdPregunta;
    @Expose
    public  int IdEncuesta;
    @Expose
    public  String Pregunta ;
    @Expose
    public  boolean MultiRespuesta;
    @Expose
    public  String Seleccionado ;
    @Expose
    public ArrayList<CRespuesta> Respuestas = new ArrayList<CRespuesta>();

    public  CPregunta(){}
    public ArrayList<CRespuesta> getRespuestas(){return Respuestas;}
    public String getSeleccionado()
    {
        return Seleccionado;
    }
    public boolean isMultiRespuesta()
    {
        return MultiRespuesta;
    }
    public  String getPregunta()
    {
        return  Pregunta;
    }
    public  int getIdEncuesta()
    {
        return IdEncuesta;
    }
    public  int getIdPregunta()
    {
        return IdPregunta;
    }
    public String VideoUrl ="";
    public String AudioUrl ="";



}
