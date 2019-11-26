package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.Constant;
import com.example.nilaksha.schoolbus.ClassFile.LoadSchool;
import com.example.nilaksha.schoolbus.ClassFile.OkHTTPClient;
import com.example.nilaksha.schoolbus.ClassFile.SaveChildren;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentChildrenRegister extends AppCompatActivity implements LocationListener {

    private ArrayList<String> arrayList;

    private Spinner SchoolName;

    Button GetLocation,Save;

    String lat,lng;

    EditText name;

    LocationManager locationManager;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_children_register);



        CheckPermission();

        lat = "";
        lng = "";

        name = (EditText)findViewById(R.id.txtChildrenName);

        GetLocation = (Button)findViewById(R.id.btnLocation);
        Save = (Button)findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(ParentChildrenRegister.this);



        SchoolName = (Spinner) findViewById(R.id.cmbSchooName);
        arrayList = new ArrayList<String>();

        //load school name
        webCallLoadSchool();



        GetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set location as current location
                getLocation();

                //change button text
                GetLocation.setText("Set As Current Location (SET)");

                //change button color
                GetLocation.setBackgroundColor(Color.BLUE);
                GetLocation.setTextColor(Color.WHITE);

            }
        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            String childrenName = name.getText().toString();
            String schoolName = SchoolName.getSelectedItem().toString();

            if(validate()){

                progressDialog.setTitle("Loading.");
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();

                saveChildren(Login.USERID,childrenName,schoolName,lat,lng);

            }


            }
        });




    }

    private boolean validate(){

        String childrenName = name.getText().toString();
        String schoolName = SchoolName.getSelectedItem().toString();

        if(childrenName.isEmpty()){

            MessageBox("Error","Please Type Children Name..");
            return false;

        }else{

            if(schoolName.equals("Select School")){
                MessageBox("Error","Please Select School..");
                return false;
            }else{

                if(lat.isEmpty() && lng.isEmpty()){
                    MessageBox("Error","Please Set Location..");
                    return false;
                }else{
                    return true;
                }
            }

        }



    }

    public void MessageBox(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        // Alert dialog button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Alert dialog action goes here
                        // onClick button code here
                        dialog.dismiss();// use dismiss to cancel alert dialog
                    }
                });
        alertDialog.show();
    }



    private void webCallLoadSchool(){
        LoadSchool loginAccount = new LoadSchool() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {

                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());


                            String schoolName = contactObject.getString("name");
                            String schoolID = contactObject.getString("id");

                            System.out.println("get from database + " + schoolName +"\n" + schoolID);

                            arrayList.add(schoolName);

                        }

                        arrayList.add(0,"Select School");
                        SchoolName.setAdapter(new ArrayAdapter<String>(ParentChildrenRegister.this,android.R.layout.simple_spinner_dropdown_item, arrayList));



                    } else{
                        Toast.makeText(ParentChildrenRegister.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(ParentChildrenRegister.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loginAccount.execute();
    }


    private void   saveChildren(String pid,String name,String schoolName,String lat,String lng){

        SaveChildren saveChildren = new SaveChildren(pid,name,schoolName,lat,lng) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    progressDialog.cancel();
                    if (jsonObject.getString("status").equals("success")) {


                        MessageBox("Success","Register Success.....!!");
                        ClearField();


                    } else{

                        MessageBox("Error","Register Fail.....!!");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void displayError() {



            }
        };
        saveChildren.execute();

    }

    private void ClearField(){
        name.setText("");
    }


    @Override
    public void onLocationChanged(Location location) {

        lat = String.valueOf(location.getLongitude());
        lng = String.valueOf(location.getLatitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(ParentChildrenRegister.this, "Enabled new provider!" + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(ParentChildrenRegister.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
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
