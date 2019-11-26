package com.example.nilaksha.schoolbus.ListViewClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nilaksha.schoolbus.ClassFile.DropHome;
import com.example.nilaksha.schoolbus.ClassFile.DropSchool;
import com.example.nilaksha.schoolbus.ClassFile.PickFromHome;
import com.example.nilaksha.schoolbus.ClassFile.PickFromSchool;
import com.example.nilaksha.schoolbus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nilaksha on 11/5/2019.
 */

public class CustomListAdapterSchool extends ArrayAdapter<ChildrenSchool> {

    ArrayList<ChildrenSchool> children;
    Context context;
    int resource;



    public CustomListAdapterSchool(@NonNull Context context, int resource, @NonNull List<ChildrenSchool> objects) {
        super(context, resource, objects);

        this.children = (ArrayList<ChildrenSchool>) objects;
        this.context = context;
        this.resource = resource;

    }


    @Override
    public int getCount() {
        return children.size();
    }

    @Nullable
    @Override
    public ChildrenSchool getItem(int position) {
        return children.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.children_list_school,null,true);
        }

        ChildrenSchool childrenListHome = getItem(position);

        final TextView id = (TextView) convertView.findViewById(R.id.txtid);
        id.setText(childrenListHome.getId());

        final TextView lat = (TextView) convertView.findViewById(R.id.txtlat);
        lat.setText(childrenListHome.getLat());

        final TextView lng = (TextView) convertView.findViewById(R.id.txtlng);
        lng.setText(childrenListHome.getLng());

        final TextView name = (TextView) convertView.findViewById(R.id.txtName);
        name.setText(childrenListHome.getName());

        final TextView school = (TextView) convertView.findViewById(R.id.txtSchool);
        school.setText(childrenListHome.getSchool());

        Button location = (Button) convertView.findViewById(R.id.btnlocation);
        Button drop = (Button) convertView.findViewById(R.id.btndrop);
        final Button pick = (Button) convertView.findViewById(R.id.btnpick);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dbid = id.getText().toString();
                String dbname = name.getText().toString();

                webCallUpdatePickFromSchool(dbid,dbname);


            }
        });

        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dbid = id.getText().toString();
                String dbname = name.getText().toString();

                webCallUpdateDropHome(dbid,dbname);


            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dblat = lat.getText().toString();
                String dblng = lng.getText().toString();

                System.out.println("************ location button clicked -***********" + dblat + " - " + dblng);

                // context.startActivity(new Intent(context, AddChildren.class));

                //load google map
                Uri gmmIntentUri = Uri.parse("geo:0,0?z=15&q="+dblng+","+dblat+"");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);

            }
        });


        return convertView;


    }

    private void webCallUpdatePickFromSchool(String id, final String name){

        PickFromSchool pickFromSchool = new PickFromSchool(id) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {



                        Toast.makeText(context,name +" Picked",Toast.LENGTH_LONG).show();



                    } else{

                        Toast.makeText(context,"Fail try Again..!!",Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void displayError() {

            }
        };
        pickFromSchool.execute();

    }

    private void webCallUpdateDropHome(String id, final String name){

        DropHome dropSchool = new DropHome(id) {
            @Override
            public void displayResult(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("status").equals("success")) {



                        Toast.makeText(context,name +" Dropped",Toast.LENGTH_LONG).show();



                    } else{

                        Toast.makeText(context,"Fail try Again..!!",Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void displayError() {

            }
        };
        dropSchool.execute();



    }

}
