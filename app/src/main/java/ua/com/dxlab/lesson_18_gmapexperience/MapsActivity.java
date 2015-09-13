package ua.com.dxlab.lesson_18_gmapexperience;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    public static final double START_LATITUDE = 48.6208;
    public static final double START_LONGITUDE = 22.287883;
    public static final String KEY_LAT = "LAT";
    public static final String KEY_LONG = "LONG";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private CustomLocationListener mCustomLocationListener;
    private LocationManager mLocationManager;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mCustomLocationListener = new CustomLocationListener(this);
        setUpMapIfNeeded();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        //Log.d("maptag", "onResume");


        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        double latitude = (double) sharedPreferences.getFloat(KEY_LAT,(float) START_LATITUDE);
        double longitude = (double) sharedPreferences.getFloat(KEY_LONG, (float) START_LONGITUDE);

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Setting the build-in zoom control buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);

    }

    public void setCurrentLocation(Location location) {
        if (location != null) {

            /*Log.d("maptag", "Lat: " + location.getLatitude() + "\n" +
                    "Lng: " + location.getLongitude());*/

            mLocation = location;
        }
    }

    private void setMarker(LatLng _latLng, String _title) {
        mMap.addMarker(new MarkerOptions().position(_latLng).title(_title));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, mCustomLocationListener);
    }

    @Override
    protected void onStop() {
        mLocationManager.removeUpdates(mCustomLocationListener);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat(KEY_LAT, (float) mLocation.getLatitude());
        editor.putFloat(KEY_LONG, (float) mLocation.getLongitude());

        editor.commit();
        super.onStop();
    }

}
