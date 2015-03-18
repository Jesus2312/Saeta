package org.saeta.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.saeta.activities.R;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPregunta;
import org.saeta.entities.CRespuesta;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class EncuestaStartActivity extends ActionBarActivity  {

    static  final int REQUEST_VIDEO_CAPTURE =1;
    private CEncuesta encuesta;
    private  RadioGroup rbGroup;
    private TextView lblTituloPregunta;
    private ListIterator<CPregunta> iterator = null;
    private MediaRecorder mRecorder = null;
    private boolean isRecording= false;
    Button btNext;
    LinearLayout ly ;
    RecordAudioButton rcbutton;
    Intent takeVideoIntent;
    VideoView videoView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta_start);
        rbGroup = (RadioGroup) findViewById(R.id.RbGroup);
        lblTituloPregunta = (TextView) findViewById(R.id.LblTituloPregunta);
        btNext= (Button) findViewById(R.id.BtSiguiente);
        ly = (LinearLayout) findViewById(R.id.ly2);
        rcbutton = new RecordAudioButton(this);
        ly.addView(rcbutton);

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

    public  void PlayTest(View v)
    {
        MediaPlayer mp = new MediaPlayer();
        try
        {
            int currIndex = iterator.nextIndex() - 1;
            String fname =  getFilesDir() +"/AU" + encuesta.IdEncuesta.toString() + "-" + encuesta.getPreguntas().get(currIndex).getIdPregunta() + ".3gp";
            mp.setDataSource(fname);
            mp.prepare();
            mp.start();
        }
        catch (Exception d)
        {
            Toast.makeText(this,"eee",Toast.LENGTH_LONG).show();
        }
    }

    private void onRecord(boolean start)
    {
        if(start)
        {
            StartRecording();
        }
        else {
            StopRecordng();
        }
    }
    public  void StopRecordng ()
    {
        try
        {
            mRecorder.stop();
            mRecorder.release();
            mRecorder= null;
            isRecording = false;
        }
        catch (Exception d)
        {

        }
    }


    public void capturarVideo (View v)
    {
        takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode ==REQUEST_VIDEO_CAPTURE && resultCode ==RESULT_OK)
        {
           videoView= new VideoView(this);
           Uri videoUri = data.getData();
           videoView.setVideoURI(videoUri);
           videoView.start();

        }
    }
    public void StartRecording () {
        try {

            if (!isRecording) {
                int currIndex = iterator.nextIndex() - 1;
                String fname =  getFilesDir() +"/AU" + encuesta.IdEncuesta.toString() + "-" + encuesta.getPreguntas().get(currIndex).getIdPregunta() + ".3gp";
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //FileOutputStream fs = openFileOutput(fname,Context.MODE_PRIVATE);
                //FileDescriptor ds = fs.getFD();
                mRecorder.setOutputFile(fname);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mRecorder.prepare();
                } catch (IOException i) {
                    Toast.makeText(this, "Error en preparacion de dispositivo ", Toast.LENGTH_LONG).show();
                }


                mRecorder.start();
                isRecording = true;
            }

        } catch (Exception d) {
            Toast.makeText(this, "Error al grabar audio " + d.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    class  RecordAudioButton extends   Button{
        boolean mStartRecording = true;

        public RecordAudioButton(Context context) {
            super(context);
            setText("Grabar Audio");
            setOnClickListener(clicker);
        }
        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);

                        if(mStartRecording)
                        {
                            setText("Detener Grabacion");
                        }
                         else {
                            setText("Grabar audio");

                     }
                mStartRecording = !mStartRecording;
              }
        };


    }

}
