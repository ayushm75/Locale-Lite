package com.example.localite;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ConsumerSignIn extends AppCompatActivity {

    TextInputLayout l1 ,l2,l3,l4 , l5;

    EditText cName;
    EditText cAddr;
    EditText cPhone;
    EditText cEmail;
    EditText cPwd;
    Button cSignin;
    Consumer c;

    ProgressDialog dialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String txtName;
    String txtAddr;
    String txtPhone;
    String txtEmail;
    String txtPwd;
    String p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_sign_in);
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);

        cSignin = (Button)findViewById(R.id.csignin);

        cSignin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                cName = (EditText)findViewById(R.id.cname);
                cAddr = (EditText)findViewById(R.id.caddr);
                cPhone = (EditText)findViewById(R.id.cphone);
                cEmail = (EditText)findViewById(R.id.cemail);
                cPwd = (EditText)findViewById(R.id.cpwd);

                databaseReference = firebaseDatabase.getReference("Consumer");

                txtName = cName.getText().toString().trim();
                txtAddr = cAddr.getText().toString().trim();
                txtPhone = cPhone.getText().toString().trim();
                txtEmail = cEmail.getText().toString().trim();
                txtPwd = cPwd.getText().toString().trim();

                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                assert digest != null;
                byte[] pp = digest.digest(txtPwd.getBytes(StandardCharsets.UTF_8));
                p= Arrays.toString(pp);

                if (txtName.isEmpty()){
                    cName.setError("Name is empty!");
                    cName.requestFocus();
                    return;
                }

                if (txtAddr.isEmpty()){
                    cAddr.setError("Address is empty!");
                    cAddr.requestFocus();
                    return;
                }

                if (txtPhone.isEmpty() && txtPhone.length()<10){
                    cPhone.setError("Enter correct phone number!");
                    cPhone.requestFocus();
                    return;
                }

                if (txtEmail.isEmpty()){
                    cEmail.setError("Email is empty!");
                    cEmail.requestFocus();
                    return;
                }

                if (txtPwd.isEmpty()){
                    cPwd.setError("Password is empty!");
                    cPwd.requestFocus();
                    return;
                }

                if (txtPwd.length()<6)
                {
                    cPwd.setError("Enter at least 6 length password!");
                    cPwd.requestFocus();
                    return;
                }

                dialog.setMessage("Registering...");
                dialog.show();

                savePreferences(txtName ,p);

                c = new Consumer(txtName , txtPhone , txtEmail , txtAddr , p);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(txtName).exists()){

                            Toast.makeText(getApplicationContext(),"User already registered!",Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }else{

                            databaseReference.child(txtName).setValue(c);
                            Toast.makeText(getApplicationContext(),"Registration Successful!",Toast.LENGTH_LONG).show();
                            dialog.cancel();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(),"Process Cancelled!",Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
            }
        });
    }

    public void savePreferences(String a ,String b){

        SharedPreferences sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name" , a);
        editor.putString("Password" , b);
        editor.putBoolean("LoggedIn" , false);
        editor.putString("Type" , "Consumer");
        editor.commit();
    }
}
