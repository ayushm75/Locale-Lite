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

import java.util.Calendar;
import java.util.Date;

public class chat extends AppCompatActivity {

    String n, m, c , cat;

    MessageClass m1;

    DatabaseReference databaseReference;

    EditText msg;
    Button send;

    String txt;

    RecyclerView rv;
    MyMessageAdapter mm;
    OtherMessageAdapter om;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        n = intent.getStringExtra("provider name");
        m = intent.getStringExtra("provider number");

        cat = n+","+m;

        rv = (RecyclerView) findViewById(R.id.rc);
        mm = new MyMessageAdapter();
        om = new OtherMessageAdapter();
        LinearLayoutManager ml = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false);
        ml.setStackFromEnd(true);
        rv.setLayoutManager(ml);


        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar5));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(n);

        msg = (EditText) findViewById(R.id.text);
        send = (Button) findViewById(R.id.send);

        m1 = new MessageClass();

        SharedPreferences ref = getSharedPreferences("Localite", MODE_PRIVATE);
        c = ref.getString("phone number of current user", null);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatHistory");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.child(c).child(cat).getChildren()) {
                    MessageClass mclass = ds.getValue(MessageClass.class);

                    if (!mclass.getConmsg().isEmpty()){
                        //Toast.makeText(chat.this , mclass.getConmsg() , Toast.LENGTH_SHORT).show();
                        rv.setAdapter(mm);
                        mm.addmsg(mclass);
                    }else if (!mclass.getPromsg().isEmpty()){
                        //Toast.makeText(chat.this , mclass.getPromsg() , Toast.LENGTH_SHORT).show();
                        rv.setAdapter(om);
                        om.addmsg(mclass);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt = msg.getText().toString().trim();

                Date d = Calendar.getInstance().getTime();

                String timestamp = String.valueOf(d);

                String day = timestamp.substring(0, 10);
                String time = timestamp.substring(11 , 19);

                String concatenated = n + "," + m;

                if (!txt.isEmpty()) {
                    m1.setConmsg(txt);
                    m1.setDay(day);
                    m1.setTime(time);
                    rv.setAdapter(mm);
                    mm.addmsg(m1);
                    databaseReference.child(c).child(concatenated).child(String.valueOf(d)).setValue(m1);
                } else {
                    Toast.makeText(chat.this, "Enter some text", Toast.LENGTH_SHORT).show();
                }

                msg.setText("");
            }
        });
    }
}
