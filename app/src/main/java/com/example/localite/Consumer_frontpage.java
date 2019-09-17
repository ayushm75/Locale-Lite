package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Consumer_frontpage extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String p , n;

    private CardView doc , elec , phar , plum , carp , others;
    private TextView c;

    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_consumer_layout);

        Intent i = getIntent();
        //n = i.getStringExtra("Username");
        p = i.getStringExtra("phone number");

        c = (TextView)findViewById(R.id.con_name);
        c.setText(n);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar10));
        //getSupportActionBar().setTitle(n);

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);
        n = sharedPreferences.getString("NameC" , " ");

        if (!isloggedin)
            changeStatus(n);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Provider");

        doc = (CardView)findViewById(R.id.doctor);
        elec = (CardView)findViewById(R.id.electrician);
        phar = (CardView)findViewById(R.id.pharmacy);
        plum = (CardView)findViewById(R.id.pharmacy);
        carp = (CardView)findViewById(R.id.carpenter);
        others = (CardView)findViewById(R.id.others);

    }

    @Override
    protected void onStart() {
        super.onStart();

        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //if (dataSnapshot.hasChild("Doctor")) {
                            //Toast.makeText(Consumer_frontpage.this , "user of such type exists" , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Consumer_frontpage.this , Pro_list.class);
                            i.putExtra("username" , n);
                            i.putExtra("Type" , "Doctor");
                            startActivity(i);
                        /*}else{
                            Toast.makeText(Consumer_frontpage.this , "No user of such type exists" , Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        phar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //if (dataSnapshot.hasChild("Pharmacist")) {
                            //Toast.makeText(Consumer_frontpage.this , "user of such type exists" , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Consumer_frontpage.this , Pro_list.class);
                            i.putExtra("username" , n);
                            i.putExtra("Type" , "Pharmacist");
                            startActivity(i);
                        /*}else{
                            Toast.makeText(Consumer_frontpage.this , "No user of such type exists" , Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //if (dataSnapshot.hasChild("Electrician")) {
                            //Toast.makeText(Consumer_frontpage.this , "user of such type exists" , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Consumer_frontpage.this , Pro_list.class);
                            i.putExtra("username" , n);
                            i.putExtra("Type" , "Electrician");
                            startActivity(i);
                        /*}else{
                            Toast.makeText(Consumer_frontpage.this , "No user of such type exists" , Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        carp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //if (dataSnapshot.hasChild("Carpenter")) {
                            //Toast.makeText(Consumer_frontpage.this , "user of such type exists" , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Consumer_frontpage.this , Pro_list.class);
                            i.putExtra("username" , n);
                            i.putExtra("Type" , "Carpenter");
                            startActivity(i);
                        /*}else{
                            Toast.makeText(Consumer_frontpage.this , "No user of such type exists" , Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        plum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //if (dataSnapshot.hasChild("Plumber")) {
                            //Toast.makeText(Consumer_frontpage.this , "user of such type exists" , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Consumer_frontpage.this , Pro_list.class);
                            i.putExtra("username" , n);
                            i.putExtra("Type" , "Plumber");
                            startActivity(i);
                        /*}else{
                            Toast.makeText(Consumer_frontpage.this , "No user of such type exists" , Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //if (dataSnapshot.hasChild("Others")) {
                            //Toast.makeText(Consumer_frontpage.this , "user of such type exists" , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Consumer_frontpage.this , Pro_list.class);
                            i.putExtra("username" , n);
                            i.putExtra("Type" , "Others");
                            startActivity(i);
                        /*}else{
                            Toast.makeText(Consumer_frontpage.this , "No user of such type exists" , Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void changeStatus(String a){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoggedIn" , true);
        editor.putString("NameC" , a);
        editor.putString("phone number of current user" , p);
        editor.putBoolean("Consumer" , true);
        editor.commit();

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
        editor.putString("phone number of current user" , null);
        editor.putBoolean("Consumer" , false);
        editor.commit();

        Intent intent = new Intent(Consumer_frontpage.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void bringupchat(){

        Intent i = new Intent(Consumer_frontpage.this , ChatHistory.class);
        startActivity(i);
    }


}
