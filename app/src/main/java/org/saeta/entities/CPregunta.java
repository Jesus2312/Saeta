package org.saeta.entities;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CPregunta {
    private  int IdPregunta;
    private  int IdEncuesta;
    private  String Pregunta ;
    private  boolean MultiRespuesta;
    private  String Seleccionado ;
    private ArrayList<CRespuesta> Respuestas = new ArrayList<CRespuesta>();

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
