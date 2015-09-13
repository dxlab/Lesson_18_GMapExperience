package ua.com.dxlab.lesson_18_gmapexperience.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ua.com.dxlab.lesson_18_gmapexperience.model.MarkerItem;

/**
 * Created by Dima on 13.09.2015.
 * CRUD little model :) D.S.
 */
public class DBMarkersOpenHelper extends SQLiteOpenHelper {

    private static final String TABLE_MARKERS = "markers";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_IS_CUSTOMIZED = "iscustomized";
    private static final String KEY_IMAGE_URI = "image_uri";


    public DBMarkersOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MARKERS_TABLE = "CREATE TABLE " + TABLE_MARKERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_LATITUDE + " REAL,"
                + KEY_LONGITUDE + " REAL,"
                + KEY_IS_CUSTOMIZED + " INTEGER,"
                + KEY_IMAGE_URI + " TEXT" +")";
        db.execSQL(CREATE_MARKERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKERS);
        onCreate(db);
    }

    void addMarker(MarkerItem _markerItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, _markerItem.getTitle());
        values.put(KEY_LATITUDE, _markerItem.getLatitude());
        values.put(KEY_LONGITUDE, _markerItem.getLongitude());
        values.put(KEY_IS_CUSTOMIZED, _markerItem.isCustomized());
        values.put(KEY_IMAGE_URI, _markerItem.getImageURI());

        db.insert(TABLE_MARKERS, null, values);
        db.close();     }

    MarkerItem getContact(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MARKERS, new String[] { KEY_ID,
                        KEY_TITLE, KEY_LATITUDE, KEY_LONGITUDE, KEY_IS_CUSTOMIZED, KEY_IMAGE_URI}, KEY_ID + "=?",
                new String[] { String.valueOf(_id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MarkerItem markerItem = new MarkerItem(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), Boolean.parseBoolean(cursor.getString(4)), cursor.getString(5));
        return markerItem;
    }


    // Getting All Markers
    public List<MarkerItem> getAllContacts() {
        List<MarkerItem> contactItemList = new ArrayList<MarkerItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MARKERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MarkerItem markerItem = new MarkerItem();
                markerItem.setID(Integer.parseInt(cursor.getString(0)));
                markerItem.setTitle(cursor.getString(1));
                markerItem.setLatitude(cursor.getDouble(2));
                markerItem.setLongitude(cursor.getDouble(3));
                markerItem.setCustomized(Boolean.parseBoolean(cursor.getString(4)));
                markerItem.setImageURI(cursor.getString(5));
                // Adding markerItem to list
                contactItemList.add(markerItem);
                Log.d("extrdata:", cursor.getString(0) + " | " +
                        cursor.getString(1) + " | " +
                        cursor.getString(2) + " | " +
                        cursor.getString(3) + " | " +
                        cursor.getString(4) + " | " +
                        cursor.getString(5));
            } while (cursor.moveToNext());
        }

        return contactItemList;
    }

    public int updateContact(MarkerItem _markerItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, _markerItem.getTitle());
        values.put(KEY_LATITUDE, _markerItem.getLatitude());
        values.put(KEY_LONGITUDE, _markerItem.getLongitude());
        values.put(KEY_IS_CUSTOMIZED, _markerItem.isCustomized());
        values.put(KEY_IMAGE_URI, _markerItem.getImageURI());

        return db.update(TABLE_MARKERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(_markerItem.getID()) });
    }

    public void deleteContact(MarkerItem _markerItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARKERS, KEY_ID + " = ?",
                new String[]{String.valueOf(_markerItem.getID())});
        db.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MARKERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }

    public void deleteAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARKERS, null, null);
        db.close();
    }
}
