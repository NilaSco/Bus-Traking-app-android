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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadFeeDetails;
import com.example.nilaksha.schoolbus.ClassFile.SendEmail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class IncomeReportDriver extends AppCompatActivity {

    private TextView totalAmount,totaldue,income;

    private EditText inputEmail;

    private Button email;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_report_driver);

        totalAmount = (TextView) findViewById(R.id.txtTotalAmount);
        totaldue = (TextView) findViewById(R.id.txtTotalDue);
        income = (TextView) findViewById(R.id.txtIncome);

        inputEmail = (EditText) findViewById(R.id.txtEmail);

        email = (Button) findViewById(R.id.btnEmail);

        progressDialog = new ProgressDialog(IncomeReportDriver.this);

        progressDialog.setTitle("Loading.");
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        webCallShowallFee();

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getAmount = totalAmount.getText().toString();
                String getDue = totaldue.getText().toString();
                String getIncome = income.getText().toString();
                String getEmail = inputEmail.getText().toString();

                if(getEmail.equals("")){

                    MessageBox("Error","Please Input Email..!!");

                }else{

                    progressDialog.setTitle("Loading.");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    getEmail(getAmount,getDue,getIncome,getEmail);

                }


            }
        });



    }



    private void getEmail(String amount,String due,String income,String email){

        SendEmail sendEmail = new SendEmail(amount,due,income,email) {
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


    private void webCallShowallFee(){

        LoadFeeDetails feeDetails = new LoadFeeDetails() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {
                        progressDialog.cancel();


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        int due = 0;
                        int amount = 0;
                        int incomedb = 0;


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

                                amount = amount + confee;
                                incomedb = incomedb + conpayment;



                                if(confee > conpayment){
                                    due +=  confee - conpayment;
                                }




                            }

                        }

                        totaldue.setText(due +"");
                        totalAmount.setText(amount+"");
                        income.setText(incomedb+"");



                    } else{
                        progressDialog.cancel();
                        Toast.makeText(IncomeReportDriver.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                progressDialog.cancel();
                Toast.makeText(IncomeReportDriver.this, "cant load", Toast.LENGTH_SHORT).show();
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
