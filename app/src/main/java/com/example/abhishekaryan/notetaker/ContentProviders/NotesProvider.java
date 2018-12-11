package com.example.abhishekaryan.notetaker.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.abhishekaryan.notetaker.Database.DataBaseHelper;


public class NotesProvider extends ContentProvider {

    private static final String AUTHORITY="com.example.abhishekaryan.notetaker.contentprovider.notesprovider";
    private static final String BASE_PATH="notes";
    public static final Uri CONTENT_URI=Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    //constant to identify requested operation
    private static final int NOTES=1;
    private static final int NOTES_ID=2;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    public static final String CONTENT_ITEM_TYPE="Note";

    static {

        uriMatcher.addURI(AUTHORITY,BASE_PATH,NOTES);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",NOTES_ID);
    }



        private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DataBaseHelper helper=new DataBaseHelper(getContext());
        database=helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if(uriMatcher.match(uri)==NOTES_ID){
            selection=DataBaseHelper.TABLE_COLUMN_ID + "=" + uri.getLastPathSegment();
        }


        return database.query(DataBaseHelper.TABLE_NAME,DataBaseHelper.ALL_COLIMNS,selection,null,null,null,
                DataBaseHelper.TABLE_COLUMN_NOTE_CREATED + " DESC"
                );

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

      long id = database.insert(DataBaseHelper.TABLE_NAME,null,values);
        return Uri.parse(BASE_PATH + "/" +id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DataBaseHelper.TABLE_NAME,selection,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DataBaseHelper.TABLE_NAME,values,selection,selectionArgs);
    }
}
