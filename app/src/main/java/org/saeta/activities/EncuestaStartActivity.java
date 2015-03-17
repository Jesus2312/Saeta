package org.saeta.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.saeta.activities.R;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPregunta;
import org.saeta.entities.CRespuesta;

import java.util.ArrayList;
import java.util.ListIterator;

public class EncuestaStartActivity extends ActionBarActivity  {

    private CEncuesta encuesta;
    private  RadioGroup rbGroup;
    private int indexMarker =0;
    private TextView lblTituloPregunta;
     private ListIterator<CPregunta> iterator = null;
     Button btNext;
    Button btBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta_start);
        rbGroup = (RadioGroup) findViewById(R.id.RbGroup);
        lblTituloPregunta = (TextView) findViewById(R.id.LblTituloPregunta);
        btNext= (Button) findViewById(R.id.BtSiguiente);
    //    btBack = (Button) findViewById(R.id.BtAnterior);
        MostrarEncusta();
    }

    @Override
    public  void onBackPressed() {
        // Write your code here


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea salir de la encuesta actual?") .setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UserSession.T_ENCUESTA= null;
                finish();
            }
        } ) .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //User clicked yes!
            }
        }).show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encuesta_start, menu);
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

    private  void MostrarEncusta()
    {
        try
        {
            encuesta= UserSession.T_ENCUESTA;
            if (encuesta!= null)
            {
                iterator= encuesta.getPreguntas().listIterator();
                CPregunta p = iterator.next();
                lblTituloPregunta.setText(p.getPregunta());
                DrawRespuestas(p);
             }
            else
            {
                Toast.makeText(this,"No hay encuesta", Toast.LENGTH_SHORT).show();
                btNext.setEnabled(false);
            }
        }
        catch (Exception d )
        {
            Toast.makeText(this,"Error al mostrar pregunta (E005)",Toast.LENGTH_LONG).show();
        }

    }
    //comit


    public void btSiguienteClick (View v)
    {
        if(encuesta!= null)
        {

                int resp = rbGroup.getCheckedRadioButtonId();
                if (resp < 0)
                {
                    Toast.makeText(this, "Favor de seleccionar una respusta para continuar", Toast.LENGTH_LONG).show();
                    return;
                }
            int curr = iterator.nextIndex()-1; // posicion actual del iterador
            encuesta.getPreguntas().get(curr).Seleccionado = Integer.toString(resp); // guardar la respuesta
            // Si hay mas preguntas avansar el apuntador .
            if(iterator.hasNext()){
                CPregunta pregunta = iterator.next();
                lblTituloPregunta.setText(pregunta.getPregunta());
                DrawRespuestas(pregunta);
            }
            else
            {
                btNext.setText("Finalizar Encuesta");
                String r =  encuesta.GuardarRespuestas(this);
                if(r!="1")
                {
                    Toast.makeText(this,"Error: " +r,Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Encuesta guardada correctamente.", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

  /*  public void btAnteriorClick (View v)
    {
        if (encuesta!= null)
        {

            if (iterator.hasPrevious()){

                if(btNext.getText() =="Finalizar Encuesta")
                {
                    btNext.setText("Siguiente");
                }

                iterator.previous();
                 CPregunta c = iterator.previous();
                 lblTituloPregunta.setText(c.getPregunta());
                 DrawRespuestas(c);
        }
        }
    }*/


    private void DrawRespuestas (CPregunta p)
    {
        ArrayList<CRespuesta> respuestas =p.getRespuestas();

        rbGroup.removeAllViews();

        for(CRespuesta f : respuestas)
        {
            RadioButton rbt = new RadioButton(this);
            rbt.setText(f.getRespuesta());
            rbt.setId(f.getIdRespuesta());
            rbGroup.addView(rbt);
        }
    }

}
