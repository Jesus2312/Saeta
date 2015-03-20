package org.saeta.bussiness;

import android.content.Context;

import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/19/2015.
 */
public class EncuestaBE {

    public static ArrayList<CPersona> ObtenerPersonasAEncuestar(CEncuesta encuesta,Context c ) throws Exception
    {
        ArrayList<CPersona> personas=  null;
        try
        {
             int encuestaId = SaetaUtils.tryIntParse(encuesta.getIdEncuesta());
             personas= new EncustaDAL().ObtenerPersonasAEncuestar(encuestaId,c);
        }
        catch (Exception e)
        {
           throw e;
        }
        return  personas;
    }

}
