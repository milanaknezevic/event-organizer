package com.example.eventorganizer.model.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eventorganizer.enums.Category;
import com.example.eventorganizer.model.entities.Event;
import com.example.eventorganizer.model.entities.Image;

import java.util.ArrayList;
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
    private static final String CATEGORY = "category";

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
            CATEGORY + " TEXT" + ")";

    private static final String create_image = "CREATE TABLE " + TABLE_NAME_IMAGE +
            " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT_ID + " INTEGER," +
            IMAGE + " TEXT," +
            " FOREIGN KEY (" + EVENT_ID + ") REFERENCES " + TABLE_NAME_EVENT + "(" + ID_COL + ")" +
            " ON DELETE CASCADE" +
            ")";
    private static final String SQL_DELETE_ENTRY_EVENT =
            "DROP TABLE IF EXISTS " + TABLE_NAME_EVENT;

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_event);
        sqLiteDatabase.execSQL(create_image);


    }

    public void deleteItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DROP TABLE IF EXISTS " + TABLE_NAME_IMAGE;
        db.execSQL(delete);


        SQLiteDatabase db2 = this.getWritableDatabase();
        String delete2 = "DROP TABLE IF EXISTS " + TABLE_NAME_EVENT;
        db.execSQL(delete2);
    }

    //azuriranje seme baze podataka prosljedjuje se stara i nova verzija
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRY_EVENT);
        //... za sve tabele ako treba
        onCreate(sqLiteDatabase);
    }


    //image insert, getAllForActivites
    public void insertImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE, image.getImage_url());
        values.put(EVENT_ID, image.getEvent_id());
        long imageId = db.insert(TABLE_NAME_IMAGE, null, values);
        image.setId((int) imageId);
        db.close();
    }

    @SuppressLint("Range")
    public List<Image> getAllImagesForEvent(int id) {
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

    //event insert update delete,getAll
    public long insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, event.getName());
        values.put(DESCRIPTION, event.getDescription());
        values.put(TIME, event.getTime());
        values.put(LOCATION, event.getLocation());
        values.put(CATEGORY, event.getCategory().toString());
        long eventId = db.insert(TABLE_NAME_EVENT, null, values);
        event.setId((int) eventId);
        db.close();
        return eventId;
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, event.getName());
        values.put(DESCRIPTION, event.getDescription());
        values.put(TIME, event.getTime());
        values.put(LOCATION, event.getLocation());
        values.put(CATEGORY, event.getCategory().toString());
        int rowsUpdated = db.update(TABLE_NAME_EVENT, values, ID_COL + " = " + event.getId(), null);
        db.close();
    }

    public void deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_EVENT, ID_COL + " = " + eventId, null);
        db.close();
    }

    public void deleteEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_EVENT, null, null);
        db.close();
    }


    @SuppressLint("Range")
    public List<Event> getAllEvents() {
        List<Event> events = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_EVENT + " ORDER BY TIME ASC", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Event eventTmp = new Event();
            eventTmp.setId(res.getInt(res.getColumnIndex(ID_COL)));
            eventTmp.setName(res.getString(res.getColumnIndex(EVENT_NAME)));
            eventTmp.setDescription(res.getString(res.getColumnIndex(DESCRIPTION)));
            eventTmp.setTime(res.getString(res.getColumnIndex(TIME)));
            eventTmp.setLocation(res.getString(res.getColumnIndex(LOCATION)));
            eventTmp.setCategory(Category.valueOf(res.getString(res.getColumnIndex(CATEGORY))));

            List<Image> images = getAllImagesForEvent(eventTmp.getId());
            eventTmp.setImages(images);

            events.add(eventTmp);
            res.moveToNext();
        }
        res.close();
        return events;
    }



    @SuppressLint("Range")
    public List<Event> searchEventsByName(String eventName) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_EVENT + " WHERE " + EVENT_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + eventName + "%"};
        Cursor res = db.rawQuery(selectQuery, selectionArgs);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Event eventTmp = new Event();
            eventTmp.setId(res.getInt(res.getColumnIndex(ID_COL)));
            eventTmp.setName(res.getString(res.getColumnIndex(EVENT_NAME)));
            eventTmp.setDescription(res.getString(res.getColumnIndex(DESCRIPTION)));
            eventTmp.setTime(res.getString(res.getColumnIndex(TIME)));
            eventTmp.setLocation(res.getString(res.getColumnIndex(LOCATION)));
            eventTmp.setCategory(Category.valueOf(res.getString(res.getColumnIndex(CATEGORY))));


            List<Image> images = getAllImagesForEvent(eventTmp.getId());
            eventTmp.setImages(images);

            events.add(eventTmp);
            res.moveToNext();
        }
        return events;
    }


}
