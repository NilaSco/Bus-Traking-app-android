package com.example.nilaksha.schoolbus.ClassFile;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by Nilaksha on 11/2/2019.
 */

public abstract class SaveDriversChildren extends AsyncTask<Void, Void, String> {

    String drdiverNIC,parentNIC,childrenName,school,fee,respond;

    public SaveDriversChildren(String drdiverNIC, String parentNIC, String childrenName, String school, String fee) {
        this.drdiverNIC = drdiverNIC;
        this.parentNIC = parentNIC;
        this.childrenName = childrenName;
        this.school = school;
        this.fee = fee;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            displayError();
        } else {
            try {
                JSONObject jsonResult = new JSONObject(result);
                this.respond = jsonResult.toString();
                System.out.println("jsonResult" +jsonResult);
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

    @Override
    protected String doInBackground(Void... voids) {

        OkHTTPClient okHTTPClient = new OkHTTPClient();

        try {
            this.respond = okHTTPClient.saveDriversChildren(drdiverNIC,parentNIC,childrenName,school,fee);
            System.out.println("respond" +respond);
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
        }
        return this.respond;
    }
}
