package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Provider_front_page extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_front_page);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2));

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);

        if (!isloggedin)
            changeStatus();
    }

    public void changeStatus(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoggedIn" , true);

        if (editor.commit()){
            Toast.makeText(Provider_front_page.this , "Commit successful" , Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Provider_front_page.this , "Commit unsuccessful" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.over_flow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){


            case R.id.signout:
                signmeout();
                return true;
            case R.id.chat_history:
                bringupchat();
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    public void signmeout(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoggedIn" , false);
        editor.apply();

        Intent intent = new Intent(Provider_front_page.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void bringupchat(){

    }
}
