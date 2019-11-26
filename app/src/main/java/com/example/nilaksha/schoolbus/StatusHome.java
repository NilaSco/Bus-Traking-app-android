package com.example.nilaksha.schoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StatusHome extends AppCompatActivity {

    private Button home,school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_home);

        home = (Button) findViewById(R.id.btnHome);
        school = (Button)findViewById(R.id.btnSchool);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StatusHome.this,ChildrenListHome.class);
                startActivity(intent);
            }
        });


        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusHome.this,ChildrenListSchool.class);
                startActivity(intent);

            }
        });


    }
}
