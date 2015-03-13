package org.saeta.activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.saeta.activities.R;
import org.saeta.bussiness.UserSession;

public class EncuestaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
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

    public  void GetEncuestas()
    {
        try
        {
             String user= UserSession.USER_NAME;
             String token = UserSession.TOKEN_KEY;




        }
        catch (Exception e )
        {

        }
    }


    class  asyncWsHelper extends AsyncTask<String,String,String>
    {
        int _actionToTake=-1;


        public asyncWsHelper(int action)
        {

        }

        @Override
        protected String doInBackground(String... params) {
            String opResult= null;
        switch (_actionToTake)
        {

            case 0:
            // obtener los datos de la encuesta

                break;
        }
            return opResult;
        }
    }
}
