package com.example.abhishekaryan.notetaker.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.abhishekaryan.notetaker.R;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.activity_login_user_editText);
        password=(EditText)findViewById(R.id.activity_login_user_editText);
        login=(Button)findViewById(R.id.activity_login_btn_login);
    }
}
