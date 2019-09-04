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

public class Consumer_frontpage extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_frontpage);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2));

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);

        if (!isloggedin)
            changeStatus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Producer");
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

        Intent intent = new Intent(Consumer_frontpage.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void bringupchat(){

    }
}
