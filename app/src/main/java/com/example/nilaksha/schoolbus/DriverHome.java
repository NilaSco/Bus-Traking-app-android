package com.example.nilaksha.schoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.SaveLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DriverHome extends AppCompatActivity implements LocationListener {

    GridLayout mainGrid;

    private String lat,lng;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        setSingleEvent(mainGrid);

        CheckPermission();

        getLocation();

        if(Login.USETYPE.equals("Driver")){
            updateDisplay();
        }




    }

    private void updateDisplay() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {


                getLocation();

                //validate null value
                if(lat != null){
                    saveLocation();
                }




            }

        },0,50000);//Update text every second
    }


    private void saveLocation(){

        SaveLocation saveLocation = new SaveLocation(lat,lng) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                       Toast.makeText(DriverHome.this,"run",Toast.LENGTH_LONG).show();


                    } else{

                        Toast.makeText(DriverHome.this,"error",Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void displayError() {



            }
        };
        saveLocation.execute();


    }


    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(finalI == 0){

                        Intent intent = new Intent(DriverHome.this,AddChildren.class);
                        startActivity(intent);
                    }

                    if(finalI == 1){

                        Intent intent = new Intent(DriverHome.this,AddSchool.class);
                        startActivity(intent);


                    }

                    if(finalI == 2){
                        Intent intent = new Intent(DriverHome.this,StatusHome.class);
                        startActivity(intent);

                    }

                    if(finalI == 3){

                        Intent intent = new Intent(DriverHome.this,VehicleFeeDriver.class);
                        startActivity(intent);

                    }

                    if(finalI == 4){

                        Intent intent = new Intent(DriverHome.this,ReportHomeDriver.class);
                        startActivity(intent);

                    }

                    if(finalI == 5){

                        finish();
                        System.exit(0);

                    }

                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLongitude());
        lng = String.valueOf(location.getLatitude());

        System.out.println("System lat:- "+lat + " System lng:- "+ lng);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(DriverHome.this, "Enabled new provider!" + provider, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(DriverHome.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }

    public void getLocation() {

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }
}
