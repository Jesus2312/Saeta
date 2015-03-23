package org.saeta.bussiness;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;
import org.saeta.webservice.WsConsume;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/19/2015.
 */
public class EncuestaBE {

    public static ArrayList<CPersona> ObtenerPersonasAEncuestar(Context c ) throws Exception
    {
        ArrayList<CPersona> personas=  null;
        try
        {
           //  int encuestaId = SaetaUtils.tryIntParse(c);
             personas= new EncustaDAL().ObtenerPersonasAEncuestar(c);
        }
        catch (Exception e)
        {
           throw e;
        }
        return  personas;
    }

    public static String SubirEncuestas (Context context)
    {
        String result="";
        try
        {

           Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(); // excluye los campos que solo se uan en ambito de la app
           ArrayList<CEncuesta> encuestas = new EncustaDAL(context).ObtenerEncuestasRealizadas();
           ArrayList<String >jsonArray = new ArrayList<String>();

            for(CEncuesta e: encuestas)
            {
                String encuestasJson = gson.toJson(e);
                jsonArray.add(encuestasJson);
                WsConsume consume = new WsConsume("http://api.saeta.org.mx/auditoria/");
                consume.wsPostRequest(UserSession.TOKEN_KEY,e);
            }
            result="1";
        }
        catch (Exception x)
        {
            result = "Error al subir encuestas (E020) detalles: "+x.getMessage();
        }
        return  result;

    }

}
