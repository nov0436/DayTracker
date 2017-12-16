package com.example.novak.dayostrackos;

/**
 * Created by novak on 16-Dec-17.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


public class Locator implements LocationListener{

    Context context;

    public Locator(Context c)
    {
        context = c;
    }

    public Location getLocation()
    {
        if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            Log.e("error","permission denied");
            return null;
        }
        try {
            LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled){
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000,10,this);
                Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return loc;
            }else{
                Toast.makeText(context, "Please enable GPS to use this function.", Toast.LENGTH_SHORT).show();
                Log.e("error","gps not enabled");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getCityNameAtLocation(double lat, double lon)
    {
        String curCity = "";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);

            if (addressList.size() > 0)
            {
                curCity = addressList.get(0).getLocality();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.d("error", "error retrieving address");
        }

        return curCity;
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
