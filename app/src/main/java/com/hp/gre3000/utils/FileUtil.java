package com.hp.gre3000.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileUtil {
    public static String readLocalJson(Context context, String fileName){
        String jsonString="";
        String resultString="";
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                    context.getResources().getAssets().open(fileName)));
            while ((jsonString=bufferedReader.readLine())!=null) {
                resultString+=jsonString;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultString;
    }
}
