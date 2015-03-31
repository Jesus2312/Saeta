package org.saeta.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.saeta.bussiness.DataBaseHandler;
import org.saeta.bussiness.EncuestaBE;
import org.saeta.bussiness.SaetaUtils;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.AudiotiraStatus;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;
import org.saeta.entities.CPregunta;
import org.saeta.entities.CRespuesta;

import java.util.ArrayList;

public class EncuestaActivity extends ActionBarActivity {

    //Definicion de controles
    Spinner lbEncuestas ;
    EditText tbApellidoPaterno;
    EditText tbApellidoMaterno;
    Spinner lbTelefonos;
    EditText tbMunicipio;
    TextView lbEncuestasPendientes;
    EditText tbNombre;
    Button btIniciarEncuesta ;
    EditText tbColonia;
    Spinner lbPersonas;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        InitializeComponents();
      //  h.execute();
      //  ObtenerEncuestas();

//        lbEncuestas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                 CEncuesta cEncuesta = (CEncuesta) lbEncuestas.getSelectedItem();
//               // obtener las listas de personas correspondientes a la encuesta seleccionada.
//               // ObtenerPersonasAEncuestar(cEncuesta);
//                //ShowUserData(cEncuesta);
//             }
//        @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        lbPersonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<CEncuesta> arr= null;
                CPersona persona = (CPersona) lbPersonas.getSelectedItem();
                // Obtener la encusta
                int eid = persona.getEncuestaId();
                try
                {
                  arr = ObtenerEncuestas(eid);

                     if (arr!= null)
                     {
                         CEncuesta e = arr.get(0);
                         UserSession.T_ENCUESTA= e;
                         btIniciarEncuesta.setEnabled(true);
                     }
                    else
                     {
                         Toast.makeText(EncuestaActivity.this,"Encuesta no obtenida",Toast.LENGTH_LONG).show();
                     }

                }
                catch (Exception f)
                {
                    Toast.makeText(EncuestaActivity.this,"Error al obtener encuesa E(017)",Toast.LENGTH_LONG).show();
                }


