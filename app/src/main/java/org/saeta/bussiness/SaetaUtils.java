package org.saeta.bussiness;

/**
 * Created by Jesus Lopez on 3/16/2015.
 */
public  class SaetaUtils {

    public static  int tryIntParse (String data)
    {
        try
        {
          return  Integer.parseInt(data);
        }
        catch (Exception d)
        {
            return -1;
        }
    }

    public  static float tryFloatParse (String data)
    {
        try
        {
            return Float.parseFloat(data);
        }
        catch (Exception d)
        {
            return 0;
        }

    }
}
