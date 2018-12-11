package com.example.abhishekaryan.notetaker.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.abhishekaryan.notetaker.Database.DataBaseHelper;
import com.example.abhishekaryan.notetaker.Entities.DataEntity;
import com.example.abhishekaryan.notetaker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotesCursorAdpater extends CursorAdapter {

    private ArrayList<DataEntity> datalist;


    public NotesCursorAdpater(Context context, Cursor c, int flags) {
        super(context, c, flags);

        datalist=new ArrayList<>();
    }

    public ArrayList<DataEntity> getDatalist() {
        return datalist;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        String noteText=cursor.getString(cursor.getColumnIndex(DataBaseHelper.TABLE_COLUMN_NOTE_TEXT));
        String noteCreationDate=cursor.getString(cursor.getColumnIndex(DataBaseHelper.TABLE_COLUMN_NOTE_CREATED));
        SimpleDateFormat sdf = new SimpleDateFormat(noteCreationDate);
        String dateText= DateUtils.formatDateTime(context,sdf.getCalendar().getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE);
        int pos=noteText.indexOf(10);
        if(pos !=-1){
            noteText=noteText.substring(0,pos) + "....";
        }
        dateText=dateText.substring(0,6);

        TextView textViewNote= (TextView) view.findViewById(R.id.note_list_item_textView);
        TextView textViewNoteCreated= (TextView) view.findViewById(R.id.note_list_item_creation_date);
        textViewNote.setText(noteText);
        textViewNoteCreated.setText(dateText);
        datalist.add(new DataEntity(noteText,dateText));

    }
}
