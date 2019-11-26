package com.example.nilaksha.schoolbus;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadNotification;
import com.example.nilaksha.schoolbus.ClassFile.UpdateNotiStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ParentHome extends AppCompatActivity {

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        setSingleEvent(mainGrid);

        if(Login.USETYPE.equals("Parent")){
            getNotificationUpdate();
        }






    }
    private void getNotificationUpdate(){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                getNotifi();

            }

        },0,50000);//Update text every second

    }

    private void Notification(String title, String msg , String id){


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.schoolbus)
                .setContentTitle(title)
                .setContentText(msg);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        upadteNotification(id);



    }

    private void upadteNotification(String id){

        UpdateNotiStatus updateNotiStatus = new UpdateNotiStatus(id) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        Toast.makeText(ParentHome.this,"Success",Toast.LENGTH_LONG).show();


                    } else{

                        Toast.makeText(ParentHome.this,"fail",Toast.LENGTH_LONG).show();



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void displayError() {
                Toast.makeText(ParentHome.this,"fail",Toast.LENGTH_LONG).show();
            }
        };
        updateNotiStatus.execute();



    }

    private void getNotifi(){

        LoadNotification loadNotification = new LoadNotification() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");

                        String message = "";
                        String dbid = "";

                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String cName = contactObject.getString("parentnic");



                            if(cName.equals(Login.USERID)){

                                String dbtime = contactObject.getString("msgtime");
                                String dbmsg = contactObject.getString("msg");
                                String dbname = contactObject.getString("childrenName");
                                dbid = contactObject.getString("id");

                                message += dbname + " " + dbmsg + " at " + dbtime + "";

                            }

                            if(Login.USETYPE.equals("Parent")){
                                Notification("Children Status",message ,dbid);
                            }



                        }




                    } else{
                        //Toast.makeText(ParentHome.this,"error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void displayError() {

            }
        };
        loadNotification.execute();


    }


    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(finalI == 0){

                        Intent i = new Intent(ParentHome.this,ParentChildrenRegister.class);
                        startActivity(i);
                    }

                    if(finalI == 1){
                        Intent i = new Intent(ParentHome.this,ViewLocation.class);
                        startActivity(i);

                    }

                    if(finalI == 2){

                        Intent i = new Intent(ParentHome.this,ChildrenAttendance.class);
                        startActivity(i);

                    }

                    if(finalI == 3){

                        Intent i = new Intent(ParentHome.this,FeeStatus.class);
                        startActivity(i);

                    }

                    if(finalI == 4){

                        Intent i = new Intent(ParentHome.this,ReportHomeParent.class);
                        startActivity(i);

                    }

                }
            });
        }
    }
}
