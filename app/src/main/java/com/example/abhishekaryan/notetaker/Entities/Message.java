package com.example.abhishekaryan.notetaker.Entities;

import android.content.Context;
import android.widget.Toast;

public class Message {

    public static void messageDisplay(Context context, String msg){

        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
