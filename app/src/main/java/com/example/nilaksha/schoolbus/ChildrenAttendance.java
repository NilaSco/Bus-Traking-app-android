package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadDriversChildren;
import com.example.nilaksha.schoolbus.ClassFile.SaveAttendance;
import com.example.nilaksha.schoolbus.ClassFile.feePayment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChildrenAttendance extends AppCompatActivity {

    private ArrayList<String> arrayListChildren;

    private Button attend;

    private Spinner childrenName;

    private ProgressDialog progressDialog;

    private String dbDriverNIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_attendance);

        dbDriverNIC = "";

        childrenName = (Spinner) findViewById(R.id.cmbChildren);


        attend = (Button) findViewById(R.id.btnAttend);

        arrayListChildren = new ArrayList<String>();

        progressDialog = new ProgressDialog(ChildrenAttendance.this);

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

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){

                    String children = childrenName.getSelectedItem().toString();
                    String driver = dbDriverNIC;
                    String parent = Login.USERID;


                    progressDialog.setTitle("Loading.");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    webCallSavePayment(parent,driver,children);




                }

            }
        });




    }

    private void webCallSavePayment(String name, String payment, String getFee){

        SaveAttendance saveAttendance = new SaveAttendance(name,getFee,payment) {
            @Override
            public void displayResult(JSONObject jsonObject) {
                try {
                    progressDialog.cancel();
                    if (jsonObject.getString("status").equals("success")) {

                        MessageBox("Success","Attendance Updated..");


                    } else{

                        MessageBox("Error","Fail.....!!");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void displayError() {

            }
        } ;
        saveAttendance.execute();




    }


    private boolean validate(){

        String children = childrenName.getSelectedItem().toString();


        if(children.matches("Select children")){
            MessageBox("Error","Empty Field found..!!");
            return false;

        }else{
            return true;
        }

    }


    private void webCallGetChildren(){
        LoadDriversChildren loadChildren = new LoadDriversChildren() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String parent = contactObject.getString("ParentNIC");



                            if(parent.equals(Login.USERID)){

                                String children = contactObject.getString("childrenName");

                                arrayListChildren.add(children);
                            }

                        }
                        arrayListChildren.add(0,"Select children");
                        childrenName.setAdapter(new ArrayAdapter<String>(ChildrenAttendance.this,android.R.layout.simple_spinner_dropdown_item, arrayListChildren));


                    } else{
                        Toast.makeText(ChildrenAttendance.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(ChildrenAttendance.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

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
                        Toast.makeText(ChildrenAttendance.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(ChildrenAttendance.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

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
