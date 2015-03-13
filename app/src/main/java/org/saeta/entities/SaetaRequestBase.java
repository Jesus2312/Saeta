package org.saeta.entities;

import org.apache.http.NameValuePair;
import org.saeta.webservice.WsConsume;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesus Lopez on 3/12/2015.
 */
public class SaetaRequestBase  {

    private ArrayList<NameValuePair> _params= null;
    private Object _response = null;
    private String _url= null;
    private String _token;
    public  SaetaRequestBase(String tkn , String url)
    {
        _params= new ArrayList<NameValuePair>();
         this._token = tkn;
        _url= url;
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
    public  Object getWsResponse() throws Exception
    {
        if (this._token== null)
        {
            throw  new Exception("request token null");
        }

        WsConsume consumer;

        
        return _response;
    }

    public  void  setResponse (Object h)
    {
        this._response= h;
    }


}
