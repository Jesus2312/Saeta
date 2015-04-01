package org.saeta.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.saeta.bussiness.CUrls;
import org.saeta.bussiness.DataBaseHandler;
import org.saeta.bussiness.EncuestaBE;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;
import org.saeta.webservice.WsConsume;

import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class AppMenuActivity extends ActionBarActivity {

    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void  IniciarEncuestaClick (View v)
    {
        startActivity(new Intent("org.saeta.EncuestaActivity"));
    }

    public  void DescargaEncuestasClick(View v)
    {
       new asyncHelper(0).execute();
    }


    private ArrayList<CPersona> DescargarCatalogoPersonas (CEncuesta [] encuestas) throws  Exception
    {
        ArrayList<CPersona> listaPersonas = new ArrayList<CPersona>();
        String msg= "";
        try
        {
             //////Debug.waitForDebugger();
             //Descargar catalogo de persnas por encuestas.
            String ub = CUrls.CATALOGO_PERSONAS_URL;
            for (CEncuesta e : encuestas)
            {
               CPersona [] temp  ;
              String url = CUrls.CATALOGO_PERSONAS_URL + e.getIdEncuesta();
            //  URL uri = new URL(url);
              WsConsume consume = new WsConsume();
             String result=  consume.makeHttpsGetCall(url);
//              String token = UserSession.TOKEN_KEY;
//              HttpURLConnection con = (HttpURLConnection) uri.openConnection();
//              String strCred = "Bearer "+ token;
//              con.setRequestProperty("Authorization",strCred);
//              InputStream s = con.getInputStream();
//              String result=WsConsume.convertInputStreamToString(s);
            Gson gson = new GsonBuilder().create();
              temp = gson.fromJson(result,CPersona[].class);
              for (CPersona  p : temp)
              {
                  if (!listaPersonas.contains(p))
                  {
                      p.setEncuestaId(Integer.parseInt(e.getIdEncuesta()));
                      listaPersonas.add(p);
                  }
              }
           }
        }
        catch (Exception d )
        {
            throw  d;
        }
        return listaPersonas;
    }

    public void SubirEncuestasClick(View c)
    {
        new asyncHelper(1).execute();
    }


    private String  DescargarEncuestas()
    {

        String msg="";
       ////Debug.waitForDebugger();
        CEncuesta[] encuestas;
        String result;
        HttpURLConnection connection =null;
        try
        {
            WsConsume consume = new WsConsume("https://api.saeta.org.mx/Auditoria");
            String res = consume.makeHttpsPostCall();
//            String uri="http://api.saeta.org.mx/Auditoria";
//            URL url = new URL(uri);
//            String token = UserSession.TOKEN_KEY;
//            connection = (HttpURLConnection)url.openConnection();
//            String strCred ="Bearer " +token;
//            connection.setRequestProperty("Authorization",strCred);
//            InputStream s = connection.getInputStream();
           // result = WsConsume.convertInputStreamToString(s);
            Gson gson = new GsonBuilder().create();
            encuestas = gson.fromJson(res,CEncuesta[].class);
            msg="1";

            try
            {
               GuardarEncuestas(encuestas);
            }
            catch (Exception f)
            {
                msg="Error al guardar catalogo de encuestas (E009)";
            }

            ArrayList<CPersona> personas= null;
            try
            {
                  personas =DescargarCatalogoPersonas(encuestas);
            }
            catch (Exception d )
            {
               msg ="Error al descargar catalogo de personas (E012)";
            }

            try
            {
                if (personas.size()>0)
                {
                    GuardarCatalogoPersonas(personas);
                }

            }
            catch (Exception d)
            {
                msg ="Error al guardar  catalogo de personas (E013)";
            }

        }
        catch (UnknownHostException u)
        {
            msg ="Error al descargar catalogo, verifique conexion a internet.";
        }
        catch (Exception f)
        {
            msg="Error al descargar catalogo de encuestas (E009)";

        }

        return msg;
    }

    @Override
    public void onResume ()
    {
        super.onResume();
        if (UserSession.SAVE_OK_FLAG==1)
        {
            Toast.makeText(this, "Encuesta guardada correctamente ",Toast.LENGTH_LONG).show();
            UserSession.SAVE_OK_FLAG =-1;
        }
        else  if (UserSession.SAVE_OK_FLAG ==0)
        {
            Toast.makeText(this, "Error al guardar encuesta ",Toast.LENGTH_LONG).show();
            UserSession.SAVE_OK_FLAG = -1;
        }
    }

    private  void GuardarEncuestas(CEncuesta[] encuestas)
    {
        for(CEncuesta d : encuestas)
        {
            d.saveToDataBase(AppMenuActivity.this);
        }
    }


    @Override
    public   void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

    }
    private String PostRespuestas ()
    {
        //cagado
       ////Debug.waitForDebugger();
        EncuestaBE.FilesDir = getFilesDir();
        String r= new EncuestaBE().SubirEncuestas(AppMenuActivity.this);
        return  r;
     }

    private void GuardarCatalogoPersonas (ArrayList<CPersona> personas)
    {
        try
        {
            //Debug.waitForDebugger();
            DataBaseHandler handler = new DataBaseHandler(this);

            String deleteQuery =" DELETE FROM SAETA_PERSONAS;";
            handler.ExecuteQuery(deleteQuery);

            for(CPersona s : personas)
            {
                //int rExist =SaetaUtils.QueryExistByCount("SELECT COUNT (*) FROM SAETA_PERSONAS WHERE ID_DETECTADO ="+Integer.toString(s.getIdDetectado())+";",this);
               // if ( rExist> 0 ) {
                    String queryInsert = " INSERT INTO SAETA_PERSONAS VALUES ('" + s.getCalle() + "','" + s.getCodigoPostal() + "','" + s.getColonia() + "', " +
                            s.getDistritoFederal() + "," + s.getDistritoLocal() + ",'" + s.getEstado() + "'," + s.getIdDetectado() + " ,'" + s.getLatitud() + "','" + s.getLongitud() + "','" +
                            s.getManzana() + "','" + s.getMaterno() + "','" + s.getMunicipio() + "','" + s.getNombre() + "','" + s.getNumExterior() + "','" + s.getNumInterior() + "','" + s.getPaterno() + "','" +
                            s.getSeccion() + "','" + s.getTelefono1() + "','" + s.getTelefono2() + "','" + s.getTelefono3() + "' ," + s.getEncuestaId() + ","+0+");";

                    handler.ExecuteQuery(queryInsert);
                //}
            }
        }
        catch (Exception f ){ throw  f;}
    }
    class asyncHelper extends AsyncTask<String,String,String >
    {

        private  int _action;
        private String msg;

        public asyncHelper(int a)
        {
            _action= a;
        }


        @Override
        protected void onPreExecute() {
            dialog= new ProgressDialog(AppMenuActivity.this);
            switch (_action)
            {
                case  0:
                    dialog.setMessage("Descargando catalogo de encuestas porfavor espere...");
                    break;
                case 1:
                    dialog.setMessage("Subiendo auditorias porfavor espere...");
            }
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

        }
        @Override
        protected String doInBackground(String... params) {
            //Debug.waitForDebugger();
            switch (_action)
            {
                case  0:
                   msg= DescargarEncuestas();
                    break;

                case 1:
                   msg= PostRespuestas();
                    break;

            }

            return null;
        }

        @Override
        protected void onPostExecute(String f)
        {
            switch (_action)
            {
                case  0:
                      dialog.dismiss();

                    if (msg!= "1")
                    {
                        Toast.makeText(AppMenuActivity.this,"Error al descargar catalogo detalles: "+ msg,Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(AppMenuActivity.this,"Catalogo descargado correctamente",Toast.LENGTH_LONG).show();
                    }
                 break;
                case 1:
                    dialog.dismiss();
                    if( msg=="1") {
                        Toast.makeText(AppMenuActivity.this, "Auditorias subidas correctamente", Toast.LENGTH_LONG).show();
                    }
                    else if (msg=="2")
                    {
                        Toast.makeText(AppMenuActivity.this,"Error al subir auditorias omitidas",Toast.LENGTH_LONG).show();
                    }
                    else if (msg=="3")
                    {
                        Toast.makeText(AppMenuActivity.this,"Error al subir auditorias, verifique conexion a internet",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(AppMenuActivity.this,"Error: "+ msg,Toast.LENGTH_LONG).show();
                    break;
 }
        }
    }
}
