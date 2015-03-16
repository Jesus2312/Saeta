package org.saeta.entities;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CPregunta {
    public   int IdPregunta;
    public  int IdEncuesta;
    public  String Pregunta ;
    public  boolean MultiRespuesta;
    public  String Seleccionado ;
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



}
