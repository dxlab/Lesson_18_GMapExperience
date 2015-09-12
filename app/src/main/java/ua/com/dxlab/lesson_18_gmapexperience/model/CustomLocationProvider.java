package ua.com.dxlab.lesson_18_gmapexperience.model;

import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Dima on 12.09.2015.
 */
public class CustomLocationProvider {


    public Location setCustomLocation(double _latitude, double _longitude) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(_latitude);
        location.setLongitude(_longitude);
        return location;
    }


}
