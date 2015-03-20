package org.saeta.bussiness;

import android.content.Context;
import android.database.Cursor;

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

    public  static int QueryExistByCount (String query, Context c)
    {
        int result= -1 ;
        try
        {
            DataBaseHandler h = new DataBaseHandler(c);
            Cursor cExist = h.GetCursor(query);

            if (cExist!= null)
            {
               if (cExist.moveToFirst())
               {
                   result = SaetaUtils.tryIntParse(cExist.getString(0));
               }
            }
            else
            {
                return -1;
            }

        }
        catch (Exception f )
        {
            result=-1;
        }
        return result;
    }
}
