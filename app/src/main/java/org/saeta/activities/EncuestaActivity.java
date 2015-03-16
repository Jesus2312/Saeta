package org.saeta.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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

import org.apache.http.auth.NTUserPrincipal;
import org.saeta.bussiness.DataBaseHandler;
import org.saeta.bussiness.SaetaUtils;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPregunta;
import org.saeta.entities.CRespuesta;
import org.saeta.webservice.WsConsume;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class EncuestaActivity extends ActionBarActivity {

    //Definicion de controles
    Spinner lbEncuestas ;
    EditText tbApellidoPaterno;
    EditText tbApellidoMaterno;
    Spinner lbTelefonos;
    EditText tbMunicipio;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        InitializeComponents();
      //  h.execute();
      //  ObtenerEncuestas();

        lbEncuestas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    CEncuesta cEncuesta = (CEncuesta) lbEncuestas.getSelectedItem();
                    ShowUserData(cEncuesta);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private ArrayList<CEncuesta> ObtenerEncuestas () throws Exception
    {
        ArrayList<CEncuesta> encuestas = new ArrayList<CEncuesta>();
        try
        {

            ArrayList<CPregunta> preguntas = new ArrayList<CPregunta>();
            ArrayList<CRespuesta> respuestas = new ArrayList<CRespuesta>();

            DataBaseHandler handler = new DataBaseHandler(EncuestaActivity.this);

            String q = "SELECT * FROM SAETA_ENCUESTAS E " +
                    " WHERE IDENCUESTA  NOT IN (SELECT ENCUESTA_ID FROM " +
                    " SAETA_USUARIO_RESPUESTA WHERE TERMINADO =1 );";
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

                                // Obtencion de las respuestas.
                                CRespuesta respuesta = new CRespuesta();

                                q = "SELECT * FROM SAETA_RESPUESTAS WHERE IDPREGUNTA = " + pregunta.IdPregunta + ";";

                                Cursor cResp = handler.GetCursor(q);

                                while (cResp.moveToNext()) {
                                    respuesta = new CRespuesta();
                                    respuesta.IdRespuesta = cResp.getInt(0);
                                    respuesta.IdPregunta = cResp.getInt(1);
                                    respuesta.Respuesta = cResp.getString(2);
                                    respuesta.Seleccionado = cResp.getString(3);
                                    respuesta.IndicadoraPAN = cResp.getInt(4) == 1 ? true : false;
                                    respuesta.OcasionesSeleccionada = SaetaUtils.tryIntParse(cResp.getString(5));
                                    respuesta.Domicilios = cResp.getString(6);
                                    respuestas.add(respuesta);
                                    pregunta.Respuestas.add(respuesta); // agregar respuesta
                                }
                               e.Preguntas.add(pregunta);// add pregunta
                            }
                        }

                     }// if c!= null
                    encuestas.add(e);
                }// while encuestas
            }
        }
        catch (Exception sqlExcep)
        {
            throw sqlExcep;
        }
        return encuestas;
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

            ArrayList<CEncuesta> _encuestas= null;

            try
            {
                _encuestas= ObtenerEncuestas();
            }
            catch (Exception f)
            {
                Toast.makeText(EncuestaActivity.this, "Error al obtener encuestas (E009)",Toast.LENGTH_LONG).show();
            }

            MostrarListaEncuestas(_encuestas);

         }
        catch (Exception d)
        {
            Toast.makeText(EncuestaActivity.this,"Error al crear componentes (E004",Toast.LENGTH_LONG).show();
        }
    }


    public void  ClickIniciarEncuesta (View v)
    {
        UserSession.T_ENCUESTA = null;
        CEncuesta k = (CEncuesta)lbEncuestas.getSelectedItem();
        UserSession.T_ENCUESTA=k;
        Intent intent = new Intent("org.saeta.IniciarEncuesta");
        startActivity(intent);
    }

    private void MostrarListaEncuestas (ArrayList<CEncuesta> _encuestas)
    {

                ArrayAdapter<CEncuesta> encuestaArrayAdapter = new ArrayAdapter<CEncuesta>(EncuestaActivity.this, android.R.layout.simple_spinner_item, _encuestas);
                lbEncuestas.setAdapter(encuestaArrayAdapter);

      }


}
