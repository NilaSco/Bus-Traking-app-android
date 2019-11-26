package com.example.nilaksha.schoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReportHomeParent extends AppCompatActivity {

    private Button vehiclefee,droppick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_home_parent);

        vehiclefee = (Button) findViewById(R.id.btnVehicleFee);
        droppick = (Button) findViewById(R.id.btndrop);


        vehiclefee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportHomeParent.this,VehicleFeeReport.class);
                startActivity(intent);

            }
        });


        droppick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportHomeParent.this,DropPickReport.class);
                startActivity(intent);


            }
        });


    }
}
