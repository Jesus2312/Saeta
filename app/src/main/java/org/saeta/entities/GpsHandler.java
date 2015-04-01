package org.saeta.entities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by jlopez on 3/30/2015.
 */
public class GpsHandler  implements LocationListener{

    private LocationManager locationManager;
    private Context context;



    public  GpsHandler(Context c)
    {
         locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,10,this);
    }

    public  boolean isGpsEnabled ()
    {
         boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return  enabled;
    }

    public double getLatitude()
    {
        double lat;
        try {
             Location location = locationManager.getLastKnownLocation("gps");
             lat= location.getLatitude();
        }
        catch (Exception e )
        {
            lat =0.0;
        }
        return  lat;
    }

    public  double getLongitude () {
        double lon;
        try {
            Location location = locationManager.getLastKnownLocation("gps");
            lon = location.getLongitude();
        } catch (Exception d) {
            lon = 0.0;
        }
        return lon;
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
