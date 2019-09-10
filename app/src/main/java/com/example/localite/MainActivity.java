package com.example.localite;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
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

    EditText phone , password;
    Button login;
    TextView newUser;
    SharedPreferences sharedPreferences;

    String txtphone;
    String txtpwd;
    String p;

    Consumer cc;

    ProgressDialog dialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = (EditText)findViewById(R.id.phone);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        newUser  = (TextView)findViewById(R.id.newuser);

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar));

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);

        dialog = new ProgressDialog(this);

        if (sharedPreferences != null){
            if (checkIfLoggedIn()){

                if (sharedPreferences.getBoolean("Consumer" , false)){
                    String name = sharedPreferences.getString("NameC" , null);
                    Intent intent = new Intent(MainActivity.this , Consumer_frontpage.class);
                    intent.putExtra("Username" , name);
                    startActivity(intent);
                    finish();
                }else if(sharedPreferences.getBoolean("Provider" , false)){
                    String name = sharedPreferences.getString("NameP" , null);
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
                ConsumerOrProvider c = new ConsumerOrProvider();
                c.show(getSupportFragmentManager() , "123");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                txtphone = phone.getText().toString().trim();
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

                if (txtphone.isEmpty()){
                    phone.setError("Name is empty!");
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

                if (txtphone.charAt(0) == '#'){
                    databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child("info");
                    dialog.setMessage("Logging user in");
                    dialog.setCancelable(false);
                    dialog.show();

                    adminlogin(txtphone ,txtpwd);

                }else{
                    databaseReference = FirebaseDatabase.getInstance().getReference("Consumer");
                    dialog.setMessage("Logging user in");
                    dialog.setCancelable(false);
                    dialog.show();

                    login(txtphone ,p);
                }
            }
        });
    }

    private void adminlogin(String txtphone, final String pwd) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                AdminInfo ai = dataSnapshot.getValue(AdminInfo.class);
                if (pwd.equals(ai.getuPwd())){

                    Intent i = new Intent(MainActivity.this , Admin_front_page.class);
                    dialog.dismiss();
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void savePreferences(String a ,String b){

        SharedPreferences sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NameC" , a);
        editor.putString("PasswordC" , b);
        editor.commit();
    }

    public void getType(String a){

        if (a.equals("Consumer")){
            Intent intent = new Intent(MainActivity.this , ConsumerSignIn.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MainActivity.this , ProviderSignIn.class);
            startActivity(intent);
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

    public void login(final String a , final String b){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(a).exists()){

                        Consumer c = dataSnapshot.child(a).getValue(Consumer.class);

                        if (c.getPwd().equals(b)){

                            Intent intent = new Intent(MainActivity.this , Consumer_frontpage.class);
                            intent.putExtra("Username" , c.getName());
                            intent.putExtra("phone number" , txtphone);
                            dialog.dismiss();
                            startActivity(intent);
                            finish();
                        }else{
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this , "Password is wrong" , Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this , "Something went wrong" , Toast.LENGTH_LONG).show();
                return;

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.provider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(MainActivity.this , Provider_log_in.class);
        startActivity(intent);
        finish();
        return true;

    }
}
