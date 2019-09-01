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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity{

    EditText name , password;
    Button login;
    TextView newUser;
    SharedPreferences sharedPreferences;

    String txtName;
    String txtpwd;
    String p;
    String type = "";

    ProgressDialog dialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    CheckBox consumer;
    CheckBox provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        newUser  = (TextView)findViewById(R.id.newuser);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar));

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);

        dialog = new ProgressDialog(this);

        consumer = (CheckBox)findViewById(R.id.consumer);
        provider = (CheckBox)findViewById(R.id.provider);

        if (sharedPreferences != null){
            if (checkIfLoggedIn()){

                String name = sharedPreferences.getString("Name" , null);
                String type = sharedPreferences.getString("Type" , null);

                if (type.equals("Consumer")){
                    Intent intent = new Intent(MainActivity.this , Consumer_frontpage.class);
                    intent.putExtra("Username" , name);
                    startActivity(intent);
                    finish();
                }else if (type.equals("Provider")){
                    Intent intent = new Intent(MainActivity.this , Provider_front_page.class);
                    intent.putExtra("Username" , name);
                    startActivity(intent);
                    finish();
                }

            }
        }

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this , "Clickable" , Toast.LENGTH_LONG).show();

                ConsumerOrProvider cp = new ConsumerOrProvider();

                cp.show(getSupportFragmentManager() , "123");

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                txtName = name.getText().toString().trim();
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

                if (consumer.isChecked()){

                    type = "Consumer";
                }else if (provider.isChecked()){
                    type = "Provider";
                }else if (!consumer.isChecked() && !provider.isChecked()){
                    Toast.makeText(MainActivity.this , "Check box is empty" , Toast.LENGTH_LONG).show();
                    return;
                }else if  (consumer.isChecked() && provider.isChecked()){
                    Toast.makeText(MainActivity.this , "Cannot be both at a time" , Toast.LENGTH_LONG).show();
                    return;
                }

                if (type != null)
                    databaseReference = FirebaseDatabase.getInstance().getReference(type);


                dialog.setMessage("Logging user in");
                dialog.setCancelable(false);
                dialog.show();

                login(type ,txtName ,p);

            }
        });
    }

    public void getType(String s){

        if (s.equals("Consumer")){
            Intent intent = new Intent(this , ConsumerSignIn.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this , ProviderSignIn.class);
            startActivity(intent);
            finish();
        }

    }

    public boolean checkIfLoggedIn(){

        boolean check = sharedPreferences.getBoolean("LoggedIn" , false);

        if (check){
            return true;
        }else{
            return false;
        }
    }

    public void login(final String t ,final String a , String b){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (t.equals("Consumer")){
                    if (dataSnapshot.child(a).exists()){

                        Consumer c = dataSnapshot.child(a).getValue(Consumer.class);

                        if (c.getPwd().equals(p)){

                            Intent intent = new Intent(MainActivity.this , Consumer_frontpage.class);
                            intent.putExtra("Username" , a);
                            intent.putExtra("Type" , t);
                            dialog.dismiss();
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this , "Password is wrong" , Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }else{
                    if (dataSnapshot.child(a).exists()) {

                        Provider p = dataSnapshot.child(a).getValue(Provider.class);

                        if (p.getPwd().equals(p)) {

                            Intent intent = new Intent(MainActivity.this, Provider_front_page.class);
                            intent.putExtra("Username", a);
                            intent.putExtra("Type", t);
                            dialog.dismiss();
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Password is wrong", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
