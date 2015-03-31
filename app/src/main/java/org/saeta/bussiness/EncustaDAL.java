package org.saeta.bussiness;

import android.content.Context;
import android.database.Cursor;

import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;
import org.saeta.entities.CPregunta;
import org.saeta.entities.CRespuesta;
import org.saeta.entities.SaetaLocation;

import java.util.ArrayList;

/**
 * Created by jlopez on 3/19/2015.
 */
public class EncustaDAL {


    private Context context;

    public  EncustaDAL()
    {

    }
    public EncustaDAL(Context c)
    {
        this.context = c;
    }

    public String guardarCancelLocacion (int personaId, int encuestaId, double lat, double longitud)
    {
        String res = null;
        try
        {
            String query ="INSERT INTO ENCUESTAS_CANCELADAS (ID_DETECTADO,IDENCUESTA,LATITUD,LONGITUD) VALUES ("+personaId+","+encuestaId+",'"+String.valueOf(lat)+"','"+String.valueOf(longitud)+"');";
            DataBaseHandler h = new DataBaseHandler(this.context);
            h.ExecuteQuery(query);
            res= "1";
        }
        catch (Exception f )
        {
            res="0";
        }
        return  res;
    }

    public ArrayList<CEncuesta> ObtenerEncuestasRealizadas ()throws  Exception
    {
        ArrayList<CRespuesta> dm= new ArrayList<CRespuesta>();
        ArrayList<CEncuesta> resultArray = null;
        try
        {

            ArrayList<CPersona> arp = ObtenerPersonasEncuestadas();

            if (arp!= null) {
                resultArray= new ArrayList<CEncuesta>();
                for (CPersona p : arp) {
                 CEncuesta encuesta = GetEncuestaPorPersona(p,context);

                    if(encuesta!= null)
                    {
                        int qid = SaetaUtils.tryIntParse(encuesta.IdEncuesta);
                        for(CPregunta q : encuesta.Preguntas)
                        {
                            int xid = q.IdPregunta;
                            int t = p.getIdDetectado();
                            q.Seleccionado= Integer.toString(GetRespuestaSeleccionada(t,qid,xid));
                            q.Respuestas=dm;
                        }
                        encuesta.IdDetectado = Integer.toString(p.getIdDetectado());
                        resultArray.add(encuesta);
                    }
                 }// for
            }
            else
            {

            }
        }
        catch (Exception s)
        {
            throw s;
        }
        return  resultArray;
    }


    private int GetRespuestaSeleccionada (int personaid,int encuestaid, int preguntaid)
    {
        int resp=-1;
        try
        {
            String s= " Select respuesta_id from saeta_usuario_respuesta where encuesta_id = " +
                    +encuestaid+" and persona_id= "+ personaid +" and pregunta_id ="+preguntaid+";";

            DataBaseHandler h= new DataBaseHandler(context);

            Cursor cursor = h.GetCursor(s);

            if(cursor.moveToFirst())
            {
                resp = SaetaUtils.tryIntParse(cursor.getString(0));
            }

        }
        catch (Exception d )
        {
            resp= -1;
        }
        return  resp;
    }





    public CEncuesta GetEncuestaPorPersona (CPersona _persona,Context context)
    {
        ArrayList<CEncuesta> encuestas = new ArrayList<CEncuesta>();
        CEncuesta erresult= null;
         int idEncuesta = _persona.getEncuestaId();
        int personaId =_persona.getIdDetectado();
       try
        {

            ArrayList<CPregunta> preguntas = new ArrayList<CPregunta>();
            ArrayList<CRespuesta> respuestas = new ArrayList<CRespuesta>();

            DataBaseHandler handler = new DataBaseHandler(context);

            String q =" SELECT * FROM SAETA_ENCUESTAS where idencuesta in (select distinct encuesta_id from saeta_usuario_respuesta" +
                    " where encuesta_id ="+idEncuesta+" and persona_id ="+personaId+");";
            Cursor c = handler.GetCursor(q);

            if (c!= null )
            {
                CEncuesta e = null;

                while (c.moveToNext()) {
                    e = new CEncuesta();
                    e.IdEncuesta = c.getString(0);
                    e.Encuesta = c.getString(1);
                    e.IdProceso = c.getString(2);
                    e.IdDetectado = c.getString(3);
                    e.Municipio = c.getString(4);
                    e.Paterno = c.getString(5);
                    e.Materno = c.getString(6);
                    e.Telefono1 = c.getString(7);
                    e.Telefono2 = c.getString(8);
                    e.Telefono3 = c.getString(9);


                    if (e != null) {

                        q = " SELECT * FROM SATEA_PREGUNTAS WHERE IDENCUESTA = " + e.IdEncuesta + ";";

                        Cursor crPreguntas = handler.GetCursor(q);
                        CPregunta pregunta = null;
                        if (crPreguntas != null) {
                            while (crPreguntas.moveToNext()) {
                                pregunta = new CPregunta();
                                pregunta.IdPregunta = crPreguntas.getInt(0);
                                pregunta.IdEncuesta = crPreguntas.getInt(1);
                                pregunta.Pregunta = crPreguntas.getString(2);
                                pregunta.MultiRespuesta = crPreguntas.getString(3) == "1"  && crPreguntas.getString(3)!= null ? true : false;
                                pregunta.Seleccionado = crPreguntas.getString(4);
//                                // Obtencion de las respuestas.
//                                CRespuesta respuesta = new CRespuesta();
//
//                                q = "SELECT * FROM SAETA_RESPUESTAS WHERE IDPREGUNTA = " + pregunta.IdPregunta + ";";
//
//                                Cursor cResp = handler.GetCursor(q);
//
//                                while (cResp.moveToNext()) {
//                                    respuesta = new CRespuesta();
//                                    respuesta.IdRespuesta = cResp.getInt(0);
//                                    respuesta.IdPregunta = cResp.getInt(1);
//                                    respuesta.Respuesta = cResp.getString(2);
//                                    respuesta.Seleccionado = cResp.getString(3);
//                                    respuesta.IndicadoraPAN = cResp.getInt(4) == 1 ? true : false;
//                                    respuesta.OcasionesSeleccionada = SaetaUtils.tryIntParse(cResp.getString(5));
//                                    respuesta.Domicilios = cResp.getString(6);
//                                    respuestas.add(respuesta);
//                                    pregunta.Respuestas.add(respuesta); // agregar respuesta
//                                }
                                e.Preguntas.add(pregunta);// add pregunta
                            }
                        }

                    }// if c!= null
                    encuestas.add(e);
                }// while encuestas
            }

            if(encuestas!= null)
            {
             erresult= encuestas.get(0);
            }
         }
        catch (Exception sqlExcep)
        {
            throw sqlExcep;
        }

       return erresult;
    }



