package com.ahmadrosid.drawroutemaps;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int n = 1;
    private LatLng SlatLng;
    private String collegeMessage;
    private TextView collegeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle collegedata = getIntent().getExtras();
        if(collegedata == null){
            return;
        }
        collegeMessage =collegedata.getString("src");
        collegeDetails = (TextView) findViewById(R.id.textView);
        collegeDetails.setText(collegeMessage);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dit = new LatLng(18.623194, 73.816104);
        mMap.addMarker(new MarkerOptions().position(dit).title("DIT,Pimpri"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dit));



    }


    public void onMapSearch(View view) {


      //  EditText SourceSearch = (EditText) findViewById(R.id.editText);
        String location = collegeDetails.getText().toString();
        List<Address> addressList = null;
        EditText DestSearch = (EditText) findViewById(R.id.editText2);
        String Dlocation = DestSearch.getText().toString();
        List<Address> addressList2 = null;

        if ((location != null || !location.equals("")) || (Dlocation != null || !Dlocation.equals(""))) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                addressList2 = geocoder.getFromLocationName(Dlocation, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(collegeDetails.getText().toString() == "MyLocation"){
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else{
                    if(!mMap.isMyLocationEnabled())
                        mMap.setMyLocationEnabled(true);

                    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (myLocation == null) {
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                        String provider = lm.getBestProvider(criteria, true);
                        myLocation = lm.getLastKnownLocation(provider);
                    }

                    if(myLocation!=null){
                        SlatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                        //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
                  //      mMap.addMarker(new MarkerOptions().position(SlatLng).title("My Location"));
                    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(SlatLng));
                    }
                }
        }

        else {
            Address address1 = addressList.get(0);
             SlatLng = new LatLng(address1.getLatitude(), address1.getLongitude());

        }


            Address address2 = addressList2.get(0);
            LatLng DlatLng = new LatLng(address2.getLatitude(), address2.getLongitude());
            DrawRouteMaps.getInstance(this)
                    .draw(SlatLng, DlatLng, mMap);

            mMap.addMarker(new MarkerOptions().position(SlatLng).title("Source"));

            mMap.addMarker(new MarkerOptions().position(DlatLng).title("Destination"));

            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(SlatLng)
                    .include(DlatLng).build();
            Point displaySize = new Point();
            getWindowManager().getDefaultDisplay().getSize(displaySize);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));

        }


    }

    public void satelliteClick(View v) {
        switch (n) {

            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                n = 2;
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                n = 3;
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                n = 1;
                break;

        }
    }

    public void goToSearch(View view){
        Intent m = new Intent(this,MainActivity.class);
        startActivity(m);
    }
    public void myLoc(View v) {


        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            if(!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if(myLocation!=null){
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
              //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
                mMap.addMarker(new MarkerOptions().position(userLocation).title("My Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            }
        }
    }
}
