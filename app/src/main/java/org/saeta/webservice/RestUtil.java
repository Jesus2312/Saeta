package org.saeta.webservice;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by jlopez on 3/28/2015.
 */
public class RestUtil  {

    private String url ;
    private URLConnection urlConnection;
    private URL httpUrl ;
    private HttpURLConnection con;
    private String urlQuery= null;

    public ArrayList<NameValuePair> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<NameValuePair> parameters) {
        this.parameters = parameters;
    }

    private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public  RestUtil(String _url)
    {
        url =_url;

    }

    public String getQueryUrl ()
    {
        String res = null;
        if (this.parameters.size()>0)
        { res = URLEncodedUtils.format(this.parameters,"utf-8");}
        return  res;
    }

    public String  makeGetCall () throws  Exception{
        try {

            if (url.equals(null) || url.equals(""))
            {
                throw new Exception("La url no puede ser nula o vacia ");
            }
            String qp = getQueryUrl();
            if (!qp.equals(null))
            {
                this.url +=("?"+ qp);
            }
            httpUrl = new URL(url);
            con  = (HttpURLConnection)httpUrl.openConnection();
            InputStream is = con.getInputStream();
            String res = convertInputStreamToString(is);
            System.out.println(res);

        } catch (IOException f) {
            throw  new IOException("Error al intentar conectarse verifique conectividad a internet");
        }
        return null;
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