    public CEncuesta GetEncuestaPorPersonaCancelada(CPersona _persona,Context context)
    {
        CEncuesta erresult= null;
        int idEncuesta = _persona.getEncuestaId();
        int personaId =_persona.getIdDetectado();
        try
        {

            ArrayList<CPregunta> preguntas = new ArrayList<CPregunta>();
            ArrayList<CRespuesta> respuestas = new ArrayList<CRespuesta>();

            DataBaseHandler handler = new DataBaseHandler(context);

            String q =" SELECT * FROM SAETA_ENCUESTAS where idencuesta in (select distinct IDENCUESTA from ENCUESTAS_CANCELADAS" +
                    " where ID_DETECTADO ="+personaId+");";
            Cursor c = handler.GetCursor(q);

            if (c!= null )
            {
                CEncuesta e = null;

                while (c.moveToNext()) {
                    e = new CEncuesta();
                    e.IdEncuesta = c.getString(0);
                    e.Encuesta = c.getString(1);
                    e.IdProceso = c.getString(2);
                    e.IdDetectado = c.getString(3);
                    e.Municipio = c.getString(4);
                    e.Paterno = c.getString(5);
                    e.Materno = c.getString(6);
                    e.Telefono1 = c.getString(7);
                    e.Telefono2 = c.getString(8);
                    e.Telefono3 = c.getString(9);
                  erresult= e;
                }// while encuestas
            }

        }
        catch (Exception sqlExcep)
        {
            throw sqlExcep;
        }

        return erresult;
    }


    private ArrayList<CPersona> ObtenerPersonasEncuestadas()
    {
        ArrayList<CPersona> arrayResult = null;
        try
        {

            DataBaseHandler handler = new DataBaseHandler(context);
            String qPersonas =" Select * from saeta_personas where id_detectado in ( "+
                    " select distinct persona_id from saeta_usuario_respuesta);";
            Cursor cr = handler.GetCursor(qPersonas);

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
                pTemp.setEncuestaId(SaetaUtils.tryIntParse(cr.getString(20)));
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
                    pTemp.setEncuestaId(SaetaUtils.tryIntParse(cr.getString(20)));
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


    public String CancelarEncuesta(CPersona p, int status)
    {
        String result = null;
        try
        {
            DataBaseHandler handler= new DataBaseHandler(this.context);
            String query =" UPDATE SAETA_PERSONAS SET ENCUESTA_STATUS ="+ status +" Where Id_Detectado ="+p.getIdDetectado()+";";
            handler.ExecuteQuery(query);
            result="1";
        }
        catch (Exception f)
        {
            result="0";
        }
        return result;
    }

    public ArrayList<CPersona> ObtenerPersonasAEncuestar( Context context) throws  Exception
    {
        ArrayList<CPersona> arrayResult = null;
        try
        {

            DataBaseHandler handler = new DataBaseHandler(context);
            String query = "SELECT * FROM SAETA_PERSONAS WHERE  ID_DETECTADO  not in (SELECT PERSONA_ID FROM SAETA_USUARIO_RESPUESTA" +
                               "  WHERE TERMINADO =1 ) AND ENCUESTA_STATUS NOT IN (1,2,3);";
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
                pTemp.setEncuestaId(SaetaUtils.tryIntParse(cr.getString(20)));
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
                    pTemp.setEncuestaId(SaetaUtils.tryIntParse(cr.getString(20)));
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


    public ArrayList<CPersona> ObtenerPersonasCanceladas( Context context) throws  Exception
    {
        ArrayList<CPersona> arrayResult = null;
        try
        {

            DataBaseHandler handler = new DataBaseHandler(context);
            String query = "SELECT * FROM SAETA_PERSONAS WHERE  " +
                    " ENCUESTA_STATUS IN (2,3);";
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
                pTemp.setEncuestaId(SaetaUtils.tryIntParse(cr.getString(20)));
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
                    pTemp.setEncuestaId(SaetaUtils.tryIntParse(cr.getString(20)));
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

    public SaetaLocation getCancelLocation ( int personaId)
    {
        SaetaLocation loc = new SaetaLocation();
        try
        {
            DataBaseHandler handler = new DataBaseHandler(this.context);
            String q =" Select latitud,longitud from ENCUESTAS_CANCELADAS where id_detectado=" + personaId+";";
            Cursor cr = handler.GetCursor(q);

            if (cr.moveToFirst())
            {
                String x = cr.getString(0);
                String y = cr.getString(1);
                loc.setLatitud(x);
                loc.setLongitud(y);
            }

        }
        catch (Exception d)
        {

        }
        return loc;
    }


}