                ShowUserData(persona);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private  void ObtenerPersonasAEncuestar()
    {
        try
        {
           ArrayList<CPersona> personas= EncuestaBE.ObtenerPersonasAEncuestar(this);

            if(personas!= null)
            {
                ArrayAdapter<CPersona>  adp  = new ArrayAdapter<CPersona>(this,android.R.layout.simple_spinner_item,personas);
                lbPersonas.setAdapter(adp);
                lbEncuestasPendientes.setText("Encuestas Pendientes : "+ personas.size());
            }
            else
            {
                lbEncuestasPendientes.setText("Encuestas Pendientes : 0");
                Toast.makeText(this,"No se encotraron personas para realizar este tipo de encuesta.",Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception s )
        {
             Toast.makeText(this, "Ha ocurrido un error al obtener lista de personas (E016",Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<CEncuesta> ObtenerEncuestas ( int idEncuesta ) throws Exception
    {
        ArrayList<CEncuesta> encuestas = new ArrayList<CEncuesta>();
        try
        {

            ArrayList<CPregunta> preguntas = new ArrayList<CPregunta>();
            ArrayList<CRespuesta> respuestas = new ArrayList<CRespuesta>();

            DataBaseHandler handler = new DataBaseHandler(EncuestaActivity.this);

            String q =" SELECT * FROM SAETA_ENCUESTAS where idencuesta=" + idEncuesta+";";
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
            // Imprimir el numero de encuestas disponibles .
          //  lbEncuestasPendientes.setText("Encuestas pendientes: "+ encuestas.size());
            if (encuestas.size()<= 0 )
            {
                btIniciarEncuesta.setEnabled(false);
                Toast.makeText(this, "No hay encuestas pendientes, intente descargando catalogo de encuestas", Toast.LENGTH_LONG).show();
            }
            else
            {
                btIniciarEncuesta.setEnabled(true);
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

    public  void PersonaNoContestaClick(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle("Persona no contesta.")
                .setMessage("Desea omitir esta encuesta?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        CPersona persona = (CPersona) lbPersonas.getSelectedItem();

                        if (persona != null) {
                            String r = EncuestaBE.CancelarEncuesta(persona, AudiotiraStatus.NO_CONTESTO,EncuestaActivity.this);
                            if (r.equals("1"))
                            {
                                ObtenerPersonasAEncuestar();
                                Toast.makeText(EncuestaActivity.this,"Encuesta modificada correctamente",Toast.LENGTH_LONG).show();
                            }else
                            {
                                Toast.makeText(EncuestaActivity.this,"Ocurrio un error al modificar la encuesta.",Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(EncuestaActivity.this,"Debe seleccionar una persona de la lista",Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();


    }


    public void DomicilioNoEncontradoClick (View v)
    {

        new AlertDialog.Builder(this)
                    .setTitle("Domicilio no encontado.")
                   .setMessage("Desea omitir esta encuesta por domicilio no encontrado?")
                   .setIcon(android.R.drawable.ic_dialog_alert)
                   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int whichButton) {

                           CPersona persona = (CPersona) lbPersonas.getSelectedItem();

                           if (persona != null) {
                               String r = EncuestaBE.CancelarEncuesta(persona, AudiotiraStatus.DOMICILIO_NO_ENCONTRADO,EncuestaActivity.this);
                               if (r.equals("1"))
                               {
                                   ObtenerPersonasAEncuestar();
                                   Toast.makeText(EncuestaActivity.this,"Encuesta modificada correctamente",Toast.LENGTH_LONG).show();
                                                                  }else
                               {
                                   Toast.makeText(EncuestaActivity.this,"Ocurrio un error al modificar la encuesta.",Toast.LENGTH_LONG).show();
                               }


                           } else {
                               Toast.makeText(EncuestaActivity.this,"Debe seleccionar una persona de la lista",Toast.LENGTH_LONG).show();

                           }
                       }
                   })
                 .setNegativeButton(android.R.string.no, null).show();




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

    private void ShowUserData (CPersona e)
    {

        tbApellidoPaterno.setText(e.getPaterno());
        tbApellidoMaterno.setText(e.getMaterno());
        tbMunicipio.setText(e.getCalle());
        tbColonia.setText(e.getColonia());
        tbNombre.setText(e.getNombre());
        String [] telefonos =  new String[]{ e.getTelefono1(),e.getTelefono2(),e.getTelefono3()};
        ArrayAdapter<String> adapterTelefonos = new ArrayAdapter<String>(EncuestaActivity.this,android.R.layout.simple_spinner_item,telefonos);
        lbTelefonos.setAdapter(adapterTelefonos);


    }



    public  void LocatorClick(View v)
    {
        // Set lat lon

        CPersona p = (CPersona) lbPersonas.getSelectedItem();
        LocatorActivity.lat = String.valueOf(p.getLatitud());
        LocatorActivity.lon = String.valueOf(p.getLongitud());
        LocatorActivity.PersonaId= p.getIdDetectado();
        LocatorActivity.nombrePersona = p.getNombre() +" "+ p.getPaterno() +" "+ p.getMaterno();
        LocatorActivity.domicilioPersona = p.getCalle()+ "# "+p.getNumInterior()  + "Colonia: "+ p.getColonia();
        startActivity(new Intent("org.saeta.locator"));
    }
    private void InitializeComponents()
    {
        try
        {
            tbApellidoPaterno= (EditText) findViewById(R.id.TbApellidoPaterno);
            tbApellidoMaterno = (EditText) findViewById(R.id.TbApellidoMaterno);
            tbMunicipio = (EditText) findViewById(R.id.TbMunicipio);
            lbTelefonos = (Spinner) findViewById(R.id.LbTelefonos);
           // lbEncuestas = (Spinner) findViewById(R.id.LbEncuestas);
            lbEncuestasPendientes = (TextView) findViewById(R.id.LbEncuestasPendientes);
            btIniciarEncuesta = (Button) findViewById(R.id.BtIniciarEncuesta);
            lbPersonas = (Spinner) findViewById(R.id.LbPersonas);
            tbNombre =(EditText) findViewById(R.id.TbNombre);
            tbColonia = (EditText) findViewById(R.id.TbColonia);

            ObtenerPersonasAEncuestar();

         //   ArrayList<CEncuesta> _encuestas= null;

//            try
//            {
//                _encuestas= ObtenerEncuestas();
//            }
//            catch (Exception f)
//            {
//                Toast.makeText(EncuestaActivity.this, "Error al obtener encuestas (E009)",Toast.LENGTH_LONG).show();
//            }

            //MostrarListaEncuestas(_encuestas);



         }
        catch (Exception d)
        {
            Toast.makeText(EncuestaActivity.this,"Error al crear componentes (E004",Toast.LENGTH_LONG).show();
        }
    }


    public void  ClickIniciarEncuesta (View v)
    {
       // UserSession.T_ENCUESTA = null;
        UserSession.T_PERSONA = null;
     //   CEncuesta k = (CEncuesta)lbEncuestas.getSelectedItem();
        CPersona p = (CPersona) lbPersonas.getSelectedItem();
       // UserSession.T_ENCUESTA=k;
        UserSession.T_PERSONA =p;
        Intent intent = new Intent("org.saeta.IniciarEncuesta");
        startActivity(intent);
        finish();
    }

    private void MostrarListaEncuestas (ArrayList<CEncuesta> _encuestas)
    {
           ArrayAdapter<CEncuesta> encuestaArrayAdapter = new ArrayAdapter<CEncuesta>(EncuestaActivity.this, android.R.layout.simple_spinner_item, _encuestas);
                lbEncuestas.setAdapter(encuestaArrayAdapter);
      }


}
