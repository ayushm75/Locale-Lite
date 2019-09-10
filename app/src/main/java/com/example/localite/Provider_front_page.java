package com.example.localite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Provider_front_page extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean isloggedin;

    private static final String TAG = "SelectImageActivity";

    String oname , ophone , otype;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference , ref2;

    TextView name , phone , addr , uv , dv;

    Provider p;

    static final int SELECT_IMAGE = 5000;

    private static final int PICK_IMAGE_REQUEST = 5;
    private Uri mImageUri;

    ProgressBar mprogress;

    ImageView iv;

    private StorageReference mStorageref;
    private DatabaseReference databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_front_page);

        Intent i = getIntent();
        oname = i.getStringExtra("Username");
        ophone = i.getStringExtra("phone number");
        otype = i.getStringExtra("type");

        uv = (TextView)findViewById(R.id.uno);
        dv = (TextView)findViewById(R.id.dno);

        iv = (ImageView)findViewById(R.id.imageView6);
        mprogress = new ProgressBar(this);

        mStorageref = FirebaseStorage.getInstance().getReference("AuthProvider");
        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        ref2 = FirebaseDatabase.getInstance().getReference("Admin").child("Work");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar2));
        getSupportActionBar().setTitle(oname);

        sharedPreferences = getSharedPreferences("Localite" , MODE_PRIVATE);
        isloggedin = sharedPreferences.getBoolean("LoggedIn" , false);

        if (!isloggedin)
            changeStatus(oname);
        else{
            oname = sharedPreferences.getString("NameP" , null);
            ophone = sharedPreferences.getString("phone number of current user" , null) ;
            otype = sharedPreferences.getString("type" , null);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Provider");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                p = dataSnapshot.child(otype).child(ophone).getValue(Provider.class);
                name = (TextView)findViewById(R.id.name);
                phone = (TextView)findViewById(R.id.phone);
                addr = (TextView)findViewById(R.id.addr);
                name.setText(oname);
                phone.setText("Phone No.:" + ophone);
                addr.setText( "Address" + p.getAddr());
                uv.setText(String.valueOf(p.getUpvotes()));
                dv.setText(String.valueOf(p.getDownvotes()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        handlePermission();
    }

    public void changeStatus(String a){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NameP" , a);
        editor.putBoolean("LoggedIn" , true);
        editor.putString("phone number of current user" , ophone);
        editor.putBoolean("Provider" , true);
        editor.putString("type" ,otype);
        editor.commit();

        if (editor.commit()){
            Toast.makeText(Provider_front_page.this , "Commit successful" , Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Provider_front_page.this , "Commit unsuccessful" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.over_flow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){


            case R.id.signout:
                signmeout();
                return true;
            case R.id.chat_history:
                bringupchat();
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    public void signmeout(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoggedIn" , false);
        editor.apply();

        Intent intent = new Intent(Provider_front_page.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void bringupchat(){

        Intent i = new Intent(Provider_front_page.this , ChatHistoryP.class);
        startActivity(i);
    }

    void handlePermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return ;
        }
        if (ContextCompat.checkSelfPermission(Provider_front_page.this , Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this ,
                     new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                     SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case SELECT_IMAGE:
                for (int i=0 ; i< permissions.length ; i++){
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){

                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(Provider_front_page.this , permission);
                    }else{
                        showSettingsAlert();

                    }
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAppSettings(Provider_front_page.this);
                    }
                });
        alertDialog.show();
    }

    public static void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public void pick(View view) {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
        startActivityForResult(i , PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_IMAGE_REQUEST && requestCode != RESULT_OK)){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(iv, new Callback() {
                @Override
                public void onSuccess() {

                    Toast.makeText(Provider_front_page.this , "Success" , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {

                    Toast.makeText(Provider_front_page.this , "Here  i am" , Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Toast.makeText(Provider_front_page.this , "Are we even reach here" , Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void upload(View view) {

        if (mImageUri != null){

           final StorageReference ref = mStorageref.child(oname +"."+ getFileExtension(mImageUri));

           ref.putFile(mImageUri)
                   .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                       @Override
                       public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                           if (!task.isSuccessful())
                               throw task.getException();

                           return ref.getDownloadUrl();
                       }
                   }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {

                   if (task.isSuccessful()){
                       Uri downUri = task.getResult();
                       p.setmImageUri(downUri.toString());
                       sendForAuthentication(p);
                       Toast.makeText(Provider_front_page.this , "Successfully uploaded" , Toast.LENGTH_SHORT).show();
                   }

               }
           });

        }else{
            Toast.makeText(Provider_front_page.this , "No file selected" , Toast.LENGTH_SHORT).show();
        }
    }

    private void sendForAuthentication(final Provider p) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                      AdminInfo ai = ds.getValue(AdminInfo.class);
                    if (distance(p.getLat() , p.getLon() , ai.getLat() , ai.getLon()) < 5){
                        ref2.child(ds.getKey()).setValue(p);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist;
    }
}
