package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

    CardAdapter c1;

    ListView cl;

    String p , n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_frontpage);

        Intent i = getIntent();
        n = i.getStringExtra("Username");
        p = i.getStringExtra("phone number");

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle(n);

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);

        c1 = new CardAdapter(this);
        cl = (ListView)findViewById(R.id.cardList);
        cl.setAdapter(c1);

        if (!isloggedin)
            changeStatus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Provider");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Card card = new Card(ds.getKey());
                        c1.newCard(card);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Card ctemp = (Card) c1.getItem(i);
                String a = ctemp.getType();
                if (a != null) {
                    Toast.makeText(Consumer_frontpage.this , a , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Consumer_frontpage.this, Pro_list.class);
                    intent.putExtra("Type", a);
                    intent.putExtra("username" , n);
                    startActivity(intent);
                }else{
                    Toast.makeText(Consumer_frontpage.this , "String is empty" , Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    public void changeStatus(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoggedIn" , true);
        editor.putString("phone number of current user" , p);

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
        editor.apply();

        Intent intent = new Intent(Consumer_frontpage.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void bringupchat(){

    }


}
