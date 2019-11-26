package com.example.nilaksha.schoolbus.ClassFile;

import android.content.Context;
import android.content.SharedPreferences;


public class MyPreference{
        private static MyPreference myPreference;
        private static SharedPreferences sharedPreferences;

        public static MyPreference getInstance(Context context) {
            if (myPreference == null) {
                myPreference = new MyPreference(context);
            }
            return myPreference;
        }

        private MyPreference(Context context) {
            sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        }

        public void saveData(String key, String value) {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor .putString(key, value);
            prefsEditor.commit();
        }

        public static String getData(String key) {
            if (sharedPreferences!= null) {
                return sharedPreferences.getString(key, "");
            }
            return "";
        }

}
