package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.nilaksha.schoolbus.ClassFile.Register;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterParent extends AppCompatActivity {

    EditText name,nic,address,tel,email,password;
    Button register;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);

        name = (EditText)findViewById(R.id.txtName);
        nic = (EditText)findViewById(R.id.txtNic);
        address = (EditText)findViewById(R.id.txtAddress);
        tel = (EditText)findViewById(R.id.txtMobileNumber);
        email = (EditText)findViewById(R.id.txtEmail);
        password = (EditText)findViewById(R.id.txtPassword);

        register = (Button)findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(RegisterParent.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){

                    String getname = name.getText().toString().trim();
                    String getnic = nic.getText().toString().trim();
                    String getaddress = address.getText().toString().trim();
                    String gettel = tel.getText().toString().trim();
                    String getemail = email.getText().toString().trim();
                    String getpassword = password.getText().toString().trim();

                    progressDialog.setTitle("Loading.");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    webCall(getname,getnic,getaddress,gettel,getemail,getpassword);

                }

            }
        });

    }

    private void webCall(String name, String nic , String address, String tel, String email, String password){
        Register register = new Register(name,nic,address,tel,email,password) {
            @Override
            public void displayResult(JSONObject jsonObject) {
                try {
                    progressDialog.cancel();
                    if (jsonObject.getString("status").equals("success")) {


                        MessageBox("Success","Register Success.....!!");


                    } else{

                        MessageBox("Error","Register Fail.....!!");



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                displayErr();
            }
        };
        register.execute();
    }

    private void displayRe(JSONObject jsonObject) {


    }

    private void displayErr() {
        MessageBox("Error","Register Fail.....!!");
        progressDialog.cancel();

    }

    private  boolean validate(){

        String getname = name.getText().toString().trim();
        String getnic = nic.getText().toString().trim();
        String getaddress = address.getText().toString().trim();
        String gettel = tel.getText().toString().trim();
        String getemail = email.getText().toString().trim();
        String getpassword = password.getText().toString().trim();

        if(getname.isEmpty() && getnic.isEmpty() && getaddress.isEmpty() && gettel.isEmpty() && getemail.isEmpty() && getpassword.isEmpty()){
            MessageBox("Error","Empty Field found..!!");
            return false;
        }else{
            if(gettel.toString().length() <10){
                MessageBox("Error","Mobile Number Invalied..!!");
                return  false;
            }else{
                if(getpassword.toString().length() < 6){
                    MessageBox("Error","Week Password user another password...!!");
                    return  false;
                }else{
                    if(isValid(getemail)){
                        return true;
                    }else{
                        MessageBox("Error","Invalied Email..!!");
                        return false;
                    }
                }
            }
        }

    }

    static boolean isValid(String email) {

        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
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
