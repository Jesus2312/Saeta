package org.saeta.bussiness;

import android.content.Context;
import android.database.Cursor;

import org.saeta.entities.CPersona;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/19/2015.
 */
public class EncustaDAL {


    public ArrayList<CPersona> ObtenerPersonasAEncuestar(int encuestaId, Context context) throws  Exception
    {
        ArrayList<CPersona> arrayResult = null;
        try
        {

            DataBaseHandler handler = new DataBaseHandler(context);
            String query = "SELECT * FROM SAETA_PERSONAS WHERE IDENCUESTA =" +encuestaId +" and ID_DETECTADO  not in (SELECT PERSONA_ID FROM SAETA_USUARIO_RESPUESTA" +
                    " WHERE ENCUESTA_ID =" + encuestaId+
                    "  AND TERMINADO =1 );";
            Cursor cr = handler.GetCursor(query);

            if (cr.moveToFirst())
            {
                CPersona pTemp = new CPersona();
                arrayResult = new ArrayList<CPersona>();

                pTemp = new CPersona();
                pTemp.setCalle(cr.getString(0));
                pTemp.setCodigoPostal(cr.getString(1));
                pTemp.setColonia(cr.getString(2));
                pTemp.setDistritoFederal(SaetaUtils.tryIntParse(cr.getString(3)));
                pTemp.setDistritoLocal(SaetaUtils.tryIntParse(cr.getString(4)));
                pTemp.setEstado(cr.getString(5));
                pTemp.setIdDetectado(SaetaUtils.tryIntParse(cr.getString(6)));
                pTemp.setLatitud(SaetaUtils.tryFloatParse(cr.getString(7)));
                pTemp.setLongitud(SaetaUtils.tryFloatParse(cr.getString(8)));
                pTemp.setManzana(SaetaUtils.tryIntParse(cr.getString(9)));
                pTemp.setMaterno(cr.getString(10));
                pTemp.setMunicipio(cr.getString(11));
                pTemp.setNombre(cr.getString(12));
                pTemp.setNumExterior(cr.getString(13));
                pTemp.setNumInterior(cr.getString(14));
                pTemp.setPaterno(cr.getString(15));
                pTemp.setSeccion(SaetaUtils.tryIntParse(cr.getString(16)));
                pTemp.setTelefono1(cr.getString(17));
                pTemp.setTelefono2(cr.getString(18));
                pTemp.setTelefono3(cr.getString(19));

                arrayResult.add(pTemp);
                while (cr.moveToNext())
                {
                    pTemp = new CPersona();
                    pTemp.setCalle(cr.getString(0));
                    pTemp.setCodigoPostal(cr.getString(1));
                    pTemp.setColonia(cr.getString(2));
                    pTemp.setDistritoFederal(SaetaUtils.tryIntParse(cr.getString(3)));
                    pTemp.setDistritoLocal(SaetaUtils.tryIntParse(cr.getString(4)));
                    pTemp.setEstado(cr.getString(5));
                    pTemp.setIdDetectado(SaetaUtils.tryIntParse(cr.getString(6)));
                    pTemp.setLatitud(SaetaUtils.tryFloatParse(cr.getString(7)));
                    pTemp.setLongitud(SaetaUtils.tryFloatParse(cr.getString(8)));
                    pTemp.setManzana(SaetaUtils.tryIntParse(cr.getString(9)));
                    pTemp.setMaterno(cr.getString(10));
                    pTemp.setMunicipio(cr.getString(11));
                    pTemp.setNombre(cr.getString(12));
                    pTemp.setNumExterior(cr.getString(13));
                    pTemp.setNumInterior(cr.getString(14));
                    pTemp.setPaterno(cr.getString(15));
                    pTemp.setSeccion(SaetaUtils.tryIntParse(cr.getString(16)));
                    pTemp.setTelefono1(cr.getString(17));
                    pTemp.setTelefono2(cr.getString(18));
                    pTemp.setTelefono3(cr.getString(19));

                    arrayResult.add(pTemp);
                }
            }
        }
        catch (Exception err)
        {
           throw err;
        }
        return arrayResult;
    }
}
