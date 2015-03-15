package org.saeta.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    EditText tbApellidoPaterno;
    EditText tbApellidoMaterno;
    Spinner lbTelefonos;
    EditText tbMunicipio;
    ////

    asyncWsHelper h = new asyncWsHelper(0);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        InitializeComponents();
         h.execute();

        lbEncuestas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (h.getStatus() != AsyncTask.Status.RUNNING) {
                    CEncuesta cEncuesta = (CEncuesta) lbEncuestas.getSelectedItem();
                    ShowUserData(cEncuesta);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void ShowUserData (CEncuesta e)
    {

        tbApellidoPaterno.setText(e.getApellidoPaterno());
        tbApellidoPaterno.setText(e.getMaterno());
        tbMunicipio.setText(e.getMunicipio());

        // mostrar telefonos ;

        String [] telefonos =  new String[]{ e.getTelefono1(),e.getTelefono2(),e.getTelefono3()};
        ArrayAdapter<String> adapterTelefonos = new ArrayAdapter<String>(EncuestaActivity.this,android.R.layout.simple_spinner_item,telefonos);
        lbTelefonos.setAdapter(adapterTelefonos);


    }

    private void InitializeComponents()
    {
        try
        {
            tbApellidoPaterno= (EditText) findViewById(R.id.TbApellidoPaterno);
            tbApellidoMaterno = (EditText) findViewById(R.id.TbApellidoMaterno);
            tbMunicipio = (EditText) findViewById(R.id.TbMunicipio);
            lbTelefonos = (Spinner) findViewById(R.id.LbTelefonos);
            lbEncuestas = (Spinner) findViewById(R.id.LbEncuestas);

         }
        catch (Exception d)
        {
            Toast.makeText(EncuestaActivity.this,"Error al crear componentes (E004",Toast.LENGTH_LONG).show();
        }
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


    public void  ClickIniciarEncuesta (View v)
    {
        UserSession.T_ENCUESTA = null;
        CEncuesta k = (CEncuesta)lbEncuestas.getSelectedItem();
        UserSession.T_ENCUESTA=k;
        Intent intent = new Intent("org.saeta.IniciarEncuesta");
        startActivity(intent);
    }


    class  asyncWsHelper extends AsyncTask<String,String,String>
    {
        int _actionToTake=-1;
         CEncuesta[] encuestas;


        public asyncWsHelper(int action)
        {
             _actionToTake= action;
        }


//f
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
                try {

                    for(CEncuesta v : encuestas)
                    {
                        v.saveToDataBase(EncuestaActivity.this);
                    }

                     ArrayAdapter<CEncuesta> encuestaArrayAdapter = new ArrayAdapter<CEncuesta>(EncuestaActivity.this, android.R.layout.simple_spinner_item, encuestas);
                    lbEncuestas.setAdapter(encuestaArrayAdapter);
                }
                catch (Exception f)
                {
                    f.printStackTrace();
                }

            }
            else
            {
                Toast.makeText(EncuestaActivity.this,"No se encontraron encuestas pendientes.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
