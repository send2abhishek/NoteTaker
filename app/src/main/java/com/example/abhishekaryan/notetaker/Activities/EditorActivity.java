package com.example.abhishekaryan.notetaker.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishekaryan.notetaker.ContentProviders.NotesProvider;
import com.example.abhishekaryan.notetaker.Database.DataBaseHelper;
import com.example.abhishekaryan.notetaker.R;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    private String NoteFilter;
    private String OldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editor=(EditText)findViewById(R.id.editor_activity_editText);

        Intent intent=getIntent();
        Uri uri=intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        if(uri==null){

            action=Intent.ACTION_INSERT;
            setTitle("New Note");
        }
        else {

            action=Intent.ACTION_EDIT;
            NoteFilter=DataBaseHelper.TABLE_COLUMN_ID + "=" + uri.getLastPathSegment();
            Cursor cursor=getContentResolver().query(uri,DataBaseHelper.ALL_COLIMNS,NoteFilter,null,null);
            cursor.moveToNext();
            OldText=cursor.getString(cursor.getColumnIndex(DataBaseHelper.TABLE_COLUMN_NOTE_TEXT));
            editor.setText(OldText);
            editor.requestFocus();
        }
    }

    private void finishEditing(){

        String newText=editor.getText().toString().trim();
        switch (action){

            case Intent.ACTION_INSERT:
                if(newText.length()==0){
                    setResult(RESULT_CANCELED);
                }
                else{
                    insertNote(newText);
                }
                break;
 
            case Intent.ACTION_EDIT:
                if(newText.length()==0){
                   deleteNote();
                }
                else if(newText.equals(OldText)){
                    setResult(RESULT_CANCELED);
                }
                else {

                    updateNote(newText);
                }



        }
        finish();

    }

    private void deleteNote() {

        getContentResolver().delete(NotesProvider.CONTENT_URI,NoteFilter,null);
        Toast.makeText(this,"Note Deleted",Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void updateNote(String newText) {
        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBaseHelper.TABLE_COLUMN_NOTE_TEXT,newText);
        getContentResolver().update(NotesProvider.CONTENT_URI,contentValues,NoteFilter,null);
        Toast.makeText(this,"Note Updated",Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);

    }

    private void insertNote(String data) {

        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBaseHelper.TABLE_COLUMN_NOTE_TEXT,data);
        getContentResolver().insert(NotesProvider.CONTENT_URI,contentValues);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.editor_activity_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id){

            case R.id.editor_activity_menu_del:
                deleteNote();
                break;

        }


        return true;
    }
}
