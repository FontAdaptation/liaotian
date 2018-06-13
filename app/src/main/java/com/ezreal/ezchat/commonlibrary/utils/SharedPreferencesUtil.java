package com.ezreal.ezchat.commonlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 仝心
 */

public class SharedPreferencesUtil {


    public static void setIntSharedPreferences(Context context, String name, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setStringSharedPreferences(Context context, String name, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains(key)) {
            editor.remove(key);
            editor.commit();
        }
        editor.putString(key, value);
        editor.commit();
    }

    public static int getIntSharedPreferences(Context context, String name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, 0);
        return value;
    }
    public static String getStringSharedPreferences(Context context, String name, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value;
    }


}
