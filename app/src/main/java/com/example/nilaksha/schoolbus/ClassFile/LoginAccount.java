package com.example.nilaksha.schoolbus.ClassFile;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by Damith on 5/29/2018.
 */
public abstract class LoginAccount extends AsyncTask<Void, Void, String> {
    //String
    private String email,
            password,
            respond;

    public LoginAccount(String email,String password) {
        this.email = email;
        this.password = password;

    }

    protected String doInBackground(Void... arg0) {


        OkHTTPClient okHTTPClient = new OkHTTPClient();

        try {
            this.respond = okHTTPClient.login(email,password);
            System.out.println("respond" + respond);
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
        return this.respond;
    }

    protected void onPostExecute(String result) {

        if (result == null) {
            displayError();
        } else {
            try {
                JSONObject jsonResult = new JSONObject(result);
                this.respond = jsonResult.toString();
                System.out.println("jsonResult" + jsonResult);
                displayResult(jsonResult);
            } catch (Exception e) {

                System.out.println("Listen for jobs" + e.getMessage());
                displayError();
            } finally {
                System.out.println("Listen for jobs f");
            }
        }
        super.onPostExecute(result);
    }

    public abstract void displayResult(JSONObject jsonObject);

    public abstract void displayError();
}