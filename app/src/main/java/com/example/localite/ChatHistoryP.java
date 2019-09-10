package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatHistoryP extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager llm;
    historyadapterP ha;

    String c , n , m;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        rv = (RecyclerView)findViewById(R.id.rv2);
        llm = new LinearLayoutManager(this);
        ha = new historyadapterP(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(ha);

        SharedPreferences ref = getSharedPreferences("Localite", MODE_PRIVATE);
        c = ref.getString("phone number of current user", null);

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatHistory");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.child(c).getChildren()){
                    String nm = split_name(ds.getKey());
                    //Toast.makeText(ChatHistory.this , nm , Toast.LENGTH_SHORT).show();
                    ha.addname(nm , m);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String split_name(String a){

        int i = a.indexOf(",");
        n = a.substring(0 , i);
        m = a.substring(i+1);
        return n;
    }
}
