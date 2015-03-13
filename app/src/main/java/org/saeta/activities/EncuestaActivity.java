package org.saeta.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.webservice.WsConsume;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EncuestaActivity extends ActionBarActivity {

    //Definicion de controles
    Spinner lbEncuestas ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);

        new asyncWsHelper(0).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encuesta, menu);
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

    public CEncuesta[] GetEncuestas()
    {
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

          }
        catch (Exception e )
        {
          return  null;
        }
        finally
        {
            if(connection!= null)
            connection.disconnect();
        }
        return  encuestas;
    }

    class  asyncWsHelper extends AsyncTask<String,String,String>
    {
        int _actionToTake=-1;
         CEncuesta[] encuestas;


        public asyncWsHelper(int action)
        {
             _actionToTake= action;
        }



        @Override
        protected String doInBackground(String... params) {
            String opResult= null;
        switch (_actionToTake)
        {

            case 0:
            // obtener los datos de la encuesta
             encuestas=GetEncuestas();
                break;
        }
            return opResult;
        }

        @Override
        protected  void  onPostExecute(final String result)
        {

            switch (_actionToTake)
            {
                case 0:
                    MostrarListaEncuestas();
                    break;
            }

        }


        // Metodos de utileria privados
         private void MostrarListaEncuestas ()
        {
            if (this.encuestas.length>0)
            {
                ArrayAdapter<CEncuesta> encuestaArrayAdapter = new ArrayAdapter<CEncuesta>(EncuestaActivity.this,android.R.layout.simple_spinner_item,encuestas);
                lbEncuestas.setAdapter(encuestaArrayAdapter);

            }
            else
            {
                Toast.makeText(EncuestaActivity.this,"No se encontraron encuestas pendientes.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
