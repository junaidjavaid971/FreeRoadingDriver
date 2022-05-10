package com.apps.freeroadingdriver.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.apps.freeroadingdriver.new_modelll.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BMSPrefs {
    static String PREF_NAME = "nannyhunt_pref";
    private static SharedPreferences sharedPrefs;

    private static void getInstance(Context context) {
        if (sharedPrefs == null) {
            sharedPrefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        }
    }

    public static String putString(Context context, String key, String value) {
        getInstance(context);
        Editor edit = sharedPrefs.edit();
        edit.putString(key, value);
        edit.commit();
        return key;
    }


    public static String putLong(Context context, String key, long value) {
        getInstance(context);
        Editor edit = sharedPrefs.edit();
        edit.putLong(key, value);
        edit.commit();
        return key;
    }
    public static long getLong(Context context, String key) {
        getInstance(context);
        return sharedPrefs.getLong(key, 0);
    }
    public static String putBoolean(Context context, String key, Boolean value) {
        getInstance(context);
        Editor edit = sharedPrefs.edit();
        edit.putBoolean(key, value.booleanValue());
        edit.commit();
        return key;
    }

    public static String getString(Context context, String key) {
        getInstance(context);
        return sharedPrefs.getString(key, "");
    }

    public static Boolean getBoolean(Context context, String key) {
        getInstance(context);
        return Boolean.valueOf(sharedPrefs.getBoolean(key, true));
    }
    public static Profile getListUser(Context context, String key){
        getInstance(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(key, "");
        Type type = new TypeToken<List<Profile>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static String putListData(Context context, String key, Profile list)
    {
        getInstance(context);
        Editor edit = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        edit.putString(key, json);
        edit.commit();
        return key;
    }
}
