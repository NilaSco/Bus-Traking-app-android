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
import com.example.nilaksha.schoolbus.ClassFile.LoadDriversChildren;
import com.example.nilaksha.schoolbus.ClassFile.feePayment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VehicleFeeDriver extends AppCompatActivity {

    private ArrayList<String> arrayListChildren;

    private EditText fee,payment;

    private Button pay;

    private Spinner childrenName;

    private ProgressDialog progressDialog;

    private String parentNIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_fee_driver);

        childrenName = (Spinner) findViewById(R.id.cmbChildren);

        fee = (EditText) findViewById(R.id.txtVehicleFee);
        payment = (EditText) findViewById(R.id.txtPayment);

        pay = (Button) findViewById(R.id.btnPay);

        arrayListChildren = new ArrayList<String>();

        progressDialog = new ProgressDialog(VehicleFeeDriver.this);

        parentNIC = "";



        //load driver's children
        webCallGetChildren();


        childrenName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                System.out.print(item);

                webCallGetVehicleFee(item +"");

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){


                    String children = childrenName.getSelectedItem().toString();
                    String getPayment = payment.getText().toString();
                    String getFee = fee.getText().toString();


                    progressDialog.setTitle("Loading.");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    webCallSavePayment(children,getPayment,getFee,parentNIC);

                    //clear field..
                    clearField();
                    
                }



            }
        });


    }

    private void clearField(){
        childrenName.setSelection(0);
        payment.setText("");
        fee.setText("");
    }

    private void webCallSavePayment(String name, String payment, String getFee,String parentid){

        feePayment feepayment = new feePayment(name,getFee,payment,parentid) {
            @Override
            public void displayResult(JSONObject jsonObject) {
                try {
                    progressDialog.cancel();
                    if (jsonObject.getString("status").equals("success")) {

                        MessageBox("Success","Payment Updated..");


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
        feepayment.execute();




    }

    private boolean validate(){

        String getPayment = payment.getText().toString();
        String getFee = fee.getText().toString();

        if(getPayment.isEmpty() && getFee.isEmpty()){
            MessageBox("Error","Empty Field found..!!");
            return false;

        }else{
            return true;
        }



    }

    private void webCallGetVehicleFee(final String name){
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
                                fee.setText(contactObject.getString("fee"));
                                parentNIC = contactObject.getString("ParentNIC");

                            }

                        }



                    } else{
                        Toast.makeText(VehicleFeeDriver.this,"No Children", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(VehicleFeeDriver.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

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

                            String driver = contactObject.getString("driverNIC");



                            if(driver.equals(Login.USERID)){

                                String children = contactObject.getString("childrenName");

                                arrayListChildren.add(children);
                            }

                        }
                        arrayListChildren.add(0,"Select children");
                        childrenName.setAdapter(new ArrayAdapter<String>(VehicleFeeDriver.this,android.R.layout.simple_spinner_dropdown_item, arrayListChildren));


                    } else{
                        Toast.makeText(VehicleFeeDriver.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(VehicleFeeDriver.this, "cant load", Toast.LENGTH_SHORT).show();
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
