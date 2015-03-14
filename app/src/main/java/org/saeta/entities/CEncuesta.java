package org.saeta.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CEncuesta  {

    private String IdEncuesta ;
    private String Encuesta;
    private String IdProceso;
    private String IdDetectado;
    private String Municipio;
    private String Paterno ;
    private String Materno ;
    private String Nombre;
    private String Telefono1;
    private String Telefono2;
    private String Telefono3;
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

    @Override
    public String toString()
    {
        return Encuesta;
    }



}
