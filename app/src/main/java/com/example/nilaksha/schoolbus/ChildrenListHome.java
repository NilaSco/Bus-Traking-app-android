package com.example.nilaksha.schoolbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.LoadDriversChildren;
import com.example.nilaksha.schoolbus.ClassFile.LoadTodayAttendance;
import com.example.nilaksha.schoolbus.ListViewClass.ChildrenHome;
import com.example.nilaksha.schoolbus.ListViewClass.CustomListAdapterHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChildrenListHome extends AppCompatActivity {

    ArrayList<ChildrenHome> arrayList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_list_home);

        arrayList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.lstchildren) ;

        webCallGettodayAtt();
    }


    private void webCallGettodayAtt(){
        LoadTodayAttendance loadChildren = new LoadTodayAttendance() {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {


                        JSONArray getvlaue = jsonObject.getJSONArray("msg");


                        for (int i = 0; i < getvlaue.length(); i++) {

                            JSONObject contactObject = new JSONObject(getvlaue.get(i).toString());

                            String driver = contactObject.getString("driverid");



                            if(driver.equals(Login.USERID)){

                                String id = contactObject.getString("id");
                                String lat = contactObject.getString("homeLat");
                                String lng = contactObject.getString("homeLng");


                                arrayList.add(new ChildrenHome(contactObject.getString("childrenName"),contactObject.getString("school"),id,lat,lng));
                            }

                        }
                        CustomListAdapterHome adapter = new CustomListAdapterHome(getApplicationContext(), R.layout.children_list_home_layout,arrayList);
                        listView.setAdapter(adapter);


                    } else{
                        Toast.makeText(ChildrenListHome.this,"No Result found....!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void displayError() {
                Toast.makeText(ChildrenListHome.this, "cant load", Toast.LENGTH_SHORT).show();
            }
        };
        loadChildren.execute();

    }
}
