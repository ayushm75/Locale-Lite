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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ProviderSignIn extends AppCompatActivity {

    EditText oName;
    EditText type;
    EditText bName;
    EditText addr;
    EditText email;
    EditText phone;
    EditText pwd;
    Button psignin;

    ProgressDialog dialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    String txtName;
    String txtType;
    String txtbName;
    String txtAddr;
    String txtPhone;
    String txtEmail;
    String txtPwd;
    String p;
    double lat;
    double lon;

    Provider pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_sign_in);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        oName = (EditText)findViewById(R.id.cname);
        type = (EditText)findViewById(R.id.ptype);
        bName = (EditText)findViewById(R.id.bcname);
        addr = (EditText)findViewById(R.id.caddr);
        phone = (EditText)findViewById(R.id.cphone);
        email = (EditText)findViewById(R.id.cemail);
        pwd = (EditText)findViewById(R.id.cpwd);
        psignin = (Button)findViewById(R.id.csignin);

        firebaseDatabase = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);

        psignin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                txtName = oName.getText().toString().trim();
                txtType = type.getText().toString().trim();
                txtbName = bName.getText().toString().trim();
                txtAddr = addr.getText().toString().trim();
                txtPhone = phone.getText().toString().trim();
                txtEmail = email.getText().toString().trim();
                txtPwd = pwd.getText().toString().trim();

                databaseReference = firebaseDatabase.getReference("Provider");

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
                    oName.setError("Name is empty!");
                    oName.requestFocus();
                    return;
                }

                if (txtType.isEmpty()){
                    type.setError("Bussiness name is empty!");
                    type.requestFocus();
                    return;
                }

                if (txtbName.isEmpty()){
                    bName.setError("Bussiness name is empty!");
                    bName.requestFocus();
                    return;
                }

                if (txtAddr.isEmpty()){
                    addr.setError("Address is empty!");
                    addr.requestFocus();
                    return;
                }

                if (txtPhone.isEmpty() && txtPhone.length()<10){
                    phone.setError("Enter correct phone number!");
                    phone.requestFocus();
                    return;
                }

                if (txtEmail.isEmpty()){
                    email.setError("Email is empty!");
                    email.requestFocus();
                    return;
                }

                if (txtPwd.isEmpty()){
                    pwd.setError("Password is empty!");
                    pwd.requestFocus();
                    return;
                }

                if (txtPwd.length()<6)
                {
                    pwd.setError("Enter at least 6 length password!");
                    pwd.requestFocus();
                    return;
                }

                dialog.setMessage("Registering...");
                dialog.show();

                savePreferences(txtName ,p);

                pr = new Provider(txtName , txtType , txtbName , txtAddr , txtPhone , txtEmail , p , lat , lon , 0 , 0 , false);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                          if (dataSnapshot.child(txtType).exists()){
                              if (dataSnapshot.child(txtType).child(txtName).exists()){

                                  Toast.makeText(getApplicationContext(),"Provider already registered!",Toast.LENGTH_LONG).show();
                                  dialog.cancel();
                              }else{
                                  databaseReference.child(txtType).child(txtName).setValue(pr);
                                  Toast.makeText(getApplicationContext(),"Registration Successful!",Toast.LENGTH_LONG).show();
                                  dialog.cancel();
                                  finish();
                              }
                          }else{

                              databaseReference.child(txtType).child(txtName).setValue(pr);
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
        editor.putBoolean("Provider" , true);
        editor.commit();
    }
}
