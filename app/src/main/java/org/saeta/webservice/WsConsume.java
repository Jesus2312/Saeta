package org.saeta.webservice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;
import org.saeta.bussiness.UserSession;
import org.saeta.entities.CEncuesta;
import org.saeta.entities.CPersona;
import org.saeta.entities.CPregunta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
   //         UrlEncodedFormEntity entity = new UrlEncodedFormEntity(this.wsParameters);
     //       post.setEntity(entity);
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

    public ArrayList<NameValuePair> FEncode(CEncuesta f)
    {
        ArrayList<NameValuePair> l = new ArrayList<NameValuePair>();
        try
        {
            l.add(new BasicNameValuePair("Encuesta",f.Encuesta));
            l.add( new BasicNameValuePair("IdDetectado",f.IdDetectado));
            l.add( new BasicNameValuePair("IdEncuesta",f.IdEncuesta));
            l.add( new BasicNameValuePair("IdProceso",f.IdProceso));
            l.add( new BasicNameValuePair("Materno",f.Materno));
            l.add( new BasicNameValuePair("Nombre",f.Nombre));

            int idx = 0;
            for(CPregunta p : f.Preguntas)
            {
                l.add(new BasicNameValuePair("Preguntas["+idx+"][IdPregunta]",String.valueOf(p.IdPregunta)));
                l.add(new BasicNameValuePair("Preguntas["+idx+"][IdEncuesta]",String.valueOf(p.IdEncuesta)));
                l.add(new BasicNameValuePair("Preguntas["+idx+"][Pregunta]",p.Pregunta));
                l.add(new BasicNameValuePair("Preguntas["+idx+"][MultiRespuesta]",String.valueOf(p.MultiRespuesta)));
                l.add(new BasicNameValuePair("Preguntas["+idx+"][Seleccionado]",String.valueOf(p.Seleccionado)));
                l.add(new BasicNameValuePair("Preguntas["+idx+"][Respuestas]",""));
                l.add(new BasicNameValuePair("Preguntas["+idx+"][Checked]",String.valueOf(p.Seleccionado)));
                idx+=1;
            }


        }
        catch (Exception n)
        {
           l = null;
        }
        return  l;
    }

    public  String wsPostRequest(String token,CEncuesta p)
    {
        String res ="";
        try
        {
            ArrayList<NameValuePair> l = FEncode(p);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response;
            HttpPost post = new HttpPost(this._url);
            String x = "Bearer " + UserSession.TOKEN_KEY;
            post.setHeader("Authorization",x);
            post.setEntity(new UrlEncodedFormEntity(l));
            response= client.execute(post);
            InputStream stream = response.getEntity().getContent();
            String json = convertInputStreamToString(stream);
            res= json;
       }
        catch (Exception e)
        {

        }
        return  res;
    }


//    public  String wsPostRequest(String data,String token)
//    {
//        String res ="";
//        try
//        {
//            URL url=  new URL("http://api.saeta.org.mx/Auditoria");
//            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//            httpCon.setDoInput(true);
//            httpCon.setDoOutput(true);
//            String strCred = "Bearer "+ token;
//            httpCon.setRequestProperty("Authorization",strCred);
//            httpCon.setRequestMethod("POST");
//            OutputStream os = httpCon.getOutputStream();
//            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
//            osw.write(data);
//            osw.flush();
//            osw.close();
//
//            InputStream is = httpCon.getInputStream();
//            String result=WsConsume.convertInputStreamToString(is);
//            System.out.println(result);
//        }
//        catch (Exception e)
//        {
//
//        }
//        return  res;
//    }
//

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
