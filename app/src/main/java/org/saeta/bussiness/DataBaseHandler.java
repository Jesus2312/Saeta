package org.saeta.bussiness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jesus Lopez on 3/15/2015.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private final static String DATABASE_NAME ="BD_SAETA";
    private final static  int DATABASE_VERSION = 1;

    public  DataBaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        // create table encuestas

        String str1 ="CREATE TABLE SAETA_ENCUESTAS (" +
                " IdEncuesta INTEGER, " +
                " Encuesta TEXT, " +
                " IdProceso  INTEGER, " +
                "  IdDetectado  INTEGER, " +
                "  Municipio  TEXT, " +
                "  Paterno  TEXT, " +
                "  Materno  TEXT," +
                "  Nombre   TEXT, " +
                "  Telefono1  TEXT, " +
                "  Telefono2  TEXT, " +
                "  Telefono3  INTEGER," +
                "  Terminada INTEGER DEFAULT 0 " +
                " ) ";

        // create table preguntas
        String str2 = "CREATE TABLE  SATEA_PREGUNTAS  ( " +
                "  IdPregunta  INTEGER, " +
                "  IdEncuesta  INTEGER, " +
                "  Pregunta  TEXT, " +
                "  MultiRespuesta  INTEGER, " +
                "  Seleccionado  TEXT " +
                ");";


        // create table respuestas
        String str3 ="CREATE TABLE SAETA_RESPUESTAS ( " +
                "  IdRespuesta  INTEGER, " +
                "  IdPregunta  INTEGER, " +
                "  Respuesta  TEXT, " +
                "  Seleccionado  TEXT, " +
                "  IndicadoraPAN  INTEGER, " +
                "  OcasionesSeleccionada  INTEGER, " +
                "  Domicilios  TEXT " +
                ");";

        String str4="CREATE TABLE  SAETA_USUARIO_RESPUESTA  ( " +
                "  REG_ID  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "  TOKEN_NUMBER  INTEGER NOT NULL, " +
                "  ENCUESTA_ID  INTEGER, " +
                "  PERSONA_ID  INTEGER, " +
                "  PREGUNTA_ID  INTEGER, " +
                "  RESPUESTA_ID  INTEGER, " +
                "  FECHA_RESPUESTA  TEXT, " +
                "  TERMINADO  INTEGER " +
                ");";
        String str5 =" CREATE TABLE ENCUESTA_MEDIA (" +
                "IDENCUESTA INTEGER , ID_DETECTADO INTEGER ," +
                "PHOTO_DATA BLOB, AUDIO_DATA BLOB," +
                "VIDEO_DATA BLOB" +
                ");";
        String str6= " CREATE TABLE SAETA_PERSONAS ( "+
                  " CALLE TEXT, " +
                   "CODIGO_POSTAL TEXT," +
                " COLONIA TEXT, " +
                "DISTRITO_FEDERAL INTEGER," +
                " DISTRITO_LOCAL INTEGER ," +
                " ESTADO TEXT , "+
                 " ID_DETECTADO INTEGER PRIMARY KEY, " +
                " LATITUD TEXT, " +
                " LONGITUD TEXT, " +
                " MANZANA TEXT," +
                " APELLIDO_MATERNO TEXT, " +
                " MUNICIPIO TEXT, " +
                " NOMBRE TEXT, " +
                " NUM_EXTERIROR TEXT, " +
                " NUM_INTERIOR TEXT , " +
                " APELLIDO_PATERNO TEXT, " +
                " SECCION TEXT, " +
                " TELEFONO_1 TEXT, " +
                " TELEFONO_2 TEXT, " +
                " TELEFONO_3 TEXT, IDENCUESTA NUMBER );";

        try
        {
             db.execSQL(str1);
             db.execSQL(str2);
             db.execSQL(str3);
             db.execSQL(str4);
             db.execSQL(str5);
             db.execSQL(str6);
        }
        catch (Exception f)
        {

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public String ExecuteQuery (String query)
    {
       String t;
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);
            t="1";
        }
        catch (Exception f)
        {
            t= "(E007)";
        }
        return  t;
    }

    public String   SaveToDataBase(ContentValues values,String tableName)
    {
        String res = null;
        try
        {
            SQLiteDatabase sq  = this.getWritableDatabase();
            ContentValues cv = values;
            sq.insert(tableName,null,cv);
            res="1";
        }
        catch (Exception g)
        {
            res= "Error al guardar transaccion (E006)";

        }
        return  res;
    }

    public Cursor GetCursor(String query )
    {
        Cursor res = null;
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            res= db.rawQuery(query,null);
        }
        catch (Exception f)
        {
            res= null;
        }
        return res;
    }
}
