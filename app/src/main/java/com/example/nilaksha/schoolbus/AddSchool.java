package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadSchool;
import com.example.nilaksha.schoolbus.ClassFile.SaveDriversChildren;
import com.example.nilaksha.schoolbus.ClassFile.SaveDriversSchool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddSchool extends AppCompatActivity {

    private ArrayList<String> arrayList;

    private Spinner SchoolName;

    private Button add;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school);

        SchoolName = (Spinner) findViewById(R.id.cmbSchooName);
        arrayList = new ArrayList<String>();

        add = (Button) findViewById(R.id.btnAdd);

        progressDialog = new ProgressDialog(AddSchool.this);

        //load school list from database
        webCallLoadSchool();



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectSchool = SchoolName.getSelectedItem().toString();
                String DriverNIC = Login.USERID;

                progressDialog.setTitle("Loading.");
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();

                webCallSave(selectSchool,DriverNIC);


            }
        });



    }

    private void webCallSave(String school,String drivernic){

        SaveDriversSchool saveDriversSchool = new SaveDriversSchool(school,drivernic) {
            @Override
            public void displayResult(JSONObject jsonObject) {
                try {
                    progressDialog.cancel();
                    if (jsonObject.getString("status").equals("success")) {


                        MessageBox("Success","School Added Success.....!!");


                    } else{

                        MessageBox("Error","Fail.....!!");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                MessageBox("Error","Fail.....!!");
                progressDialog.cancel();
            }
        };
        saveDriversSchool.execute();


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
                        SchoolName.setAdapter(new ArrayAdapter<String>(AddSchool.this,android.R.layout.simple_spinner_dropdown_item, arrayList));



                    } else{
                        Toast.makeText(AddSchool.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(AddSchool.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loginAccount.execute();
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

}
