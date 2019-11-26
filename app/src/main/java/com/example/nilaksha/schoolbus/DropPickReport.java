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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.DropPickLocation;
import com.example.nilaksha.schoolbus.ClassFile.LoadDriversChildren;
import com.example.nilaksha.schoolbus.ClassFile.LoadFeeDetails;
import com.example.nilaksha.schoolbus.ClassFile.SendEmailDropPick;
import com.example.nilaksha.schoolbus.ClassFile.SendEmailFee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DropPickReport extends AppCompatActivity {

    private Spinner childrenName;

    private ArrayList<String> arrayListChildren;

    private ArrayList<String> paymentStatus;

    private Button showData,email;

    private ListView listView;

    private EditText inputEmail;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_pick_report);

        childrenName = (Spinner) findViewById(R.id.cmbChildren);

        arrayListChildren = new ArrayList<String>();

        showData = (Button) findViewById(R.id.btnShow);

        listView = (ListView) findViewById(R.id.lstStatus);
        paymentStatus =  new ArrayList<>();

        progressDialog = new ProgressDialog(DropPickReport.this);

        inputEmail = (EditText) findViewById(R.id.txtEmail);

        email = (Button) findViewById(R.id.btnSendEmail);


        webCallGetChildren();

        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectName = childrenName.getSelectedItem().toString();

                progressDialog.setTitle("Loading.");
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();

                webCallShowFee(selectName);

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getEmail = inputEmail.getText().toString();
                String selectName = childrenName.getSelectedItem().toString();

                if(getEmail.equals("")){

                    MessageBox("Error","Please Input Email..!!");

                }else{

                    progressDialog.setTitle("Loading.");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    getEmail(getEmail,selectName);

                }


            }
        });
    }

    private void getEmail(String email,String name){

        SendEmailDropPick sendEmail = new SendEmailDropPick(email,name) {
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

    private void webCallShowFee(final String name){

        DropPickLocation feeDetails = new DropPickLocation(name) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    progressDialog.cancel();
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("frommsg");

                        paymentStatus.clear();


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String cName = contactObject.getString("parentnic");



                            if(Login.USERID.equals(cName)){

                                String dbmsg = contactObject.getString("msg");
                                String time = contactObject.getString("msgtime");
                                String date = contactObject.getString("msgDate");


                                paymentStatus.add("Status:- "+ dbmsg +"\nTime:- "+time+ "\nDate:- " +date );

                            }

                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DropPickReport.this, android.R.layout.simple_list_item_1, paymentStatus);
                        listView.setAdapter(arrayAdapter);



                    } else{
                        Toast.makeText(DropPickReport.this,"No Record Found...!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(DropPickReport.this, "cant load", Toast.LENGTH_SHORT).show();
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
                        childrenName.setAdapter(new ArrayAdapter<String>(DropPickReport.this,android.R.layout.simple_spinner_dropdown_item, arrayListChildren));


                    } else{
                        Toast.makeText(DropPickReport.this,"No Record Found..!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(DropPickReport.this, "cant load", Toast.LENGTH_SHORT).show();
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
