package ua.com.dxlab.lesson_18_gmapexperience.model;

/**
 * Created by Dima on 13.09.2015.
 */
public class MarkerItem {
    private int mID;
    private String mTitle;
    private double mLatitude;
    private double mLongitude;
    private boolean mIsCustomized;
    private String mImageURI;

    public MarkerItem(){

    }

    public MarkerItem(int _ID, String _title, double _latitude, double _longitude, boolean _isCustomized, String _imageURI) {
        mID = _ID;
        mTitle = _title;
        mLatitude = _latitude;
        mLongitude = _longitude;
        mIsCustomized = _isCustomized;
        mImageURI = _imageURI;
    }

    public MarkerItem(String _title, double _latitude, double _longitude, boolean _isCustomized, String _imageURI) {
        mTitle = _title;
        mLatitude = _latitude;
        mLongitude = _longitude;
        mIsCustomized = _isCustomized;
        mImageURI = _imageURI;
    }

    public int getID() {
        return mID;
    }

    public void setID(int _ID) {
        mID = _ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String _title) {
        mTitle = _title;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double _latitude) {
        mLatitude = _latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double _longitude) {
        mLongitude = _longitude;
    }

    public boolean isCustomized() {
        return mIsCustomized;
    }

    public void setCustomized(boolean _isCustomized) {
        mIsCustomized = _isCustomized;
    }

    public String getImageURI() {
        return mImageURI;
    }

    public void setImageURI(String _imageURI) {
        mImageURI = _imageURI;
    }
}
