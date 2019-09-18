package com.example.localite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class maps extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    private MapView mapView;
    MapboxMap mapboxMap;
    // LocationComponent -> Get the Location
    private LocationComponent locationComponent;
    // For Permissions
    private PermissionsManager permissionsManager;
    // We need to Specify the Current Route
    DirectionsRoute currentRoute;
    // For Navigation Route Map
    NavigationMapRoute navigationMapRoute;
    private Button button;

    double lon , lat;

    String type , no;

    DatabaseReference databaseReference;

    Provider p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Integrating the Mapbox in our app ....
        Mapbox.getInstance(this,"sk.eyJ1IjoiYXJ5YW4tMDA3NyIsImEiOiJjazBwY2lpbDUwaWNxM2NwcDhtbG9sYTIyIn0.sNTfkpX3F7Volqx7sPhWOQ");
        setContentView(R.layout.activity_maps);

        Intent i = getIntent();
        //type = i.getStringExtra("protype");
        //no = i.getStringExtra("prono");
        Bundle b = i.getExtras();

        lat = b.getDouble("lat");
        lon = b.getDouble("lon");

        Toast.makeText(this , lat + " " + lon , Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Provider");


        mapView = (MapView)findViewById(R.id.mapView);
        // Activities have the ability, under special circumstances, to restore themselves to a previous state
        mapView.onCreate(savedInstanceState);
        // getMapAsync call the onMapReadyMethod LINE 158
        mapView.getMapAsync(this);

        button = findViewById(R.id.button);
        button.setEnabled(false);

    }

    public void getLatLon(double lat , double lon){
        this.lat = lat;
        this.lon = lon;

        Toast.makeText(maps.this , lat + " " + lon , Toast.LENGTH_SHORT).show();
    }
    
    // OnExplanationNeeded & onPermissionResult & onMapClick & onMapReady
    // These are the implemented Methods ....
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    // PERMISSION WORK START FROM HERE >>>>
    @Override
    public void onPermissionResult(boolean granted) {
        if(granted) {
            enableLocationComponent(mapboxMap.getStyle());
        }else {
            Toast.makeText(getApplicationContext(),"Permission not Granted ",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        // Getting the Destination Point Here ....
        Point destinationPoint = Point.fromLngLat(p.getLon(),
                p.getLat());

        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");

        if(source != null){
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        // Created getRoute Below :-> LINE 124
        getRoute(originPoint, destinationPoint);

        button.setEnabled(true);
        button.setBackgroundResource(R.color.mapbox_blue);
        return true;
    }

    // For getting the Route Between Origin & Destination ...
    private void getRoute(Point originPoint, Point destinationPoint) {
        // Calling DirectionAPI for Navigation SDK
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(originPoint)
                .destination(destinationPoint)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if( response.body() != null && response.body().routes().size()>0){
                            currentRoute = response.body().routes().get(0);

                            if( navigationMapRoute != null){
                                navigationMapRoute.removeRoute();
                            }else{
                                navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                            }

                            navigationMapRoute.addRoute(currentRoute);
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });

    }


    // WHEN THE MAP IS READY >>>>>
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        // When the map is Ready we need to display user Current Location
        this.mapboxMap = mapboxMap;

        // For the Zoom Feature
        this.mapboxMap.setMinZoomPreference(15);
        // We need to set the Style of map Box Map ( Leave it for Later )
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Created enableLocationComponent Method .... // LINE 224
                enableLocationComponent(style);

                // Adding Destination Icon Layer ( Making Method )>> LINE 194
                addDestinationIconLayer(style);

                // onMapClick LINE 101
                //mapboxMap.addOnMapClickListener(maps.this);

                Point destinationPoint = Point.fromLngLat(lon, lat);

                Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                        locationComponent.getLastKnownLocation().getLatitude());

                GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");

                if(source != null){
                    source.setGeoJson(Feature.fromGeometry(destinationPoint));
                }

                // Created getRoute Below :-> LINE 124
                getRoute(originPoint, destinationPoint);

                button.setEnabled(true);
                button.setBackgroundResource(R.color.mapbox_blue);

                // ON CLICK OF BUTTON >>>

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean simulateRoute = true;
                        NavigationLauncherOptions options = NavigationLauncherOptions
                                .builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();

                        NavigationLauncher.startNavigation(maps.this, options);
                    }
                });
            }
        });
    }

    // This is for Adding Destination Icon Image ....
    private void addDestinationIconLayer(Style style) {
        style.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.mapbox_marker_icon_default));

        //GeoJson is Geo -> Earth , Js -> JavaScript ,  O -> Object , N -> Notation
        // Used for Representing Simple Geographical Features ...

        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        style.addSource(geoJsonSource);

        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id" ,
                "destination-source-id");

        destinationSymbolLayer.withProperties(iconImage("destination-icon-id"),
                iconAllowOverlap(true) , iconIgnorePlacement(true));

        style.addLayer(destinationSymbolLayer);
    }






    // PERMISSIONS CHECK HERE >>>> Before enablingLocationComponent
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check If the Permissions are Enabled , if Not then Request for it
        if(PermissionsManager.areLocationPermissionsGranted(this)) {
            // Then we can Activate MapBox Location Component
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this,loadedMapStyle);
            // We just Activated the Location Component now we just need to set it
            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);
        }else {
            // If the Permission not Enabled then ask it ...
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
            // After this onRequestPermissionResult  LINE 243
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Then go to OnPermissionsResult ..... LINE 102
    }


    // LETS HAVE SOME METHODS >>>>>>
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
