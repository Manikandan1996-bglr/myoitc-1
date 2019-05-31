package com.velozion.myoitc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JAGADISH on 7/19/2018.
 */

public class PreferenceUtil {

    public static String saveData(String key, String value , Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();

        return key;
    }

    public static String getData(String key, Context context){
        SharedPreferences prefs = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void clearUser(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
