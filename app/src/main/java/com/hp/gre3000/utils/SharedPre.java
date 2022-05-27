package com.hp.gre3000.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPre {
    private static SharedPreferences sharedPreferences;
    private static Context context;
    private static SharedPreferences.Editor editor;

    public static void init(Context con) {
        context = con;
        sharedPreferences = context.getSharedPreferences("SharedPreUtilGRE", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static boolean set(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static boolean set(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(String name) {
        return getString(name, "");
    }

    public static String getString(String name, String defaultValue) {
        return sharedPreferences.getString(name, defaultValue);
    }

    public static boolean set(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static boolean set(String key, long value) {
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    //把对象序列化后 存到sp
    public static void set(String key, Object obj) {
        try {
            String value = new Gson().toJson(obj);
            set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T getObject(String name, Class<T> clazz) {
        T acc = null;
        try {
            String json = getString(name);
            acc = new Gson().fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public static <T> T getObject(String name, Type type) {
        T acc = null;
        try {
            String json = getString(name);
            acc = new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public static <T> void set(String key, List<T> list) {
        try {
            String value = new Gson().toJson(list);
            set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> getList(String name, final Class<T> clazz) {
        List<T> acc = null;
        try {
            String json = getString(name);
            Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clazz);
            acc = new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

//    public static void setEncryption(String name, String flag) {
//        set(name, AESUtil.encrypt2str(flag, JNIUtil.getPwd(context)));
//    }
//
//    public static String getSDecryption(String name) {
//        return AESUtil.decrypt2str(getString(name, ""), JNIUtil.getPwd(context));
//    }
}
