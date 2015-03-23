package org.saeta.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import org.saeta.activities.R;
import org.saeta.bussiness.SaetaUtils;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;

import java.io.IOException;

public class FinalEncuestaActivity extends ActionBarActivity {

    static  final int REQUEST_VIDEO_CAPTURE =1;
    Intent takeVideoIntent;
    VideoView videoView = null;
    private MediaRecorder mRecorder = null;
    private boolean isRecording= false;
    private CEncuesta encuestaAGrabar = null;
    private CPersona personaEncuestada = null;
    RecordAudioButton recordAudioButton = null;
    LinearLayout masterL = null;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_encuesta);
        GetData();
        masterL= (LinearLayout) findViewById(R.id.MasterL);
        recordAudioButton = new RecordAudioButton(this);
        masterL.addView(recordAudioButton,4);
    }

    private void GetData ()
    {
        encuestaAGrabar= UserSession.T_ENCUESTA;
        personaEncuestada = UserSession.T_PERSONA;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_final_encuesta, menu);
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

    public void FinalizarEncuestaClick(View v)
    {
        GetDialog();
    }


    private void GetDialog ()
    {

        if (encuestaAGrabar.VideoUrl.equals("")&& encuestaAGrabar.AudioUrl.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("Finalizar Encuesta")
                    .setMessage("Desea guardar esta en cuesta sin fotografia/audio/video?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            new asyncSaveEncuetsta(encuestaAGrabar, personaEncuestada).execute();

                            // finish();
                            //Toast.makeText(FinalEncuestaActivity.this, "Yaay", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
        else
        {
            new asyncSaveEncuetsta(encuestaAGrabar,personaEncuestada).execute();
        }
    }


    private int  SaveEncuesta( CEncuesta e, CPersona p)
    {
        int i=0;
        try
        {
             ////   Debug.waitForDebugger();
            String r=e.GuardarRespuestas(this,p);
            if (r.equals("1"))
            {
                i =1;
            }
            else
            {
                i=0;
            }

        }
        catch (Exception f)
        {
            i =0;
        }
        return  i ;
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
            Toast.makeText(this,"Error al detener grabacion de audio E(0012)",Toast.LENGTH_LONG).show();
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
          //  int idx = iterator.nextIndex()-1;
            videoView= new VideoView(this);
            Uri videoUri = data.getData();
            String absolutePath ="";
            try
            {
                String [] proj = {MediaStore.Video.Media.DATA};
                Cursor cursor =  this.getContentResolver().query(videoUri,proj,null,null,null);
                int  colIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                absolutePath = cursor.getString(colIndex);

            }
            catch (Exception d)
            {
                Toast.makeText(this, "Error al capturar video E(017)",Toast.LENGTH_LONG).show();
                absolutePath =null;
            }
           // encuesta.getPreguntas().get(idx).VideoUrl= absolutePath;
            encuestaAGrabar.VideoUrl = absolutePath;
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }
    public void StartRecording () {
        try {

            if (!isRecording) {
                //int currIndex = iterator.nextIndex() - 1;
                String fname =  getFilesDir() +"/AUDIO-ENCUESTA-" + encuestaAGrabar.IdEncuesta.toString() +"-"+personaEncuestada.getIdDetectado()+ ".3gp";
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //FileOutputStream fs = openFileOutput(fname,Context.MODE_PRIVATE);
                //FileDescriptor ds = fs.getFD();
                mRecorder.setOutputFile(fname);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                // guardar la ruta del archivo
               // encuesta.getPreguntas().get(currIndex).AudioUrl =fname;
                encuestaAGrabar.AudioUrl = fname;

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
    class  RecordAudioButton extends Button {
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


    class  asyncSaveEncuetsta extends AsyncTask<String,String,String> implements LocationListener
    {

        private LocationManager locationManager;
        private String provider;
        private CEncuesta _e;
        private CPersona _p;
        float lat ;
        float lon;
        int saveResult;
        public  asyncSaveEncuetsta(CEncuesta encuesta,CPersona persona)
        {
            _e= encuesta;
            _p = persona;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

        }

        @Override
        protected void onPreExecute ()
        {
            try
            {
                dialog= new ProgressDialog(FinalEncuestaActivity.this);
                dialog.setMessage("Guardando encuesta porfavor espere...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
                Location location = locationManager.getLastKnownLocation(provider);
                 lat = (float)location.getLatitude();
                 lon = (float) location.getLongitude();
            }
            catch (Exception f)
            {
                System.out.println(f.getMessage());
            }

        }

        @Override
        protected String doInBackground(String... params) {

           // Setear las coordenadas .

            int f ;
             _e.Latitud =  Float.toString(lat);
             _e.Longitud = Float.toString(lon);
             saveResult= SaveEncuesta(_e,_p);
             return  null;
        }

        @Override
        protected  void onPostExecute (String f)
        {
            UserSession.T_ENCUESTA = null;
            UserSession.T_PERSONA = null;
            UserSession.SAVE_OK_FLAG= saveResult;
            dialog.setTitle("Encuesta guardada!");
            dialog.dismiss();
            finish();
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


}
