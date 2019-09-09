package com.example.localite;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Provider_log_in extends AppCompatActivity {

    EditText phone;
    EditText type;
    EditText password;
    Button login;

    SharedPreferences sharedPreferences;

    String txtphone;
    String txttype;
    String txtpwd;
    String p;

    ProgressDialog dialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_log_in);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phone = (EditText)findViewById(R.id.phone);
        type = (EditText)findViewById(R.id.ctype);
        password = (EditText)findViewById(R.id.cpwd);
        login = (Button)findViewById(R.id.csignin);

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);

        dialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                txtphone = phone.getText().toString().trim();
                txttype = type.getText().toString().trim();
                txtpwd = password.getText().toString().trim();

                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                assert digest != null;
                byte[] pp =  digest.digest(txtpwd.getBytes(StandardCharsets.UTF_8));
                p= Arrays.toString(pp);

                if (txtphone.isEmpty() && txtphone.length()<10){
                    phone.setError("Enter correct phone number!");
                    phone.requestFocus();
                    return;
                }

                if (txtpwd.isEmpty()){
                    password.setError("Password is empty!");
                    password.requestFocus();
                    return;
                }

                if (txtpwd.length()<6)
                {
                    password.setError("Enter at least 6 length password!");
                    password.requestFocus();
                    return;
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("Provider");


                dialog.setMessage("Logging user in");
                dialog.setCancelable(false);
                dialog.show();

                login(txtphone ,txttype ,p);

            }
        });
    }

    public void login(final String a ,final String t , final String b){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(t).exists()){

                    if (dataSnapshot.child(t).child(a).exists()){
                        Provider c = dataSnapshot.child(t).child(a).getValue(Provider.class);

                        if (c.getPwd().equals(b)){

                            Intent intent = new Intent(Provider_log_in.this , Consumer_frontpage.class);
                            intent.putExtra("Username" , a);
                            dialog.dismiss();
                            startActivity(intent);
                            finish();
                        }else{
                            dialog.dismiss();
                            Toast.makeText(Provider_log_in.this , "Password is wrong" , Toast.LENGTH_LONG).show();
                            return;
                        }
                    }else{

                        dialog.dismiss();
                        Toast.makeText(Provider_log_in.this , "Provider not registered" , Toast.LENGTH_LONG).show();
                        return;
                    }
                }else{

                    dialog.dismiss();
                    Toast.makeText(Provider_log_in.this , "Provider type not registered" , Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                dialog.dismiss();
                Toast.makeText(Provider_log_in.this , "Something went wrong" , Toast.LENGTH_LONG).show();
                return;

            }
        });

    }
}
