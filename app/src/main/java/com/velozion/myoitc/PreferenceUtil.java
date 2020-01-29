package com.velozion.myoitc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    public static void saveData(String key, String value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static void saveUserType(String key, int value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public static String getData(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static int getUserType(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public static void clearUser(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Utils.appName, Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
