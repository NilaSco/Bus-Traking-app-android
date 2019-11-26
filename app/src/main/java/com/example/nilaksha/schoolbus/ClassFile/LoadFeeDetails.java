package com.example.nilaksha.schoolbus.ClassFile;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by Nilaksha on 11/5/2019.
 */

public abstract class LoadFeeDetails extends AsyncTask<Void, Void, String> {

    private String respond;

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

    @Override
    protected String doInBackground(Void... voids) {
        OkHTTPClient okHTTPClient = new OkHTTPClient();
        try {
            this.respond = okHTTPClient.getFeeStatus();
            System.out.println("respond" +respond);
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
        }
        return this.respond;
    }

    public abstract void displayResult(JSONObject jsonObject);

    public abstract void displayError();

}
