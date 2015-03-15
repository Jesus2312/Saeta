package org.saeta.entities;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.saeta.bussiness.DataBaseHandler;

import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CEncuesta  {

    public final  static  String  TABLE_NAME ="SAETA_ENCUESTAS";

    private String IdEncuesta  ="";
    private String Encuesta ="";
    private String IdProceso="";
    private String IdDetectado ="";
    private String Municipio="";
    private String Paterno ="";
    private String Materno ="" ;
    private String Nombre ="";
    private String Telefono1 ="";
    private String Telefono2 ="";
    private String Telefono3 ="";
    private ArrayList<CPregunta> Preguntas = new ArrayList<CPregunta>();

    public ArrayList<CPregunta> getPreguntas(){return Preguntas;}

    public CEncuesta()
    {

    }

    public  String getTelefono3()
    {
        if (Telefono3!= null && Telefono2!="")
        {
            return  Telefono3;
        }
        else
        {
            return"N/A";
        }
    }

    public  String getTelefono2()
    {
        if (Telefono2!= null && Telefono2!=""){
            return Telefono2;
        }
        else {
            return  "N/A";
        }
    }

    public  String getTelefono1 ()
    {
       if (Telefono1!=null && Telefono1!=""){
           return Telefono1;
       }
        else
       {
           return "N/A";
       }
    }
    public String getNombre()
    {
        return Nombre;
    }

    public  String getMaterno ()
    {
        return Materno;
    }

    public  String getApellidoPaterno ()
    {
        return Paterno;
    }

    public  String getMunicipio()
    {
        return Municipio;
    }
    public String getIdDetectado()
    {
        return IdDetectado;
    }


    public String  getIdEncuesta()
    {
        return IdEncuesta;
    }

    public String getEncuesta ()
    {
        return Encuesta;
    }
    public String getIdProceso()
    {
        return IdProceso;
    }

    public ContentValues getContentValues()
    {
        ContentValues cv = new ContentValues();
        cv.put("IdEncuesta",1);
        cv.put("Encuesta",getEncuesta());
        cv.put("IdProceso",getIdProceso());
        cv.put("IdDetectado",getIdDetectado());
        cv.put("Paterno",getApellidoPaterno());
        cv.put("Materno",getMaterno());
        cv.put("Nombre",getNombre());
        cv.put("Telefono1",getTelefono1());
        cv.put("Telefono2",getTelefono2());
        cv.put("Telefono3",getTelefono3());
       return  cv;

    }

    public  String saveToDataBase(Context context)
    {
        String r= "";
        try
        {
            //GUARDAR LA ENCUESTA
           StringBuilder qry= new StringBuilder();

            qry.append(" INSERT INTO SAETA_ENCUESTAS VALUES ( ")
                    .append(this.IdEncuesta).append(" ,'").append(this.Encuesta).append("', ") .append( IdProceso)
                    .append(" , ").append(IdDetectado).append(" , '").append(Paterno).append("' , '").append(Materno).append("','").append(Nombre)
                    .append("','").append(Telefono1).append("','").append(Telefono2).append("','").append(Telefono3).append("')");

            String f = qry.toString();

            DataBaseHandler handler = new DataBaseHandler(context);
            handler.ExecuteQuery(f);

            // GUARDAR LAS PREGUNTAS

            for (CPregunta c : this.Preguntas)
            {
                int mRespuesta = c.isMultiRespuesta()==true?1:0;
                ContentValues cv = new ContentValues();
                cv.put("IdPregunta" ,c.getIdPregunta());
                cv.put("IdEncuesta",c.getIdEncuesta());
                cv.put("Pregunta",c.getPregunta());
                cv.put("Multirespuesta", mRespuesta);
                cv.put("Seleccionado",c.getSeleccionado());
                handler.SaveToDataBase(cv,"SATEA_PREGUNTAS");

                for(CRespuesta v : c.getRespuestas())
                {
                    int idp = v.isIndicadoraPAN()== true? 1:0;
                    ContentValues contentValues= new ContentValues();
                    contentValues.put("IdRespuesta",v.getIdRespuesta());
                    contentValues.put("IdPregunta", v.getIdEncuesta());
                    contentValues.put("Respuesta", v.getRespuesta());
                    contentValues.put("Seleccionado",v.getSeleccionado());
                    contentValues.put("IndicadoraPAN",idp);
                    contentValues.put("OcasionesSeleccionada",v.getOcasionesSeleccionada());
                    contentValues.put("Domicilios", v.getDomicilios());
                    handler.SaveToDataBase(contentValues,"SAETA_RESPUESTAS");
                }

            }


        }
        catch (Exception f)
        {

        }

        return  r;

    }

    @Override
    public String toString()
    {
        return Encuesta;
    }



}
