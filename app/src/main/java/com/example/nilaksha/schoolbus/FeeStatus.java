package com.example.nilaksha.schoolbus;

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

public class FeeStatus extends AppCompatActivity {

    private Spinner childrenName;

    private ArrayList<String> arrayListChildren;

    private ArrayList<String> paymentStatus;

    private Button showData;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_status);

        childrenName = (Spinner) findViewById(R.id.cmbChildren);

        arrayListChildren = new ArrayList<String>();

        showData = (Button) findViewById(R.id.btnShow);

        listView = (ListView) findViewById(R.id.lstPayment);
        paymentStatus =  new ArrayList<>();


        webCallGetChildren();

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectName = childrenName.getSelectedItem().toString();

                webCallShowFee(selectName);

            }
        });




    }

    private void webCallShowFee(final String name){

        LoadFeeDetails feeDetails = new LoadFeeDetails() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


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

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FeeStatus.this, android.R.layout.simple_list_item_1, paymentStatus);
                        listView.setAdapter(arrayAdapter);



                    } else{
                        Toast.makeText(FeeStatus.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(FeeStatus.this, "cant load", Toast.LENGTH_SHORT).show();
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
                        childrenName.setAdapter(new ArrayAdapter<String>(FeeStatus.this,android.R.layout.simple_spinner_dropdown_item, arrayListChildren));


                    } else{
                        Toast.makeText(FeeStatus.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(FeeStatus.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

    }
}
