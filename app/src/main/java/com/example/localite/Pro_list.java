package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pro_list extends AppCompatActivity {

    ListView l;
    provideradapter pa;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_list);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar4));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pa = new provideradapter(this);

        final Intent intent = getIntent();
        final String a = intent.getStringExtra("Type");

        l = (ListView)findViewById(R.id.lv);
        l.setAdapter(pa);

        databaseReference = FirebaseDatabase.getInstance().getReference("Provider");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.child(a).getChildren()){
                    Provider pp = ds.getValue(Provider.class);
                    pa.newpro(pp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
