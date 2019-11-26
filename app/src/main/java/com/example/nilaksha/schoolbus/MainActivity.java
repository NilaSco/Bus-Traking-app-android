package com.example.nilaksha.schoolbus;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;

    //set splash screen view time
    private static int Splash_Time_Out = 4000; //milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent login = new Intent(MainActivity.this,Login.class);
                    startActivity(login);
                }
            },Splash_Time_Out);



        }else{
            Intent error = new Intent(MainActivity.this,NetworkError.class);
            startActivity(error);
        }




    }
}
