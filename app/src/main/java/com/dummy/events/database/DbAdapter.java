package com.dummy.events.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by Saksham on 23-Mar-17.
 */

public class DbAdapter extends SQLiteOpenHelper {
    public DbAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "eventsDb", factory, version);
    }

    public DbAdapter(Context context)
    {
        super(context,"eventDb",null,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table = "create table events (AID integer primary key autoincrement," +
                "title varchar(50), signstate varchar(10) , signemail varchar(25)," +
                "image blob,category varchar(50),comments varchar(1000),description varchar(1000) );";

        String table1 = "create table comments ( _id integer ," +
                "AID integer primary key autoincrement,comment varchar(1000) )";


        db.execSQL(table);
        db.execSQL(table1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void anyQuery(String query)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void insert(Vo object)
    {
        ContentValues cv = new ContentValues();
        cv.put("title",object.getTitle());
        cv.put("signstate",object.getSignState());
        cv.put("signemail",object.getSignEmail());
        cv.put("image",object.getImage());
        cv.put("category",object.getCategory());
        cv.put("comments",object.getComments());
        cv.put("description",object.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("events",null,cv);
    }

    public void insertComments(StringVo vo)
    {
        ContentValues cv = new ContentValues();
        cv.put("_id",vo.getId());
        cv.put("comment",vo.getComment());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("comments",null,cv);
    }

    public Cursor fetch(String query)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query,null);
    }
}
