package com.example.localite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class deciding_place extends AppCompatActivity {

    private TextView name , type , phone ,  email ,  addr;
    private ImageView doc;

    DatabaseReference databaseReference , databaseReference2;

    private String key;
    private String n;
    private String title;
    private String uname;

    private Provider p;

    StorageReference ref1 , ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deciding_place);

        Intent i = getIntent();
        key = i.getStringExtra("key");
        title = i.getStringExtra("title");
        uname = i.getStringExtra("uname");

        name = (TextView)findViewById(R.id.name);
        type = (TextView)findViewById(R.id.type);
        phone = (TextView)findViewById(R.id.phone);
        email = (TextView)findViewById(R.id.email);
        addr = (TextView)findViewById(R.id.addr);
        doc = (ImageView)findViewById(R.id.doc);

        n = getkey(key);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar8));
        getSupportActionBar().setTitle(title);

        databaseReference = FirebaseDatabase.getInstance().getReference("AdminWork");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Provider");

        ref1 = FirebaseStorage.getInstance().getReference("AuthProvider");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  p = dataSnapshot.child(uname).child(key).getValue(Provider.class);
                  name.setText(p.getOwnerName());
                  type.setText(p.getBussType());
                  phone.setText(p.phone);
                  email.setText(p.getEmail());
                  addr.setText(p.getAddr());
                  Picasso.get().load(p.getmImageUri()).fit().centerCrop().into(doc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFragment ifag = new imageFragment();
                ifag.getUri(p.getmImageUri());
                ifag.show(getSupportFragmentManager() , "121");
            }
        });*/
    }

    private String getkey(String key) {

        int index = key.indexOf(",");
        String nn = key.substring(0 , index);

        return nn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.admin , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){


            case R.id.approve:
                approve();
                return true;
                
            case R.id.disapprove:
                disapprove();
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void disapprove() {

        databaseReference.child(uname).child(key).removeValue();
        databaseReference2.child(p.getBussType()).child(p.getPhone()).removeValue();
    }

    private void approve() {

        databaseReference.child(uname).child(key).removeValue();
        p.setAuth(true);
        databaseReference2.child(p.getBussType()).child(p.getPhone()).setValue(p);
    }
}
