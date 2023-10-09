package com.example.eventorganizer.model.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eventorganizer.model.entities.Category;
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
            CATEGORY_ID + " INTEGER," +
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
    public void insertEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, event.getName());
        values.put(DESCRIPTION, event.getDescription());
        values.put(TIME, event.getTime());
        values.put(LOCATION, event.getLocation());
        values.put(CATEGORY_ID, event.getCategory_id());
        long eventId = db.insert(TABLE_NAME_EVENT, null, values);
        event.setId((int) eventId);
        db.close();
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, event.getName());
        values.put(DESCRIPTION, event.getDescription());
        values.put(TIME, event.getTime());
        values.put(LOCATION, event.getLocation());
        values.put(CATEGORY_ID, event.getCategory_id());
        int rowsUpdated = db.update(TABLE_NAME_EVENT, values, ID_COL + " = " + event.getId(), null);
        db.close();
    }

    public void deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_EVENT, ID_COL + " = " + eventId, null);
        db.close();
    }
    @SuppressLint("Range")
    public List<Event> getAllEvents() {
        List<Event> events = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_EVENT, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Event eventTmp = new Event();
            eventTmp.setId(res.getInt(res.getColumnIndex(ID_COL)));
            eventTmp.setName(res.getString(res.getColumnIndex(EVENT_NAME)));
            eventTmp.setDescription(res.getString(res.getColumnIndex(DESCRIPTION)));
            eventTmp.setTime(res.getString(res.getColumnIndex(TIME)));
            eventTmp.setLocation(res.getString(res.getColumnIndex(LOCATION)));
            eventTmp.setCategory_id(res.getInt(res.getColumnIndex(CATEGORY_ID)));

            List<Image> images = getAllImagesForEvent(eventTmp.getId());
            eventTmp.setImages(images);

            events.add(eventTmp);
            res.moveToNext();
        }
        res.close();
        return events;
    }

    @SuppressLint("Range")
    public List<Event> searchEventsByName(String eventName)
    {
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
            eventTmp.setCategory_id(res.getInt(res.getColumnIndex(CATEGORY_ID)));

            List<Image> images = getAllImagesForEvent(eventTmp.getId());
            eventTmp.setImages(images);

            events.add(eventTmp);
            res.moveToNext();
        }
        return events;
    }

    //category insert,delete
    public void insertCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getName());
        long categoryId = db.insert(TABLE_NAME_CATEGORY, null, values);
        category.setId((int) categoryId);
        db.close();
    }
    public void deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_CATEGORY, ID_COL + " = " + categoryId, null);
        db.close();
    }

    @SuppressLint("Range")
    public List<Category> getAllCategories() {
        List<Category> categories = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_CATEGORY, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Category categoryTmp = new Category();
            categoryTmp.setId(res.getInt(res.getColumnIndex(ID_COL)));
            categoryTmp.setName(res.getString(res.getColumnIndex(CATEGORY_NAME)));

            categories.add(categoryTmp);
            res.moveToNext();
        }
        res.close();
        return categories;
    }
}
