package org.saeta.bussiness;

import android.content.Context;
import android.database.Cursor;

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
import java.net.UnknownHostException;
import java.util.ArrayList;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by jlopez on 3/19/2015.
 */
public class EncuestaBE {
    public  static File FilesDir;


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


    public  String UploadFtpFiles (File fileName)
    {
        String res ="";
         final String FTP_HOST= "webapps.com.mx";
         final String FTP_USER = "saeta@webapps.com.mx";
         final String FTP_PASS  ="Nn200231803!";

         FTPClient client = new FTPClient();
         try {


            client.connect(FTP_HOST,21);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
           // client.changeDirectory("/upload/");
            client.upload(fileName,new MyTransferListener());
         res="1";
        } catch (Exception e) {
             res= e.getMessage();
            e.printStackTrace();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        finally {
          try
          {
              client.disconnect(true);
          }
          catch (Exception f){}
         }
return  res;
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


    public  String SubirEncuestas (Context context)
    {

        String result="";
        try {

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

                            // Obtener las URls de los archivos multimedia para eliminar.
                            String query =" select PHOTO_DATA, AUDIO_DATA, VIDEO_DATA FROM ENCUESTA_MEDIA WHERE IDENCUESTA ="+ e.getIdEncuesta()+";";
                            Cursor crMedia = null;
                            DataBaseHandler handler = new DataBaseHandler(context);
                            crMedia = handler.GetCursor(query);

                            if (crMedia!= null) {
                                while (crMedia.moveToNext()) {
                                    String audio= null,video = null, foto = null;
                                    audio = crMedia.getString(1);
                                    video = crMedia.getString(2);
                                    foto = crMedia.getString(0);

                                    if (audio!= null) {
                                        File f = new File(audio);
                                        if (f.exists())
                                        {               f.delete();

                                        }

                                    }
                                    if (video!= null) {
                                        File f = new File(video);
                                        if (f.exists())
                                        {               f.delete();

                                        }

                                    }

                                    if (foto!= null) {
                                        File f = new File(foto);
                                        if (f.exists())
                                        {               f.delete();

                                        }

                                    }
                                }
                                crMedia.close();
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


    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {


        }

        public void transferred(int length) {


        }

        public void completed() {


        }

        public void aborted() {


        }

        public void failed() {


        }

    }

}
