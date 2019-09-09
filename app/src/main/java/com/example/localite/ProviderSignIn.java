package com.example.localite;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

    TextInputLayout b ;

    private GoogleMap mMap;
    static private final int REQUEST_LOCATION_PERMISSION = 500;
    LocationManager locationManager;
    Location location;

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
        b = (TextInputLayout) findViewById(R.id.addr);

        firebaseDatabase = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);

        b.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = getLastKnownLocation();
                lat = location.getLatitude();
                lon = location.getLongitude();
                txtAddr = getaddr();
                addr.setText(txtAddr);
            }
        });

        psignin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                txtName = oName.getText().toString().trim();
                txtType = type.getText().toString().trim();
                txtbName = bName.getText().toString().trim();
                if (txtAddr == null) {
                    txtAddr = addr.getText().toString().trim();
                }
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

                getLatLon();

                pr = new Provider(txtName , txtType , txtbName , txtAddr , txtPhone , txtEmail , p , lat , lon , 0 , 0 , false);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                          if (dataSnapshot.child(txtType).exists()){
                              if (dataSnapshot.child(txtType).child(txtPhone).exists()){

                                  Toast.makeText(getApplicationContext(),"Provider already registered!",Toast.LENGTH_LONG).show();
                                  dialog.cancel();
                              }else{
                                  databaseReference.child(txtType).child(txtPhone).setValue(pr);
                                  Toast.makeText(getApplicationContext(),"Registration Successful!",Toast.LENGTH_LONG).show();
                                  dialog.cancel();
                                  finish();
                              }
                          }else{

                              databaseReference.child(txtType).child(txtPhone).setValue(pr);
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

    public String getaddr(){

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();// Only if available else return NULL

        String a = address+city+state+country+postalCode+knownName;

        return a;
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

    public void getLatLon(){

        Geocoder geocoder = new Geocoder(getApplicationContext());

        try {
            List<Address> addresses = geocoder.getFromLocationName(txtAddr, 1);
            if (addresses != null && addresses.size() > 0){
                lat = addresses.get(0).getLatitude();
                lon = addresses.get(0).getLongitude();
            }else{
                lat = 0;
                lon = 0;
                Toast.makeText(getApplicationContext() , "Can't get location" , Toast.LENGTH_LONG).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
