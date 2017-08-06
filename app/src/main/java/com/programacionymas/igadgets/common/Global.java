package com.programacionymas.igadgets.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

public class Global {

    public static void showMessageDialog(Context context, String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

    public static void saveIntGlobalPreference(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveStringGlobalPreference(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int getIntFromGlobalPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    public static String getStringFromGlobalPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

}
