package org.saeta.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.Expose;

import org.saeta.bussiness.DataBaseHandler;
import org.saeta.bussiness.UserSession;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jlopez on 3/13/2015.
 */
public class CEncuesta {

    public final static String TABLE_NAME = "SAETA_ENCUESTAS";

    @Expose
    public String IdEncuesta = "";
    @Expose
    public String Encuesta = "";
    @Expose
    public String IdDetectado = "";
    @Expose
    public String IdProceso = "";
    @Expose
    public String Municipio = "";
    @Expose
    public String Paterno = "";
    @Expose
    public String Materno = "";
    @Expose
    public String Nombre = "";
    @Expose
    public String Telefono1 = "";
    @Expose
    public String Telefono2 = "";
    @Expose
    public String Telefono3 = "";
    @Expose
    public ArrayList<CPregunta> Preguntas = new ArrayList<CPregunta>();
    public String AudioUrl = "";
    public String VideoUrl = "";
    public String PhotoUrl = "";
    public String Latitud = "";
    public String Longitud = "";

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int Status ;


    public ArrayList<CPregunta> getPreguntas() {
        return Preguntas;
    }

    public CEncuesta() {

    }

    public String getTelefono3() {
        if (Telefono3 != null && Telefono2 != "") {
            return Telefono3;
        } else {
            return "N/A";
        }
    }

    public String getTelefono2() {
        if (Telefono2 != null && Telefono2 != "") {
            return Telefono2;
        } else {
            return "N/A";
        }
    }

    public String getTelefono1() {
        if (Telefono1 != null && Telefono1 != "") {
            return Telefono1;
        } else {
            return "N/A";
        }
    }

    public String getNombre() {
        return Nombre;
    }

    public String getMaterno() {
        return Materno;
    }

    public String getApellidoPaterno() {
        return Paterno;
    }

    public String getMunicipio() {
        return Municipio;
    }

    public String getIdDetectado() {
        return IdDetectado;
    }


    public String getIdEncuesta() {
        return IdEncuesta;
    }

    public String getEncuesta() {
        return Encuesta;
    }

    public String getIdProceso() {
        return IdProceso;
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("IdEncuesta", 1);
        cv.put("Encuesta", getEncuesta());
        cv.put("IdProceso", getIdProceso());
        cv.put("IdDetectado", getIdDetectado());
        cv.put("Paterno", getApellidoPaterno());
        cv.put("Materno", getMaterno());
        cv.put("Nombre", getNombre());
        cv.put("Telefono1", getTelefono1());
        cv.put("Telefono2", getTelefono2());
        cv.put("Telefono3", getTelefono3());
        return cv;

    }

