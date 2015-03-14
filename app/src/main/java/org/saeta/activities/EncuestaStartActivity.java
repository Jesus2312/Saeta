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

public class EncuestaStartActivity extends ActionBarActivity  {

    private CEncuesta encuesta;
    private  RadioGroup rbGroup;
    private int indexMarker =0;
    private TextView lblTituloPregunta;
    Button btNext;
    Button btBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta_start);
        rbGroup = (RadioGroup) findViewById(R.id.RbGroup);
        lblTituloPregunta = (TextView) findViewById(R.id.LblTituloPregunta);
        btNext= (Button) findViewById(R.id.BtSiguiente);
        btBack = (Button) findViewById(R.id.BtAnterior);
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
                CPregunta p = encuesta.getPreguntas().get(0);
                lblTituloPregunta.setText(p.getPregunta());
                DrawRespuestas(p);
                indexMarker +=1;
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

    public void btSiguienteClick (View v)
    {
        //TODO Guardar respuesta actual

        int total = encuesta.getPreguntas().size();

        if (indexMarker == total)
        {
          btNext.setText("Finalizar");
        }
        else
        {
            // obtener la pregunta por medio del indice
            CPregunta pregunta = encuesta.getPreguntas().get(indexMarker);
            lblTituloPregunta.setText(pregunta.getPregunta());
            // dibjuarlas respuestas a tal pregunta .
            DrawRespuestas(pregunta);
            // incrementar el indice .
            indexMarker += 1;
        }
    }

    public void btAnteriorClick (View v)
    {
        if(indexMarker >0 ) {
            // obtener la pregunta por medio del indice
            CPregunta pregunta = encuesta.getPreguntas().get(indexMarker);
            lblTituloPregunta.setText(pregunta.getPregunta());
            // dibjuarlas respuestas a tal pregunta .
            DrawRespuestas(pregunta);
            // incrementar el indice .
            indexMarker-=1;
        }
            }


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
