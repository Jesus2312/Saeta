package org.saeta.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.saeta.bussiness.DataBaseHandler;

import java.io.File;
import java.io.FileOutputStream;

public class LocatorActivity extends Activity {


    public  static String lat;
    public  static String lon;
    public  static  String nombrePersona;
    public  static  String domicilioPersona;
    public  static  String telefonoPersona;
    public  static  int PersonaId;

    private TextView lbNombrePersona ;
    private TextView lbDomicilioPersona ;

    WebView webView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        InitializeBrowser();
        OpenImage();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locator, menu);
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

    public void GuardarMapa (View v )
    {

        DataBaseHandler hn = new DataBaseHandler(LocatorActivity.this);
        FileOutputStream out = null;
        String filename;
        try
        {
            // si  ya existe un mapa entonces eliminarlo
         filename = Environment.getExternalStorageDirectory().toString()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
       //    filename = getFilesDir().getAbsolutePath()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
            File f = new File(filename);
           if (f.exists())
           {               f.delete();
             String q =" delete from persona_mapas where id_persona= '"+ PersonaId+"';";
              hn.ExecuteQuery(q);
          }

            // Guardar mapa
            out = new FileOutputStream(filename);
            Bitmap bitmap = getBitmapFromView(webView);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);

            // guardar en base de datos .
            String query =" insert into persona_mapas values ('"+PersonaId+"' ,'"+ filename+"');";
            hn.ExecuteQuery(query);

            Toast.makeText(LocatorActivity.this,"Mapa guardado correctamente",Toast.LENGTH_LONG).show();
       }
        catch (Exception  y) {
            Toast.makeText(LocatorActivity.this,"Error al guardar mapa",Toast.LENGTH_LONG).show();

        }
    }


    public void OpenImage ()
    {
        try
        {
             String filename = Environment.getExternalStorageDirectory().toString()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
            // String filename=getFilesDir().getAbsolutePath()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
            File fImage = new File( filename);
            if (fImage.exists())
            {
                String furi = "file://"+ filename;
                String html= "<html><head></head><body> <img src=\""+furi+"\"</body></html>";
                webView.loadDataWithBaseURL("",html,"text/html","utf-8","");
                // webView.loadUrl(url);
            }
        }
        catch (Exception d ) {
            Toast.makeText(LocatorActivity.this,"Error al abrir imagen", Toast.LENGTH_LONG).show();
        }

    }


    private int SaveImgeExternal (String path) throws Exception
    {
        String imageFileName = path;
        File storageDir = Environment.getExternalStoragePublicDirectory(
              Environment.DIRECTORY_PICTURES);
       // File storageDir = getFilesDir().getParentFile();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return  1;
    }

    public  Bitmap getBitmapFromView(View view) {
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

    public void DeleteMap (View v)
    {
        DataBaseHandler hn = new DataBaseHandler(LocatorActivity.this);
        FileOutputStream out = null;
        String filename;
        try
        {
            filename = Environment.getExternalStorageDirectory().toString()+"/MAPA_PERSONA_"+String.valueOf(PersonaId)+".png";
            File f = new File(filename);
            if (f.exists())
            {
                f.delete();
                String q =" delete from persona_mapas where id_persona= '"+ PersonaId+"';";
                hn.ExecuteQuery(q);
                // Reload web view
                InitializeBrowser();
                Toast.makeText(LocatorActivity.this,"Mapa eliminado correctamente, se cargara de nuevo el localizador para guardar otro mapa",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(LocatorActivity.this,"No hay mapa a eliminar.",Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception d)
        {
            Toast.makeText(LocatorActivity.this,"Error detalles: " +d.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void InitializeBrowser()
    {
        webView = (WebView) findViewById(R.id.WbView);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.setWebViewClient(new WebViewClient());
        String url ="http://maps.google.com/maps?z=10&t=m&q=loc:"+lat+"+"+lon;
        webView.loadUrl(url);
        //webView.loadUrl("http://maps.google.com/maps?z=10&t=m&q=loc:38.9419+-78.3020");

        // Etiquetas
        lbNombrePersona = (TextView) findViewById(R.id.LblNombre);
        lbDomicilioPersona = (TextView) findViewById(R.id.LblDomicilio);

        lbNombrePersona.setText(nombrePersona);
        lbDomicilioPersona.setText(domicilioPersona);


    }
}
