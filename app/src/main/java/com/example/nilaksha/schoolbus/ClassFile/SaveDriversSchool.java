package com.example.nilaksha.schoolbus.ClassFile;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by Nilaksha on 11/6/2019.
 */

public abstract class SaveDriversSchool extends AsyncTask<Void, Void, String> {

    String schoolName,driverNIC,respond;

    public SaveDriversSchool(String schoolName, String driverNIC) {
        this.schoolName = schoolName;
        this.driverNIC = driverNIC;
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
            this.respond = okHTTPClient.saveDriversSchool(schoolName,driverNIC);
            System.out.println("respond" +respond);
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
        }
        return this.respond;
    }
}
