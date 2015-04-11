package org.saeta.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import org.saeta.bussiness.EncuestaBE;
import org.saeta.bussiness.SaetaUtils;
import org.saeta.entities.CPersona;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MapaGeneralActivity extends ActionBarActivity {


    WebView wb1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_general);
        wb1 = (WebView) findViewById(R.id.WebView1);
        getPersonas();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa_general, menu);
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

    public Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }



    public  void recargarMapa (View v)
    {
        try
        {
           String  filename = Environment.getExternalStorageDirectory().toString()+"/MAPA_GENERAL.png";
            //    filename = getFilesDir().getAbsolutePath()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
            File f = new File(filename);
            if (f.exists())
            {
                f.delete();
                getPersonas();
                Toast.makeText(MapaGeneralActivity.this, "Se cargara un nuevo mapa porfavor espere..", Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(MapaGeneralActivity.this, "No hay mapa guardado un nuevo mapa se cargara en el visualizador", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception g)
        {
            Toast.makeText(MapaGeneralActivity.this, "Error al recargar mapa", Toast.LENGTH_LONG).show();
        }
    }

    public  void guardarMapaClick (View v)
    {
        FileOutputStream out = null;
        String filename;
        try
        {
            // si  ya existe un mapa entonces eliminarlo
            filename = Environment.getExternalStorageDirectory().toString()+"/MAPA_GENERAL.png";
            //    filename = getFilesDir().getAbsolutePath()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
            File f = new File(filename);
            if (f.exists())
            {
                f.delete();

            }

            // Guardar mapa
            out = new FileOutputStream(filename);
            Bitmap bitmap = getBitmapFromView(wb1);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);

            Toast.makeText(MapaGeneralActivity.this,"Mapa guardado correctamente",Toast.LENGTH_LONG).show();
        }
        catch (Exception  y) {
            Toast.makeText(MapaGeneralActivity.this,"Error al guardar mapa",Toast.LENGTH_LONG).show();

        }
    }


    private void getPersonas ()
    {
        try
        {
            String filename = Environment.getExternalStorageDirectory().toString()+"/MAPA_GENERAL.png";
            //    filename = getFilesDir().getAbsolutePath()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
            File f = new File(filename);
            if (f.exists())
            {

                String furi = "file://"+ filename;
                String html= "<html><head></head><body> <img src=\""+furi+"\"</body></html>";
                wb1.loadDataWithBaseURL("",html,"text/html","utf-8","");
            }
            else {

                ArrayList<CPersona> personas = null;
                personas=EncuestaBE.ObtenerPersonasAEncuestar(MapaGeneralActivity.this);

                if (personas== null || personas.size()<=0)
                {
                     Toast.makeText(MapaGeneralActivity.this,"No se han descargado datos para general el mapa", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = SaetaUtils.getGeneralMapMarkers(personas);
                {
                    wb1.loadUrl(url);
                }
            }
        }
        catch (Exception h )
        {
            Toast.makeText(MapaGeneralActivity.this, " Error al generar mapa ", Toast.LENGTH_LONG).show();
        }

    }
}
