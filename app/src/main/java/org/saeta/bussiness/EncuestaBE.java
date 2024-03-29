package org.saeta.bussiness;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;
import org.saeta.entities.GpsHandler;
import org.saeta.entities.SaetaLocation;
import org.saeta.webservice.WsConsume;

import java.io.File;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by jlopez on 3/19/2015.
 */
public class EncuestaBE {
    public static File FilesDir;


    public static  String CancelarEncuesta (CPersona p , int status ,Context context)
    {
        String gr= null;
        GpsHandler gpsHandler = new GpsHandler(context);
        double lat = gpsHandler.getLatitude();
        double lon = gpsHandler.getLongitude();
        String res = new EncustaDAL(context).CancelarEncuesta(p,status);
        String res1= new EncustaDAL(context).guardarCancelLocacion(p.getIdDetectado(),p.getEncuestaId(),lat,lon);

        if (res.equals("1" )&& res1.equals("1"))
        {
            gr="1";
        }
        else
        {
            gr="0";

        }
        return  gr;
    }

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


    private String SubirEncuestasCanceladas (Context context)
    {
        String r= null;
        try
        {
            ArrayList<CPersona> cPersonasCanceladas = new EncustaDAL().ObtenerPersonasCanceladas(context);
            ArrayList<CEncuesta> encuestasCanceladas = new ArrayList<CEncuesta>();

            if (cPersonasCanceladas!= null) {
                for (CPersona p : cPersonasCanceladas) {
                    // Obterner coordenadas
                    SaetaLocation loc = new EncustaDAL(context).getCancelLocation(p.getIdDetectado());
                    CEncuesta encuesta = new CEncuesta();
                    encuesta = new EncustaDAL().GetEncuestaPorPersonaCancelada(p, context);
                    encuesta.IdDetectado = String.valueOf(p.getIdDetectado());
                    encuesta.Latitud = loc.getLatitud();
                    encuesta.Longitud = loc.getLongitud();
                    encuesta.Status = 2;  // default traerlo del obtener personas canceladas
                    encuestasCanceladas.add(encuesta);
                }


                for (CEncuesta i : encuestasCanceladas) {
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("IdEncuesta", i.getIdEncuesta()));
                    params.add(new BasicNameValuePair("Encuesta", i.Encuesta));
                    params.add(new BasicNameValuePair("IdProceso", i.IdProceso));
                    params.add(new BasicNameValuePair("IdDetectado", i.IdDetectado));
                    params.add(new BasicNameValuePair("Municipio", i.Municipio));
                    params.add(new BasicNameValuePair("Paterno", i.Paterno));
                    params.add(new BasicNameValuePair("Materno", i.Materno));
                    params.add(new BasicNameValuePair("Nombre", i.Nombre));
                    params.add(new BasicNameValuePair("Telefono1", i.Telefono1));
                    params.add(new BasicNameValuePair("Telefono2", i.Telefono2));
                    params.add(new BasicNameValuePair("Telefono3", i.Telefono3));
                    params.add(new BasicNameValuePair("Preguntas", ""));
                    params.add(new BasicNameValuePair("LatitudAuditoria", i.Latitud));
                    params.add(new BasicNameValuePair("LongitudAuditoria", i.Longitud));
                    params.add(new BasicNameValuePair("EstatusAuditoria", String.valueOf(i.Status)));
                    params.add(new BasicNameValuePair("koEstatusAuditoria", String.valueOf(i.Status)));

                    WsConsume consume = new WsConsume("https://api.saeta.org.mx/auditoria/");
                    consume.setParameters(params);
                    consume.doHttpsGetCall(i);

                    DataBaseHandler handler = new DataBaseHandler(context);
                    String updQuery = "UPDATE SAETA_PERSONAS SET UPLOADED_FLAG = 1 WHERE ID_DETECTADO="+ i.getIdDetectado()+";";
                    handler.ExecuteQuery(updQuery);
                }
            }
            return  "1";
        }
        catch (UnknownHostException u)
        {
            r="3";
        }
        catch (Exception d )
        {
            r="0";
        }
     return  r;
    }


    public  String SubirEncuestas (Context context,AsyncTask task)
    {
        Method method;
        String result="";
        try {

            method = task.getClass().getMethod("ReportProgress",String.class);
            method.invoke(task,"Subiendo Auditorias");
            String subirEncuestasRes;
            subirEncuestasRes= SubirEncuestasCanceladas(context);
            if (!subirEncuestasRes.equals("1"))
            {
                result= subirEncuestasRes;
            }
            else
            {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(); // excluye los campos que solo se uan en ambito de la app
                ArrayList<CEncuesta> encuestas = new EncustaDAL(context).ObtenerEncuestasRealizadas();
                ArrayList<String> jsonArray = new ArrayList<String>();


                if (encuestas != null) {
                    for (CEncuesta e : encuestas) {

                        String encuestasJson = gson.toJson(e);
                        jsonArray.add(encuestasJson);
                        WsConsume consume = new WsConsume("https://api.saeta.org.mx/auditoria/");
                        String op= consume.makeHttpsGetCall(e);
                        if (!op.equals("0")) {
                            method.invoke(task,"Subiendo Archivos Multimedia");

                            // Obtener las URls de los archivos multimedia para eliminar.
                            String query =" select PHOTO_DATA, AUDIO_DATA, VIDEO_DATA FROM ENCUESTA_MEDIA WHERE IDENCUESTA ="+ e.getIdEncuesta()+" and ID_DETECTADO ="+e.getIdDetectado()+";";
                            Cursor crMedia = null;
                            DataBaseHandler handler = new DataBaseHandler(context);
                            crMedia = handler.GetCursor(query);

                            if (crMedia!= null) {
                                while (crMedia.moveToNext()) {
                                    String audio= null,video = null, foto = null;
                                    audio = crMedia.getString(1);
                                    video = crMedia.getString(2);
                                    foto = crMedia.getString(0);
                                    String urlm= CUrls.MULTIPART_UPLOAD_URL+e.getIdDetectado()+"/";

                                    if (audio!= null) {
                                        File f = new File(audio);
                                        if (f.exists())
                                        {
                                            urlm =CUrls.MULTIPART_UPLOAD_URL+e.getIdDetectado()+"/1";
                                           WsConsume multipartCall = new WsConsume(urlm);
                                           String fres= multipartCall.makeMultipartHttpsCall(f,CUrls.MIME_AUDIO);
                                            if (fres.contains("correctamente")){
                                                  f.delete();
                                            }


                                        }

                                    }
                                    if (video!= null) {
                                        File f = new File(video);
                                        if (f.exists())
                                        {
                                            urlm =CUrls.MULTIPART_UPLOAD_URL+e.getIdDetectado()+"/2";
                                            WsConsume multipartCall = new WsConsume(urlm);
                                            String fres= multipartCall.makeMultipartHttpsCall(f,CUrls.MIME_VIDEO);
                                            if (fres.contains("correctamente")){
                                                f.delete();
                                            }

                                        }

                                    }

                                    if (foto!= null) {
                                        File f = new File(foto);
                                        if (f.exists())
                                        {
                                            urlm =CUrls.MULTIPART_UPLOAD_URL+e.getIdDetectado()+"/3";
                                            WsConsume multipartCall = new WsConsume(urlm);
                                            String fres= multipartCall.makeMultipartHttpsCall(f,CUrls.MIME_IMAGE);
                                            if (fres.contains("correctamente")){
                                                f.delete();
                                            }
                                        }
                                    }
                                }
                                crMedia.close();
                            }
                            try
                            {
                                // Obtener los el mapa para eliminar si es que existe
                                String filename = Environment.getExternalStorageDirectory().toString()+"/MAPA_PERSONA_"+String.valueOf(e.getIdDetectado())+".png";
                                File fMapa = new File( filename);

                                if (fMapa.exists())
                                {
                                    fMapa.delete();
                                }

                                String updQuery = "UPDATE SAETA_PERSONAS SET UPLOADED_FLAG = 1 WHERE ID_DETECTADO="+ e.getIdDetectado()+";";
                                handler.ExecuteQuery(updQuery);
                            }
                            catch (Exception f )
                            {
                                result="Auditorias se subieron correctamente pero los mapas no pudieron ser eliminados";
                            }
                        }

                        }

                    }
                }
                result= "1";
            }

        catch (Exception d)
        {
            result="0";
        }
        return  result;
    }



}
