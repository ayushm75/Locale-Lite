package com.example.localite;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
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
import java.util.List;

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

    double lat;
    double lon;

    private GoogleMap mMap;
    static private final int REQUEST_LOCATION_PERMISSION = 500;
    LocationManager locationManager;
    Location location;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_sign_in);
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

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

                location = getLastKnownLocation();
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                dialog.setMessage("Registering...");
                dialog.show();

                savePreferences(txtName ,p);

                c = new Consumer(txtName , txtPhone , txtEmail , txtAddr , p , lat , lon);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(txtPhone).exists()){

                            Toast.makeText(getApplicationContext(),"User already registered!",Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }else{

                            databaseReference.child(txtPhone).setValue(c);
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

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void savePreferences(String a ,String b){

        SharedPreferences sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NameC" , a);
        editor.putString("PasswordC" , b);
        editor.putBoolean("LoggedIn" , false);
        editor.putString("phone number of current user" , null);
        editor.putBoolean("Consumer" , false);
        editor.putBoolean("Provider" , false);
        editor.commit();
    }
}
