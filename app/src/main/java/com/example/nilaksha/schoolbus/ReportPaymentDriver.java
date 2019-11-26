package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadDriversChildren;
import com.example.nilaksha.schoolbus.ClassFile.LoadFeeDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportPaymentDriver extends AppCompatActivity {

    private Spinner childrenName;

    private ArrayList<String> arrayListChildren;

    private ArrayList<String> paymentStatus;

    private ListView listView;

    private Button showData;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_payment_driver);

        childrenName = (Spinner) findViewById(R.id.cmbChildren);

        arrayListChildren = new ArrayList<String>();

        showData = (Button) findViewById(R.id.btnShow);

        listView = (ListView) findViewById(R.id.lstPayment);
        paymentStatus =  new ArrayList<>();

        progressDialog = new ProgressDialog(ReportPaymentDriver.this);

        progressDialog.setTitle("Loading.");
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        webCallGetChildren();

        webCallShowallFee();

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectName = childrenName.getSelectedItem().toString();

                webCallShowFee(selectName);




            }
        });



    }
    private void webCallShowallFee(){

        LoadFeeDetails feeDetails = new LoadFeeDetails() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {
                        progressDialog.cancel();


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        paymentStatus.clear();


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String cName = contactObject.getString("driverID");



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

                                paymentStatus.add("Children Name:- "+children +"\nPayment Date:- "+paymentDate +"\nVehicle fee-: "+fee+ "\nPayment:- " +payment +"\nDue:- "+total);


                            }

                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReportPaymentDriver.this, android.R.layout.simple_list_item_1, paymentStatus);
                        listView.setAdapter(arrayAdapter);



                    } else{
                        progressDialog.cancel();
                        Toast.makeText(ReportPaymentDriver.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                progressDialog.cancel();
                Toast.makeText(ReportPaymentDriver.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        feeDetails.execute();




    }


    private void webCallShowFee(final String name){

        LoadFeeDetails feeDetails = new LoadFeeDetails() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {
                        progressDialog.cancel();


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        paymentStatus.clear();


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String cName = contactObject.getString("childrenName");



                            if(cName.equals(name)){

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

                                paymentStatus.add("Payment Date:- "+paymentDate +"\nVehicle fee-: "+fee+ "\nPayment:- " +payment +"\nDue:- "+total);


                            }

                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReportPaymentDriver.this, android.R.layout.simple_list_item_1, paymentStatus);
                        listView.setAdapter(arrayAdapter);



                    } else{
                        progressDialog.cancel();
                        Toast.makeText(ReportPaymentDriver.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                progressDialog.cancel();
                Toast.makeText(ReportPaymentDriver.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        feeDetails.execute();

    }



    private void webCallGetChildren(){
        LoadDriversChildren loadChildren = new LoadDriversChildren() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {
                        progressDialog.cancel();


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
                        childrenName.setAdapter(new ArrayAdapter<String>(ReportPaymentDriver.this,android.R.layout.simple_spinner_dropdown_item, arrayListChildren));


                    } else{
                        progressDialog.cancel();
                        Toast.makeText(ReportPaymentDriver.this,"No record found..!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                progressDialog.cancel();
                Toast.makeText(ReportPaymentDriver.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

    }


}
