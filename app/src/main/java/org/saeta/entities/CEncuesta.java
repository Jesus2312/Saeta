package org.saeta.entities;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CEncuesta {

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
        return Telefono3;
    }

    public  String getTelefono2()
    {
        return Telefono2;
    }

    public  String getTelefono1 ()
    {
        return Telefono1;
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


}
