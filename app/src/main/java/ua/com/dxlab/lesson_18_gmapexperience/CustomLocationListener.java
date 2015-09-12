package ua.com.dxlab.lesson_18_gmapexperience;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Dima on 12.09.2015.
 */
public class CustomLocationListener implements LocationListener {

    private MapsActivity mMapsActivity;

    public CustomLocationListener(MapsActivity _mapsActivity) {
        this.mMapsActivity = _mapsActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        mMapsActivity.setCurrentLocation(location);
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
