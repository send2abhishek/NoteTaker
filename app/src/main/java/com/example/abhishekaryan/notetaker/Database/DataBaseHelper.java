package com.example.abhishekaryan.notetaker.Database;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.abhishekaryan.notetaker.Entities.Message;

public class DataBaseHelper extends SQLiteOpenHelper{

    private Context context;

    //constant for database name and version

    private static final String DATABASE_NAME="notes.db";
    private static final int DATABASE_VERSION=1;

        //constant for Table name  notes and Table columns

    public static final String TABLE_NAME="notes";
    public static final String TABLE_COLUMN_ID="_id";
    public static final String TABLE_COLUMN_NOTE_TEXT="noteText";
    public static final String TABLE_COLUMN_NOTE_CREATED="noteCreated";


    //constant for Table name  master and Table columns

    public static final String TABLE_NAME_MASTER="master";
    public static final String TABLE_MASTER_COLUMN_ID="_id";
    public static final String TABLE_MASTER_COLUMN_NAME="name";
    public static final String TABLE_MASTER_COLUMN_PASSWORD="password";
    public static final String TABLE_MASTER_COLUMN_EMAIL="email";
    public static final String TABLE_MASTER_COLUMN_PROFILE_CREATED="profileCreated";

    public static final String[] ALL_COLIMNS={
            TABLE_COLUMN_ID,
            TABLE_COLUMN_NOTE_TEXT,
            TABLE_COLUMN_NOTE_CREATED
    };

    //Table creation constant for table notes
    private static final String TABLE_CREATION_QUERY="CREATE TABLE " + TABLE_NAME + " ("
            + TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TABLE_COLUMN_NOTE_TEXT + " TEXT,"
            + TABLE_COLUMN_NOTE_CREATED + " TEXT DEFAULT CURRENT_TIMESTAMP" +
            " )";


    //Table creation constant for table master
    private static final String TABLE_CREATION_QUERY_MASTER_TABLE="CREATE TABLE " + TABLE_NAME_MASTER + " ("
            + TABLE_MASTER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TABLE_MASTER_COLUMN_NAME + " TEXT,"
            + TABLE_MASTER_COLUMN_PASSWORD + " TEXT,"
            + TABLE_MASTER_COLUMN_EMAIL + " TEXT,"
            + TABLE_MASTER_COLUMN_PROFILE_CREATED + " TEXT DEFAULT CURRENT_TIMESTAMP" +
            " )";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        //Message.messageDisplay(context,"Database Careated");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            db.execSQL(TABLE_CREATION_QUERY);
            db.execSQL(TABLE_CREATION_QUERY_MASTER_TABLE);
            Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e) {

            Toast.makeText(context, "Have Expection in creating table" +e, Toast.LENGTH_LONG).show();
            Log.e("DatabaseTagg",""+e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        try {
            Message.messageDisplay(context, "onUpgrade called");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
        catch (SQLException e) {

            Message.messageDisplay(context, "Have Expection in upgrading table" +e);
        }

    }
}
