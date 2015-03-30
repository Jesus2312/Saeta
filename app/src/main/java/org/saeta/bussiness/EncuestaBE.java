package org.saeta.bussiness;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;
import org.saeta.webservice.WsConsume;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Handler;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by jlopez on 3/19/2015.
 */
public class EncuestaBE {
    public  static File FilesDir;

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

    public  String SubirEncuestas (Context context)
    {

        String result="";
        try {

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(); // excluye los campos que solo se uan en ambito de la app
            ArrayList<CEncuesta> encuestas = new EncustaDAL(context).ObtenerEncuestasRealizadas();
            ArrayList<String> jsonArray = new ArrayList<String>();

            for (CEncuesta e : encuestas) {

                String encuestasJson = gson.toJson(e);
                jsonArray.add(encuestasJson);
                WsConsume consume = new WsConsume("https://api.saeta.org.mx/auditoria/");
                consume.makeHttpsGetCall(e);

                //  consume.wsPostRequest(UserSession.TOKEN_KEY,e);

                // Obtener los archivos guardados de la base

//
//                DataBaseHandler h = new DataBaseHandler(context);
//                Cursor cr;
//
//                cr = h.GetCursor(" select photo_data, audio_data,video_data from encuesta_media " +
//                        "where idencuesta ="+e.IdEncuesta+" and id_detectado ="+ e.IdDetectado+";");
//
//                byte[] audio = null;
//                byte[] video = null;
//                byte[] photo = null;
//
//                if (cr.moveToFirst())
//                {
//                    String  ffoto = cr.getString(0).toString();
//                    String faudio = cr.getString(1).toString();
//                    String fvideo  = cr.getString(2).toString();
//
//
//                    // subir audio
//                    if (faudio!= null) {
////                        FileOutputStream fileOuputStream =
////                                new FileOutputStream(FilesDir + "/AUDIO_ENCUESTA_" + e.IdEncuesta + "_" + e.IdDetectado + ".3gp");
////                        fileOuputStream.write(audio);
////                        fileOuputStream.close();
//
//                        File f = new File(faudio);
//
//                        UploadFtpFiles(f);
//                    }
//
//                    if (fvideo!= null) {
//                        // subir video
////                        FileOutputStream fileOuputStream2 =
////                                new FileOutputStream(FilesDir + "/VIDEO_ENCUESTA_" + e.IdEncuesta + "_" + e.IdDetectado + ".mp4");
////                        fileOuputStream2.write(video);
////                        fileOuputStream2.close();
//
//                       File videol = new File(fvideo);
//
//                        UploadFtpFiles(videol);
//                    }
//
//                    // subir foto
//                    if(ffoto != null) {
//
//                        File fphotol = new File(ffoto);
//
//                                           UploadFtpFiles(fphotol);
//                    }
//
//                }
//               // cr.close();
//
//
//
//
//
//            }
//            result="1";
//        }
//        catch (Exception x)
//        {
//            result = "Error al subir encuestas (E020) detalles: "+x.getMessage();
//        }
//        return  result;
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
