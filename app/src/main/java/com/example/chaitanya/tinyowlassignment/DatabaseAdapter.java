package com.example.chaitanya.tinyowlassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaitanya on 12-Oct-15.
 */
public class DatabaseAdapter {

    DatabaseHelper databaseHelper;
    public DatabaseAdapter(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertData(String nodeId, String message){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_NODEID, nodeId);
        contentValues.put(DatabaseHelper.COL_MESSAGE, message);
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        /*if(id >= 0)  Log.d("ZMY", "{"+nodeId+", "+message+"} is inserted successfully");
        else    Log.d("ZMY", "{"+nodeId+", "+message+"} is not inserted");*/
        return id;
    }

    public List<post> getPosts(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] columns = {DatabaseHelper.COL_MESSAGE, DatabaseHelper.COL_NODEID};
        String orderBy = DatabaseHelper.COL_NODEID + " DESC";
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, orderBy);

        List<post> posts = new ArrayList<post>();
        while(cursor.moveToNext()){
            posts.add(new post(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MESSAGE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NODEID))));
        }
        return posts;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "database";
        private static final String TABLE_NAME = "postsTable";
        private static final int DATABASE_VERSION = 2;

        private static final String COL_NODEID = "_nodeId";
        private static final String COL_MESSAGE = "message";

        private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_NODEID + " VARCHAR(256) PRIMARY KEY, " + COL_MESSAGE + " VARCHAR(1024));";
        private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d("ZMY", "DatabaseAdapter constructor is called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("ZMY", "Database is created");
            // CREATE TABLE TABLE_NAME (COL_NODEID VARCHAR(256), COL_MESSAGE VARCHAR(1024));
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            Log.d("ZMY", "Created table");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("ZMY", "database onUpgrade is called");
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
