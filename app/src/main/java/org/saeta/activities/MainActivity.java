package org.saeta.activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.saeta.bussiness.DataBaseHandler;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.webservice.WsConsume;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    //Controls Declarations.
    Button LoginButton;
    Button RegisterButton;
    EditText LblUserName;
    EditText LblPassword;
    private ProgressDialog dialog;
    //

    public void Click (View v)
    {
        String user = LblUserName.getText().toString();
        String pass = LblPassword.getText().toString();

        if (user=="" || pass =="")
        {
            Toast.makeText(this,"Porfavor ingresa tus credenciales para continuar",Toast.LENGTH_LONG);
            return ;
        }
        else
        {
             Debug.waitForDebugger();
             new MainActivityServices().execute();
        }
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

    public void clickRegister (View v)
    {
        CEncuesta  e = new CEncuesta();

        e.saveToDataBase(MainActivity.this);

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

    public String  AuthenticateUser(String user, String pass)
    {
        String opResult= null;
        try
        {
            Debug.waitForDebugger();
            WsConsume consumer =new WsConsume("http://api.saeta.org.mx/Token");

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("grant_type","password"));
            param.add(new BasicNameValuePair("username",user));
            param.add(new BasicNameValuePair("password",pass));

            consumer.setParameters(param);

            String jsonResult=consumer.getPostWsResponse();

            JSONObject jsonLogin=  new JSONObject(jsonResult);

            String token = jsonLogin.getString("access_token");
            String fullName = jsonLogin.getString("fullName");
            String tokenType =jsonLogin.getString("token_type");
            String userName = jsonLogin.getString("userName");


            // set session variables
               UserSession.TOKEN_KEY= token;
               UserSession.TOKEN_TYPE= tokenType;
               UserSession.USER_NAME= userName;
            //

            opResult="1";

        }
        catch (JSONException jsonError)
        {
            opResult= "El nombre de usuario o contraseña son invalidos";
        }
        catch (Exception e )
        {
            opResult= "Error al validar credenciales (E001)";
        }

      return  opResult;
     }


   class MainActivityServices extends AsyncTask<String,String,String >
   {

       private  String _userName;
       private String _userPassword;
       private String _opResult;

       @Override
       protected void onPreExecute() {
           _userName = LblUserName.getText().toString();
           _userPassword = LblPassword.getText().toString();
           dialog= new ProgressDialog(MainActivity.this);
           dialog.setMessage("Validando  datos espere...");
           dialog.setIndeterminate(false);
           dialog.setCancelable(false);
           dialog.show();
       }

       @Override
       protected String doInBackground(String... params) {
          _opResult=AuthenticateUser(this._userName, this._userPassword );
           return _opResult;
       }
       @Override
       protected  void onPostExecute (final String result)
       {
           dialog.dismiss();
           if (_opResult != "1")
           {
               Toast.makeText(MainActivity.this,_opResult,Toast.LENGTH_LONG).show();
           }
           else
           {
               // cambiar a nueva actividad
               startActivity(new Intent("org.saeta.EncuestaActivity"));

           }
       }
   }

}
