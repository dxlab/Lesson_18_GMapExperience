package ua.com.dxlab.lesson_18_gmapexperience.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        MarkerItem contactItem = new MarkerItem(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return contactItem;
    }
}
