package org.saeta.entities;

import org.apache.http.NameValuePair;
import org.apache.http.params.HttpParams;
import org.saeta.webservice.WsConsume;

import java.util.ArrayList;

/**
 * Created by Jesus Lopez on 3/12/2015.
 */
public class SaetaRequestBase  {

    private ArrayList<NameValuePair> _params= null;
    private Object _response = null;
    private String _url= null;
    private  String _token;
   private ArrayList<HttpParams> args = new ArrayList<HttpParams>();

    public String get_token(){return _token;}

    public  SaetaRequestBase(String tkn , String url)
    {
        _params= new ArrayList<NameValuePair>();
         this._token = tkn;
        _url= url;
    }

    public  void setGetParameters (ArrayList<HttpParams> p)
    {
        this.args= p;
    }

    public ArrayList<NameValuePair> getRequestParameters ()
    {
        return _params;
    }

    public void setParametersArray (ArrayList<NameValuePair> p)
    {
        if (_params== null)
        {
            _params= p;
        }
    }
    public  Object getWsResponseGET() throws Exception
    {
        if (this._token== null)
        {
            throw  new Exception("request token null E(003)");
        }
        WsConsume consumer = new WsConsume(_url);
        Object x=consumer.wsGetRequest(args);
        return x;
    }

    public  void  setResponse (Object h)
    {
        this._response= h;
    }


}
