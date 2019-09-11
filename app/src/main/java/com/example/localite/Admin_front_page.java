package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_front_page extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    DatabaseReference databaseReference;
    ListView lv;
    listadapter la;

    String title , uname;

    ArrayList<String> key = new ArrayList<String>();

    Provider p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_front_page);

        Intent i = getIntent();
        uname = i.getStringExtra("username");

        int len = uname.length();

        title = uname.substring(2 , len-1);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar6));
        getSupportActionBar().setTitle(title);

        la = new listadapter(key ,this);
        lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(la);

        databaseReference = FirebaseDatabase.getInstance().getReference("AdminWork");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.child(uname).getChildren()){
                    key.add(ds.getKey());
                }

                la.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                      String a = (String) la.getItem(i);
                      Intent intent = new Intent(Admin_front_page.this , deciding_place.class);
                      intent.putExtra("key" , a);
                      intent.putExtra("title" , title);
                      intent.putExtra("uname" , uname);
                      startActivity(intent);
            }
        });
    }
}
