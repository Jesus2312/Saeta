package org.saeta.webservice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jlopez on 3/10/2015.
 */
public class WsConsume {

    private  String _url;
    HttpClient httpClient;
    HttpContext localContext;
    HttpGet httpGet;
    private ArrayList<NameValuePair> wsParameters  = new ArrayList<>();

    public WsConsume()
    {

    }

    public WsConsume(String url)
    {
        _url= url;

    }

    public String getResponse ()
    {
        BufferedReader reader = null;
        HttpURLConnection con;
        try
        {
            URL url = new URL("http://api.saeta.org.mx/Token");
            con = (HttpURLConnection) url.openConnection();
          //  con.addRequestProperty("grant_type","password");
           // con.addRequestProperty("username","USRAUDITBETO");
           // con.addRequestProperty("password","7dJp4L");

            StringBuilder sb = new StringBuilder();



           // reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ((line= reader.readLine())!= null)
            {
                sb.append(line +"\n");

            }

            return sb.toString();
        }
        catch (Exception e)
        {
           e.printStackTrace();
            return null;
        }
        finally {
            try
            {
                if (reader!= null) reader.close();
            }
            catch (Exception d)
            {
                d.printStackTrace();
                return null;
            }

        }

    }


    public void setParameters(ArrayList<NameValuePair> _params)
    {
        this.wsParameters =_params;
    }


    public void getReponse2()
    {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try
        {
            ArrayList<NameValuePair> param  = new ArrayList<>();
            //URI website = new URI("http://api.saeta.org.mx/Token");
            HttpPost httpPost = new HttpPost("http://api.saeta.org.mx/Token");

            JSONObject object = new JSONObject();

            object.put("grant_type","password");
            object.put("username","USRAUDITBETO");
            object.put("password","7dJp4L");

            param.add(new BasicNameValuePair("grant_type","password"));
            param.add(new BasicNameValuePair("username","USRAUDITBETO"));
            param.add(new BasicNameValuePair("password","7dJp4L"));

            httpPost.setEntity(new UrlEncodedFormEntity(param));

            response= httpClient.execute(httpPost);

            Object o = response;

        }
        catch (Exception e )
        {

        }
    }

    public String getPostWsResponse()
    {
        String result= null;
        try
        {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response;
            HttpPost post = new HttpPost(this._url);
            post.setEntity(new UrlEncodedFormEntity(this.wsParameters));
            response= client.execute(post);

            InputStream stream = response.getEntity().getContent();

            String json = convertInputStreamToString(stream);

            result= json;

        }
        catch ( Exception f)
        {
            return null;

        }
        finally
        {
           // Manejar recursor Dispose en este bloque.
        }
        return  result;
    }


    public String wsGetRequest(ArrayList<HttpParams> args)
    {
        String result ="";
        try
        {
            DefaultHttpClient client= new DefaultHttpClient();
            HttpGet  request= new HttpGet(this._url);

            for (HttpParams s :args)
            {
                request.setParams(s);
            }

            HttpResponse response = client.execute(request);

            HttpEntity entity = response.getEntity();

        }
        catch (Exception h)
        {
            return  null;
        }
        return  result;
    }


    public  String TestGet(String token)
    {
        String result ="";
        try
        {
           String uri="http://api.saeta.org.mx/Auditoria";
            URL url = new URL(uri);



            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            String strCred ="Bearer " +token;
            connection.setRequestProperty("Authorization",strCred);
            InputStream s = connection.getInputStream();

            result = convertInputStreamToString(s);
        }
        catch (Exception h)
        {
            return  null;
        }
        return  result;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }




}
