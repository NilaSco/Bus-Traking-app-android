package com.example.nilaksha.schoolbus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.Constant;
import com.example.nilaksha.schoolbus.ClassFile.LoginAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {

    Button login;
    EditText uName,Password;
    TextView register;

    private Constant constant;

    ProgressDialog progressDialog;

    public static String USERID;
    public static String USETYPE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        constant = new Constant();

        uName = (EditText) findViewById(R.id.txtUserName);
        Password = (EditText) findViewById(R.id.txtPassword);


        login = (Button) findViewById(R.id.btnSignin);

        register = (TextView) findViewById(R.id.tvsign_up);

        progressDialog = new ProgressDialog(Login.this);





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getUserName = uName.getText().toString().trim();
                String getPassword = Password.getText().toString().trim();

                if(getUserName.isEmpty() && getPassword.isEmpty()){

                    MessageBox("Error","Please Enter UserName And Password");

                }else{

                    progressDialog.setTitle("Loading.");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();
                    webCall(getUserName,getPassword);


                }

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this,RegisterParent.class);
                startActivity(register);
            }
        });

    }

    private void webCall(String userName,String password){
        LoginAccount loginAccount = new LoginAccount( userName, password) {
            @Override
            public void displayResult(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")) {

                        JSONArray getlaue = jsonObject.getJSONArray("msg");

                        for (int i = 0; i < getlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getlaue.get(i).toString());

                            String typeTxt = contactObject.getString("utype");
                            String userid = contactObject.getString("uname");

                            USERID = userid;
                            USETYPE = typeTxt;

                            uName.setText("");
                            Password.setText("");

                            //check user type
                            if(typeTxt.equals("Driver")){
                                progressDialog.cancel();
                                Intent intent = new Intent(Login.this, DriverHome.class);
                                startActivity(intent);
                            }

                            if(typeTxt.equals("Parent")){
                                progressDialog.cancel();
                                Intent intent = new Intent(Login.this, ParentHome.class);
                                startActivity(intent);
                            }

                        }

                    } else{

                        Toast.makeText(Login.this, constant.errorMSG, Toast.LENGTH_SHORT).show();

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
        loginAccount.execute();
    }


    private void displayErr() {

        MessageBox("Error","UserName or password invalid... try again.");
        progressDialog.cancel();

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
