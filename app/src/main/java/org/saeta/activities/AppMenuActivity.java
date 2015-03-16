package org.saeta.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.saeta.activities.R;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.webservice.WsConsume;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private String  DescargarEncuestas()
    {

        String msg="";
        Debug.waitForDebugger();
        CEncuesta[] encuestas;
        String result;
        HttpURLConnection connection =null;
        try
        {
            String uri="http://api.saeta.org.mx/Auditoria";
            URL url = new URL(uri);
            String token = UserSession.TOKEN_KEY;
            connection = (HttpURLConnection)url.openConnection();
            String strCred ="Bearer " +token;
            connection.setRequestProperty("Authorization",strCred);
            InputStream s = connection.getInputStream();
            result = WsConsume.convertInputStreamToString(s);
            Gson gson = new GsonBuilder().create();
            encuestas = gson.fromJson(result,CEncuesta[].class);
            msg="1";

            try
            {
               GuardarEncuestas(encuestas);
            }
            catch (Exception f)
            {
                msg="Error al guardar catalogo de encuestas (E009)";
            }

        }
        catch (Exception f)
        {
            msg="Error al descargar catalogo de encuestas (E009)";

        }

        return msg;
    }

    private  void GuardarEncuestas(CEncuesta[] encuestas)
    {
        for(CEncuesta d : encuestas)
        {
            d.saveToDataBase(AppMenuActivity.this);
        }
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
            dialog.setMessage("Descargando catalogo de encuestas porfavor espere...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

        }
        @Override
        protected String doInBackground(String... params) {
            switch (_action)
            {
                case  0:
                   msg= DescargarEncuestas();
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

                    }
                    else
                    {
                        Toast.makeText(AppMenuActivity.this,"Catalogo descargado correctamente",Toast.LENGTH_LONG).show();
                    }
                 break;

            }

        }
    }
}
