package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class chatp extends AppCompatActivity {
    String n, m, c, j , cat;

    MessageClass m1;

    DatabaseReference databaseReference;

    Boolean whoami;

    EditText msg;
    Button send;

    String txt;

    RecyclerView rv;
    MyMessageAdapter mm;
    OtherMessageAdapter om;

    ArrayList<MessageClass> mc = new ArrayList<MessageClass>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        n = intent.getStringExtra("consumer name");
        m = intent.getStringExtra("consumer number");

        cat = n+","+m;

        rv = (RecyclerView) findViewById(R.id.rc);
        //mm = new MyMessageAdapter(mc);
        om = new OtherMessageAdapter(mc);
        LinearLayoutManager ml = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        ml.setStackFromEnd(true);
        rv.setLayoutManager(ml);
        rv.setAdapter(om);


        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar5));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(n);

        msg = (EditText) findViewById(R.id.text);
        send = (Button) findViewById(R.id.send);

        m1 = new MessageClass();

        SharedPreferences ref = getSharedPreferences("Localite", MODE_PRIVATE);
        c = ref.getString("phone number of current user", null);
        j = ref.getString("NameP" , null);
        //whoami = ref.getBoolean("Consumer" , false);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatHistory");


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.child(c).child(cat).getChildren()) {
                    MessageClass mclass = ds.getValue(MessageClass.class);
                    mc.add(mclass);

                }

                om.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m1.count++;

                txt = msg.getText().toString().trim();

                Date d = Calendar.getInstance().getTime();

                String timestamp = String.valueOf(d);

                String day = timestamp.substring(0, 10);
                String time = timestamp.substring(11 , 19);

                String concatenated = n + "," + m;
                String concatenated2 = j+","+c;

                if (!txt.isEmpty()) {

                    m1.setPromsg(txt);
                    //m1.setPromsg(null);
                    m1.setDay(day);
                    m1.setTime(time);
                    mc.add(m1);
                    //mm.notifyDataSetChanged();
                    databaseReference.child(c).child(concatenated).child(String.valueOf(m1.count)).setValue(m1);
                    databaseReference.child(m).child(concatenated2).child(String.valueOf(m1.count)).setValue(m1);
                } else {
                    Toast.makeText(chatp.this, "Enter some text", Toast.LENGTH_SHORT).show();
                }

                msg.setText("");
            }
        });
    }
}

