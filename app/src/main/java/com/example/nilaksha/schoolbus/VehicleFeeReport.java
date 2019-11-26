package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadFeeDetails;
import com.example.nilaksha.schoolbus.ClassFile.SendEmail;
import com.example.nilaksha.schoolbus.ClassFile.SendEmailFee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VehicleFeeReport extends AppCompatActivity {

    private ArrayList<String> paymentStatus;

    private ListView listView;

    private EditText inputEmail;

    private Button email;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_fee_report);

        listView = (ListView) findViewById(R.id.lstPayment);
        paymentStatus =  new ArrayList<>();

        inputEmail = (EditText) findViewById(R.id.txtEmail);

        email = (Button) findViewById(R.id.btnSendEmail);

        progressDialog = new ProgressDialog(VehicleFeeReport.this);

        progressDialog.setTitle("Loading.");
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        webCallShowFee();


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getEmail = inputEmail.getText().toString();

                if(getEmail.equals("")){

                    MessageBox("Error","Please Input Email..!!");

                }else{

                    progressDialog.setTitle("Loading.");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    getEmail(getEmail);

                }


            }
        });




    }


    private void getEmail(String email){

        SendEmailFee sendEmail = new SendEmailFee(email) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {
                        progressDialog.cancel();



                        MessageBox("Success","Email Send Success....!!");
                        //clear textbox
                        inputEmail.setText("");





                    } else{
                        progressDialog.cancel();
                        MessageBox("Error","Email Send Fail, try again....!!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void displayError() {

            }
        };
        sendEmail.execute();

    }




    private void webCallShowFee(){

        LoadFeeDetails feeDetails = new LoadFeeDetails() {
            @Override
            public void displayResult(JSONObject jsonObject) {
                progressDialog.cancel();

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        paymentStatus.clear();


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String cName = contactObject.getString("parentid");



                            if(cName.equals(Login.USERID)){

                                String children = contactObject.getString("childrenName");
                                String paymentDate = contactObject.getString("paymentDate");
                                String payment = contactObject.getString("paymentAmount");
                                String fee = contactObject.getString("fee");

                                int confee = Integer.parseInt(fee);
                                int conpayment = Integer.parseInt(payment);

                                int total = 0;

                                if(confee > conpayment){
                                    total =  confee - conpayment;
                                }

                                paymentStatus.add("Children Name-: "+ children+"\nPayment Date:- "+paymentDate +"\nVehicle fee-: "+fee+ "\nPayment:- " +payment +"\nDue:- "+total);

                            }

                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VehicleFeeReport.this, android.R.layout.simple_list_item_1, paymentStatus);
                        listView.setAdapter(arrayAdapter);



                    } else{
                        Toast.makeText(VehicleFeeReport.this,"No Record found..!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                progressDialog.cancel();
                Toast.makeText(VehicleFeeReport.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        feeDetails.execute();




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
