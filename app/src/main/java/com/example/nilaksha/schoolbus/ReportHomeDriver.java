package com.example.nilaksha.schoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReportHomeDriver extends AppCompatActivity {

    private Button payment,income,due;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_home_driver);

        payment = (Button) findViewById(R.id.btnPaymentReport);
        income = (Button) findViewById(R.id.btnincome);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportHomeDriver.this,ReportPaymentDriver.class);
                startActivity(intent);

            }
        });


        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ReportHomeDriver.this,IncomeReportDriver.class);
                startActivity(intent);

            }
        });

    }
}
