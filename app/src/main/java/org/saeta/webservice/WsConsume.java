package org.saeta.webservice;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
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

}
