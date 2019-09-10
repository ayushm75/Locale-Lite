package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Provider_front_page extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    String oname , ophone , otype;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView name , phone , addr , uv , dv;

    Provider p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_front_page);

        Intent i = getIntent();
        oname = i.getStringExtra("Username");
        ophone = i.getStringExtra("phone number");
        otype = i.getStringExtra("type");

        uv = (TextView)findViewById(R.id.uno);
        dv = (TextView)findViewById(R.id.dno);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle(oname);

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);

        if (!isloggedin)
            changeStatus(oname);
        else{
            oname = sharedPreferences.getString("NameP" , null);
            ophone = sharedPreferences.getString("phone number of current user" , null) ;
            otype = sharedPreferences.getString("type" , null);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Provider");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                p = dataSnapshot.child(otype).child(ophone).getValue(Provider.class);
                name = (TextView)findViewById(R.id.name);
                phone = (TextView)findViewById(R.id.phone);
                addr = (TextView)findViewById(R.id.addr);
                name.setText(oname);
                phone.setText("Phone No.:" + ophone);
                addr.setText( "Address" + p.getAddr());
                uv.setText(String.valueOf(p.getUpvotes()));
                dv.setText(String.valueOf(p.getDownvotes()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changeStatus(String a){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NameP" , a);
        editor.putBoolean("LoggedIn" , true);
        editor.putString("phone number of current user" , ophone);
        editor.putBoolean("Provider" , true);
        editor.putString("type" ,otype);
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

        Intent i = new Intent(Provider_front_page.this , ChatHistory.class);
        startActivity(i);
    }

    public void upload(View view) {
    }
}