    public String GuardarRespuestas(Context c, CPersona personaEncuestada) {
        String res = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);
        try {
            DataBaseHandler handler = new DataBaseHandler(c);
            for (CPregunta p : Preguntas) {
                ContentValues cv = new ContentValues();
                cv.put("TOKEN_NUMBER", UserSession.TOKEN_KEY);
                cv.put("ENCUESTA_ID", this.IdEncuesta);
                cv.put("PERSONA_ID", personaEncuestada.getIdDetectado());
                cv.put("PREGUNTA_ID", p.getIdPregunta());
                cv.put("RESPUESTA_ID", p.getSeleccionado());
                cv.put("FECHA_RESPUESTA", strDate);
                cv.put("TERMINADO", 1);
                cv.put("R_LATITUD", this.Latitud);
                cv.put("R_LONGITUD", this.Longitud);
                handler.SaveToDataBase(cv, "SAETA_USUARIO_RESPUESTA");
            }
            // guardar los archivos enbebidos en storage local
            SaveMedia(c, personaEncuestada.getIdDetectado());
            res = "1";
        } catch (Exception f) {
            res = "Error al guardar resultados encuesta (E011)";
        }
        return res;
    }


    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    private void SaveMedia(Context c, int idDetectado) throws Exception {
        try {
            byte[] audio = null;
            byte[] video = null;
            byte[] foto = null;
            // guardar audio

            if (this.VideoUrl != "" || this.AudioUrl != "" || this.PhotoUrl!="") {
//                // coneitene audio file
//                if (this.AudioUrl != "") {
//                    audio = readFile(this.AudioUrl);
////                    File file = new File(this.AudioUrl);
////                    InputStream fis = new FileInputStream(file);
////                    BufferedInputStream bis = new BufferedInputStream(fis, 32768);
////                    ByteArrayBuffer baf = new ByteArrayBuffer(32768);
////                    int current = 0;
////                    while ((current = bis.read()) != -1) {
////                        baf.append((byte) current);
////                    }
////                    audio = baf.toByteArray();
//
//                }
//
//                if (this.PhotoUrl!="")
//                {
//                    foto= readFile(this.PhotoUrl);
//                }
//
//                if (this.VideoUrl != "") {

                  //  video = readFile(this.VideoUrl);
//                        //File file = new File(p.VideoUrl);
//                        FileInputStream fin= new FileInputStream(this.VideoUrl);
//                       // InputStream fis = new FileInputStream(file);
//                        BufferedInputStream bis = new BufferedInputStream(fin,32768);
//                        ByteArrayBuffer baf = new ByteArrayBuffer(32768);
//                        int current = 0;
//                        while ((current = bis.read()) != -1) {
//                            baf.append((byte) current);
//                        }
//                        video = baf.toByteArray();


                ContentValues cv = new ContentValues();
                cv.put("IDENCUESTA", this.getIdEncuesta());
                cv.put("ID_DETECTADO", idDetectado);
                cv.put("PHOTO_DATA",this.PhotoUrl );
                cv.put("AUDIO_DATA", this.AudioUrl);
                cv.put("VIDEO_DATA", this.VideoUrl);

                DataBaseHandler h = new DataBaseHandler(c);
                h.SaveToDataBase(cv, "ENCUESTA_MEDIA");
            }

        } catch (Exception f) {
            throw f;

        }
    }

    public String saveToDataBase(Context context) {
        String r = "";
        try {
            int RegExist = -1;
            DataBaseHandler handler = new DataBaseHandler(context);
            String query = "SELECT COUNT (*) FROM SAETA_ENCUESTAS WHERE IDENCUESTA =" + this.IdEncuesta + ";";
            Cursor cr = handler.GetCursor(query);

            if (cr != null) {
                if (cr.moveToFirst()) {
                    RegExist = cr.getInt(0);
                }
            }


            if (RegExist <= 0) {

                //GUARDAR LA ENCUESTA
                StringBuilder qry = new StringBuilder();

                qry.append(" INSERT INTO SAETA_ENCUESTAS ( IDENCUESTA,ENCUESTA,IDPROCESO,IDDETECTADO,MUNICIPIO,PATERNO,MATERNO,NOMBRE,TELEFONO1,TELEFONO2,TELEFONO3) VALUES ( ")
                        .append(this.IdEncuesta).append(" ,'").append(this.Encuesta).append("', ").append(IdProceso)
                        .append(" , ").append(IdDetectado).append(" , '").append(Municipio).append("' , '").append(Paterno).append("' , '").append(Materno).append("','").append(Nombre)
                        .append("','").append(Telefono1).append("','").append(Telefono2).append("','").append(Telefono3).append("')");

                String f = qry.toString();


                handler.ExecuteQuery(f);

                // GUARDAR LAS PREGUNTAS

                for (CPregunta c : this.Preguntas) {
                    int mRespuesta = c.isMultiRespuesta() == true ? 1 : 0;
                    ContentValues cv = new ContentValues();
                    cv.put("IdPregunta", c.getIdPregunta());
                    cv.put("IdEncuesta", c.getIdEncuesta());
                    cv.put("Pregunta", c.getPregunta());
                    cv.put("Multirespuesta", mRespuesta);
                    cv.put("Seleccionado", c.getSeleccionado());
                    handler.SaveToDataBase(cv, "SATEA_PREGUNTAS");

                    for (CRespuesta v : c.getRespuestas()) {
                        int idp = v.isIndicadoraPAN() == true ? 1 : 0;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("IdRespuesta", v.getIdRespuesta());
                        contentValues.put("IdPregunta", v.IdPregunta);
                        contentValues.put("Respuesta", v.getRespuesta());
                        contentValues.put("Seleccionado", v.getSeleccionado());
                        contentValues.put("IndicadoraPAN", idp);
                        contentValues.put("OcasionesSeleccionada", v.getOcasionesSeleccionada());
                        contentValues.put("Domicilios", v.getDomicilios());
                        handler.SaveToDataBase(contentValues, "SAETA_RESPUESTAS");
                    }

                }
            }
            r = "1";

        } catch (Exception f) {

        }

        return r;

    }

    @Override
    public String toString() {
        return Encuesta;
    }


}
