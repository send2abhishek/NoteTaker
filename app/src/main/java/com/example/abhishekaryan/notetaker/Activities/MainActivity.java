package com.example.abhishekaryan.notetaker.Activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.abhishekaryan.notetaker.Adapter.NotesCursorAdpater;
import com.example.abhishekaryan.notetaker.ContentProviders.NotesProvider;
import com.example.abhishekaryan.notetaker.Database.DataBaseHelper;
import com.example.abhishekaryan.notetaker.Entities.DataEntity;
import com.example.abhishekaryan.notetaker.R;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private static final int EDITOR_REQUEST_CODE =101 ;
    private ListView listView;
    private CursorAdapter adapter;
    private HashSet<Cursor> slectedNote;
    private ActionMode actionMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slectedNote=new HashSet<>();
        listView=(ListView)findViewById(R.id.activity_main_list_view);
        adapter=new NotesCursorAdpater(this,null,0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        getLoaderManager().initLoader(0,null,this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id){

            case R.id.activity_option_menu_sample_data:
                insertSampleData();
                break;
            case R.id.activity_option_menu_delete_data:
                DeleteAllNotes();
                break;

        }


        return true;
    }

    private void DeleteAllNotes() {

        AlertDialog dialog=new AlertDialog.Builder(this)
                .setMessage("Are you Sure to Delete All Notes ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getContentResolver().delete(NotesProvider.CONTENT_URI,null,null);
                        restartLoader();
                        Toast.makeText(MainActivity.this,"All notes Deleted",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel",null)
                .setTitle("Confirm Us")
                .show();

    }

    private void insertSampleData() {

        inserNote("Sample 1");
        inserNote("Sample 1 note \n note 2");
        inserNote("very long note with a lot of text which can exceeds " +
                "the width of the screen very long note with a lot of text which can exceeds the width of the screen");
        inserNote("Demo Data");
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }

    private void inserNote(String data) {

        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBaseHelper.TABLE_COLUMN_NOTE_TEXT,data);
        Uri noteUri=getContentResolver().insert(NotesProvider.CONTENT_URI,contentValues);
        Log.e("MainActivity",noteUri.getLastPathSegment());
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,NotesProvider.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public void NewNote(View view) {

        Intent intent=new Intent(this,EditorActivity.class);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==EDITOR_REQUEST_CODE && resultCode==RESULT_OK){
            restartLoader();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor=(Cursor)adapter.getItem(position);
        
        if(actionMode !=null){
            toggleCustomerSlection(cursor);
        }
        else {
            showCustomer(cursor);
        }

        Intent intent= new Intent(this,EditorActivity.class);
            Uri uri=Uri.parse(NotesProvider.CONTENT_URI + "/" +id);
        intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE,uri);
        startActivityForResult(intent,EDITOR_REQUEST_CODE);
    }

    private void showCustomer(Cursor cursor){

        //Toast.makeText(this,"showing customer ",Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor dataExtract=(Cursor)adapter.getItem(position);
        String data=dataExtract.getString(dataExtract.getColumnIndex(DataBaseHelper.TABLE_COLUMN_NOTE_TEXT));
        //Toast.makeText(this,"see text" + data,Toast.LENGTH_SHORT).show();
            toggleCustomerSlection(dataExtract);
        return true;
    }


    private void toggleCustomerSlection(Cursor dataExtract){

        if(slectedNote.contains(dataExtract)){

            slectedNote.remove(dataExtract);
        }
        else {
            slectedNote.add(dataExtract);
        }

        if(slectedNote.size()==0 && actionMode !=null){

            actionMode.finish();
            return;
        }
        if(actionMode==null){

            actionMode=startSupportActionMode(new DataEntityActionModeCallback());
        }
        else {

            //actionMode.invalidate();
        }
        //adapter.notifyDataSetChanged();

    }

    private class DataEntityActionModeCallback implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main_note,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {


            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int id=item.getItemId();
            if(id==R.id.menu_main_del_note){

                deleteCustomer(slectedNote);
                actionMode.finish();
                return true;
            }


            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            actionMode=null;
            slectedNote.clear();
            adapter.notifyDataSetChanged();

        }
    }

    private void deleteCustomer(Iterable<Cursor> slectedNote) {

        String NoteFilter;
        String extract;

        for(Cursor dataDel:slectedNote){

            extract=dataDel.getString(dataDel.getColumnIndex(DataBaseHelper.TABLE_COLUMN_ID));
            //Toast.makeText(this,"see text" + extract,Toast.LENGTH_SHORT).show();
            NoteFilter=DataBaseHelper.TABLE_COLUMN_ID + "=" + extract;
            getContentResolver().delete(NotesProvider.CONTENT_URI,NoteFilter,null);

        }
        adapter.notifyDataSetChanged();
        restartLoader();


    }
}
