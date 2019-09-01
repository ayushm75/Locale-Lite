package com.example.localite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class Consumer_frontpage extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_frontpage);

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);

        if (!isloggedin)
            changeStatus();
    }

    public void changeStatus(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoggedIn" , true);

        if (editor.commit()){
            Toast.makeText(Consumer_frontpage.this , "Commit successful" , Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Consumer_frontpage.this , "Commit unsuccessful" , Toast.LENGTH_LONG).show();
        }
    }
}
