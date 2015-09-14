package ua.com.dxlab.lesson_18_gmapexperience;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ua.com.dxlab.lesson_18_gmapexperience.model.MarkerItem;
import ua.com.dxlab.lesson_18_gmapexperience.model.helpers.DBMarkersOpenHelper;
import ua.com.dxlab.lesson_18_gmapexperience.view.LoadingDialog;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {

    public static final double START_LATITUDE = 48.6208;
    public static final double START_LONGITUDE = 22.287883;
    public static final String KEY_LAT = "LAT";
    public static final String KEY_LONG = "LONG";
    public static final String GOOGLE_MAP_MARKERS_DB_NAME = "GMapMarkersDB";
    public static final String DEFAULT_MARKER = "defaultMarker";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private CustomLocationListener mCustomLocationListener;
    private LocationManager mLocationManager;
    private Location mLocation;
    private DBMarkersOpenHelper mDBMarkerOpenHelper;
    private Button mBtnDelAll;
    private Button mBtnShowLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        extendUI();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mCustomLocationListener = new CustomLocationListener(this);
        mDBMarkerOpenHelper = new DBMarkersOpenHelper(MapsActivity.this, GOOGLE_MAP_MARKERS_DB_NAME, null, 1);

        setUpMapIfNeeded();
     }

    private void extendUI() {
        mBtnDelAll = new Button(this);
        mBtnDelAll.setText(R.string.delete_all);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(mBtnDelAll, params);

        mBtnShowLocation = new Button(this);
        mBtnShowLocation.setText(R.string.show_location);

        addContentView(mBtnShowLocation, params);

        mBtnDelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMarkersDialog();
            }
        });

        mBtnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void deleteMarkersDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        new AsyncTaskDeleteAllMarkersDB().execute();
                        mMap.clear();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
                dialog.dismiss();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes_answer, dialogClickListener)
                .setNegativeButton(R.string.no_answer, dialogClickListener).show();
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

        new AsyncTaskRecallMarkersDB().execute();
    }


    @Override
    public void onMapClick(LatLng _latLng) {
        String title = "Here we are:  " + String.format("%1$s | %2$s",
                String.valueOf(_latLng.latitude), String.valueOf(_latLng.longitude));
        final boolean isCustomized = false;
        setMarker(_latLng, title, isCustomized, DEFAULT_MARKER);

        MarkerItem markerItem = new MarkerItem(title, _latLng.latitude, _latLng.longitude, isCustomized, DEFAULT_MARKER);
         new AsyncTaskAddMarkerDB().execute(markerItem);

        Toast.makeText(MapsActivity.this,"LONG CLICK TO CREATE CUSTOM MARKER",Toast.LENGTH_SHORT).show();
    }

    public void setCurrentLocation(Location location) {
        if (location != null) {

            /*Log.d("maptag", "Lat: " + location.getLatitude() + "\n" +
                    "Lng: " + location.getLongitude());*/

            mLocation = location;
        }
    }

    private void setMarker(LatLng _latLng, String _title, boolean _isCustomized, String _imgURI) {

        BitmapDescriptor bd = (_imgURI.equals(DEFAULT_MARKER)) ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE) : null;

        mMap.addMarker(new MarkerOptions().position(_latLng).
                title(_title).
                snippet("Marker selected").
                icon(bd));
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


    private class AsyncTaskAddMarkerDB extends AsyncTask<MarkerItem,Void,Boolean>{
        @Override
        protected Boolean doInBackground(MarkerItem... params) {
            mDBMarkerOpenHelper.addMarker(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mBtnDelAll.setEnabled(result);
            super.onPostExecute(result);
        }
    }

    private class AsyncTaskDeleteAllMarkersDB extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            mDBMarkerOpenHelper.deleteAllMarkers();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mBtnDelAll.setEnabled(result);
            super.onPostExecute(result);
        }
    }

    private class AsyncTaskRecallMarkersDB extends AsyncTask<Void, MarkerItem, Void>{
        private final FragmentManager mFManager;
        private LoadingDialog mLoadingDialog;

        public AsyncTaskRecallMarkersDB() {
            mFManager = getSupportFragmentManager();
            mLoadingDialog = new LoadingDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<MarkerItem> markerItemsList = mDBMarkerOpenHelper.getAllMarkers();
            if (markerItemsList.size()>0) {
                for (int i = 0; i < markerItemsList.size(); i++) {
                    publishProgress(markerItemsList.get(i));
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(MarkerItem... values) {
            super.onProgressUpdate(values);
            setMarker(new LatLng(values[0].getLatitude(),
                            values[0].getLongitude()),
                    values[0].getTitle(),
                    values[0].isCustomized(), values[0].getImageURI());
        }

        @Override
        protected void onPostExecute(Void result) {
            mBtnDelAll.setEnabled(mDBMarkerOpenHelper.getMarkersCount()>0 ? true : false);
            dismissLoadingDialog();
        }

        @Override
        protected void onPreExecute() {
            showLoadingDialog();
        }


        private void showLoadingDialog() {
            mLoadingDialog.show(mFManager, "Loading");
            //Log.d("extrdata:", "Shown");

        }

        private void dismissLoadingDialog() {
            //Log.d("extrdata:", "Dismissed");
            mLoadingDialog.dismiss();
        }
    }
}
