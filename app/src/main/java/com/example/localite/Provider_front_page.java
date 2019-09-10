package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Provider_front_page extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    String oname;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TabLayout tl;
    ViewPager vp;
    SectionPageAdapter spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_front_page);

        Intent i = getIntent();
        oname = i.getStringExtra("Username");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle(oname);

        //tl = (TabLayout)findViewById(R.id.tl);
        ////vp = (ViewPager)findViewById(R.id.vp);
       // spa = new SectionPageAdapter(getSupportFragmentManager());
       // vp.setAdapter(spa);
       // tl.setupWithViewPager(vp);

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);

        if (!isloggedin)
            changeStatus(oname);
    }

    public void changeStatus(String a){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NameP" , a);
        editor.putBoolean("LoggedIn" , true);
        editor.putBoolean("Provider" , true);
        editor.commit();

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
