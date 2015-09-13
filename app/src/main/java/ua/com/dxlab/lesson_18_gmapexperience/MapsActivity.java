package ua.com.dxlab.lesson_18_gmapexperience;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ua.com.dxlab.lesson_18_gmapexperience.model.MarkerItem;
import ua.com.dxlab.lesson_18_gmapexperience.model.helpers.DBMarkersOpenHelper;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {

    public static final double START_LATITUDE = 48.6208;
    public static final double START_LONGITUDE = 22.287883;
    public static final String KEY_LAT = "LAT";
    public static final String KEY_LONG = "LONG";
    public static final String GOOGLE_MAP_MARKERS_DB_NAME = "GMapMarkersDB";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private CustomLocationListener mCustomLocationListener;
    private LocationManager mLocationManager;
    private Location mLocation;
    private DBMarkersOpenHelper mDBMarkerOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mCustomLocationListener = new CustomLocationListener(this);
        mDBMarkerOpenHelper = new DBMarkersOpenHelper(MapsActivity.this, GOOGLE_MAP_MARKERS_DB_NAME, null, 1);
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
        mMap.setOnMapClickListener(this);

        List<MarkerItem> markerItemList =  mDBMarkerOpenHelper.getAllMarkers();

    }


    private void recallMarkers(List<MarkerItem> _markerItemList) {

    }

    @Override
    public void onMapClick(LatLng _latLng) {
        String title = "Here we are:  " + String.format("%1$s | %2$s",
                String.valueOf(_latLng.latitude), String.valueOf(_latLng.longitude));
        setMarker(_latLng, title, false);
    }

    public void setCurrentLocation(Location location) {
        if (location != null) {

            /*Log.d("maptag", "Lat: " + location.getLatitude() + "\n" +
                    "Lng: " + location.getLongitude());*/

            mLocation = location;
        }
    }

    private void setMarker(LatLng _latLng, String _title, boolean _isCustomized) {
        mMap.addMarker(new MarkerOptions().position(_latLng).
                title(_title).
                snippet("Marker selected").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        MarkerItem markerItem = new MarkerItem(_title, _latLng.latitude, _latLng.longitude, _isCustomized, "defaultMarker");

        mDBMarkerOpenHelper.addMarker(markerItem);
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
