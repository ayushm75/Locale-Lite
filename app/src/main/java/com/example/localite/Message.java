package com.example.localite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Message extends AppCompatActivity {

    String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        n = intent.getStringExtra("provider name");

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar5));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(n);
    }
}
