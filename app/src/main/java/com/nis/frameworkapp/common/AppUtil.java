package com.nis.frameworkapp.common;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AppUtil {
    public static String getAssetFileContentAsString(Context c, String fileName){
        StringBuilder buf = new StringBuilder();
        InputStream json = null;
        String str = "";
        try {
            json = c.getAssets().open(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            while ((str = in.readLine()) != null){
                buf.append(str);
            }
            in.close();
        }catch (IOException io){
            return io.getMessage();
        }
        return buf.toString();
    }
}
