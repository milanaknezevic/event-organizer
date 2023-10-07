package com.example.eventorganizer.model.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eventorganizer.model.entities.Event;
import com.example.eventorganizer.model.entities.Image;

import java.util.LinkedList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "events_data.db";
    public static final Integer DATABASE_VERSION = 1;
    private static final String TABLE_NAME_EVENT = "event";
    private static final String EVENT_NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "location";
    private static final String TIME = "time";
    private static final String CATEGORY_ID = "category_id";

    private static final String TABLE_NAME_CATEGORY = "category";
    private static final String CATEGORY_NAME = "name";
    private static final String TABLE_NAME_IMAGE = "image";
    private static final String EVENT_ID = "event_id";
    private static final String IMAGE = "image_url";

    private static final String ID_COL = "id";
    private static final String create_event = "CREATE TABLE " + TABLE_NAME_EVENT + " (" +
            ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT_NAME + " TEXT," +
            DESCRIPTION + " TEXT," +
            LOCATION + " TEXT," +
            TIME + " REAL DEFAULT 0," +
            CATEGORY_ID + " INTEGER," +  // Dodajte kolonu za ID kategorije
            " FOREIGN KEY (" + CATEGORY_ID + ") REFERENCES " + TABLE_NAME_CATEGORY + "(" + ID_COL + ")" +
            ")";

    private static final String create_image = "CREATE TABLE " + TABLE_NAME_IMAGE +
            " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT_ID + " INTEGER," +
            IMAGE + " TEXT," +
            " FOREIGN KEY (" + EVENT_ID + ") REFERENCES " + TABLE_NAME_EVENT + "(" + ID_COL + ")" +
            " ON DELETE CASCADE" +
            ")";


    private static final String create_category = "CREATE TABLE " + TABLE_NAME_CATEGORY +
            " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CATEGORY_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRY_EVENT =
            "DROP TABLE IF EXISTS " + TABLE_NAME_EVENT;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_event);
        sqLiteDatabase.execSQL(create_category);
        sqLiteDatabase.execSQL(create_image);


    }

    //azuriranje seme baze podataka prosljedjuje se stara i nova verzija
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRY_EVENT);
        //... za sve tabele ako treba
        onCreate(sqLiteDatabase);
    }


//image insert, getAllForActivites
    public void insertImage(Image image,Integer idEvent) {
        //...treba dodati u event sliku
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE, image.getImage_url());
        values.put(EVENT_ID, image.getEvent_id());
        db.close();
    }

    @SuppressLint("Range")
    public List<Image> getAllImages(int id) {
        List<Image> images = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_IMAGE + " where " + EVENT_ID + "=" + id, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
                Image imageTmp = new Image();
                imageTmp.setImage_url(res.getString(res.getColumnIndex(IMAGE)));

                images.add(imageTmp);
                res.moveToNext();
        }

        res.close();
        return images;
    }
    //event insert update delete
    public void insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, event.getName());
        values.put(DESCRIPTION, event.getDescription());
        values.put(TIME, event.getTime());
        values.put(LOCATION, event.getLocation());
        values.put(CATEGORY_ID, event.getCategory_id());
        db.close();
    }
}
