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

import com.example.nilaksha.schoolbus.ClassFile.LoadChildren;
import com.example.nilaksha.schoolbus.ClassFile.LoadParent;
import com.example.nilaksha.schoolbus.ClassFile.LoadSchool;
import com.example.nilaksha.schoolbus.ClassFile.Register;
import com.example.nilaksha.schoolbus.ClassFile.SaveDriversChildren;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddChildren extends AppCompatActivity {

    private ArrayList<String> arrayListParent;

    private ArrayList<String> arrayListChildren;

    private Spinner parentName, childrenName;
    private Button add;
    private EditText schoolName,vehicleFee;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_children);

        parentName = (Spinner) findViewById(R.id.cmbParentName);
        childrenName = (Spinner) findViewById(R.id.cmbChildren);

        add = (Button) findViewById(R.id.btnAdd);

        schoolName = (EditText)findViewById(R.id.txtSchoolName);
        vehicleFee = (EditText)findViewById(R.id.txtVehicleFee);

        arrayListParent = new ArrayList<String>();
        arrayListChildren = new ArrayList<String>();

        progressDialog = new ProgressDialog(AddChildren.this);

        webCallLoadParent();


       parentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Object item = parent.getItemAtPosition(position);
               System.out.print(item);

               webCallGetSelectChildren(item +"");

           }
           public void onNothingSelected(AdapterView<?> parent) {
           }
       });

        childrenName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                System.out.print(item);

                webCallGetChildrenSchool(item +"");

               // Toast.makeText(getApplicationContext(), "The option is:" + item , Toast.LENGTH_SHORT).show();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




       add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(validate()){
                   String driverNIC = Login.USERID;
                   String parentNIC = parentName.getSelectedItem().toString();
                   String children = childrenName.getSelectedItem().toString();
                   String school = schoolName.getText().toString();
                   String fee = vehicleFee.getText().toString();

                   progressDialog.setTitle("Loading.");
                   progressDialog.setMessage("Please Wait....");
                   progressDialog.show();
                   webCallSave(driverNIC,parentNIC,children,school,fee);

               }





           }
       });

    }

    private void webCallSave(String driverNIC,String parentNIC,String children, String school,String fee){

        SaveDriversChildren register = new SaveDriversChildren(driverNIC,parentNIC,children,school,fee) {
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
                MessageBox("Error","Register Fail.....!!");
                progressDialog.cancel();
            }
        };
        register.execute();


    }

    private void ClearField(){
        schoolName.setText("");
        vehicleFee.setText("");
    }

    private boolean validate(){

        String school = schoolName.getText().toString();
        String fee = vehicleFee.getText().toString();

        if(school.isEmpty() && fee.isEmpty()){
            MessageBox("Error","Empty Field found..!!");
            return false;

        }else{
            return true;
        }



    }


    private void webCallGetChildrenSchool(final String name){
        LoadChildren loadChildren = new LoadChildren() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());


                            String parentNIC = contactObject.getString("name");

                            if(name.equals(parentNIC)){

                                String childrenSchool = contactObject.getString("school");

                                schoolName.setText(childrenSchool);

                            }
                        }



                    } else{
                        Toast.makeText(AddChildren.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(AddChildren.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

    }


    private void webCallGetSelectChildren(final String nic){
        LoadChildren loadChildren = new LoadChildren() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {

                        arrayListChildren.clear();

                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());


                            String parentNIC = contactObject.getString("parentid");

                            if(nic.equals(parentNIC)){

                                String childrenName = contactObject.getString("name");

                                arrayListChildren.add(childrenName);

                            }
                        }
                        arrayListChildren.add(0,"Select children");
                        childrenName.setAdapter(new ArrayAdapter<String>(AddChildren.this,android.R.layout.simple_spinner_dropdown_item, arrayListChildren));

                    } else{
                        Toast.makeText(AddChildren.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(AddChildren.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

    }


    private void webCallLoadParent(){
        LoadParent loadParent = new LoadParent() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {

                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());


                            String parentNIC = contactObject.getString("nic");

                            arrayListParent.add(parentNIC);

                        }

                        arrayListParent.add(0,"Select Parent");
                        parentName.setAdapter(new ArrayAdapter<String>(AddChildren.this,android.R.layout.simple_spinner_dropdown_item, arrayListParent));



                    } else{
                        Toast.makeText(AddChildren.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(AddChildren.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadParent.execute();
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
