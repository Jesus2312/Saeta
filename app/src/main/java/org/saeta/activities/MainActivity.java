package org.saeta.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.saeta.webservice.WsConsume;


public class MainActivity extends ActionBarActivity {


    //Controls Declarations.
    Button LoginButton;
    Button RegisterButton;
    EditText LblUserName;
    EditText LblPassword;
    //

    public void Click (View v)
    {
        new MainActivityServices().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         InitializeControls();

    }

    private void InitializeControls ()
    {
        LoginButton= (Button) findViewById(R.id.BtLogin);
        RegisterButton = (Button) findViewById(R.id.BtRegister);
        LblPassword = (EditText) findViewById(R.id.TbPassword);
        LblUserName = (EditText) findViewById(R.id.TbUserName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void AuthenticateUser()
    {
        Debug.waitForDebugger();
        WsConsume consumer = new WsConsume();
       consumer.getReponse2();



 }


   class MainActivityServices extends AsyncTask<String,String,String >
   {


       @Override
       protected String doInBackground(String... params) {
         AuthenticateUser();
           return null;
       }
   }

}
