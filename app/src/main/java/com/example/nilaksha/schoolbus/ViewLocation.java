package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadDriverLocation;
import com.example.nilaksha.schoolbus.ClassFile.LoadDriversChildren;
import com.example.nilaksha.schoolbus.ClassFile.LoadTodayAttendance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewLocation extends AppCompatActivity {

    private ArrayList<String> arrayListChildren;

    private Button showLoation;

    private Spinner childrenName;

    private ProgressDialog progressDialog;

    private String dbDriverNIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);


        dbDriverNIC = "";

        childrenName = (Spinner) findViewById(R.id.cmbChildren);


        showLoation = (Button) findViewById(R.id.btnViewLocation);

        arrayListChildren = new ArrayList<String>();

        progressDialog = new ProgressDialog(ViewLocation.this);

        webCallGetChildren();

        childrenName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                System.out.print(item);

                webCallGetDriverNIC(item +"");


            }
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });


        showLoation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dbDriverNIC == null){

                }else{
                    getLocation(dbDriverNIC);
                }



            }
        });


    }

    private void getLocation(String id){

        LoadDriverLocation loadDriverLocation = new LoadDriverLocation(id) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String dbdriver = contactObject.getString("driverID");
                            String dblat = contactObject.getString("lat");
                            String dblng = contactObject.getString("lng");

                            //load google map
                            Uri gmmIntentUri = Uri.parse("geo:0,0?z=15&q="+dblng+","+dblat+"");

                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);


                        }



                    } else{
                        Toast.makeText(ViewLocation.this,"No Children", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void displayError() {

            }
        };
        loadDriverLocation.execute();




    }


    private void webCallGetChildren(){
        LoadTodayAttendance todayAttendance = new LoadTodayAttendance() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String parent = contactObject.getString("parentid");



                            if(parent.equals(Login.USERID)){

                                String children = contactObject.getString("childrenName");

                                arrayListChildren.add(children);
                            }

                        }
                        arrayListChildren.add(0,"Select children");
                        childrenName.setAdapter(new ArrayAdapter<String>(ViewLocation.this,android.R.layout.simple_spinner_dropdown_item, arrayListChildren));


                    } else{
                        Toast.makeText(ViewLocation.this,"No Record Found..!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(ViewLocation.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        todayAttendance.execute();

    }

    private void webCallGetDriverNIC(final String name){
        LoadDriversChildren loadChildren = new LoadDriversChildren() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String children = contactObject.getString("childrenName");

                            if(children.equals(name)){

                                dbDriverNIC =  contactObject.getString("driverNIC");

                            }

                        }



                    } else{
                        Toast.makeText(ViewLocation.this,"No Record Found..!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(ViewLocation.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

    }

}
