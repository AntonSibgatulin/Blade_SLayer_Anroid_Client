package ru.AntonSibgatulin.bladeslayer.loader;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class LoaderFromAssets {
    public LoaderFromAssets(){

    }
   public String getStringFromAssetFile(Activity activity, String url)
    {
        AssetManager am = activity.getAssets();
        InputStream is = null;
        byte[] buffer = null;

        try {
            is = am.open(url);
          //  String s = convertStreamToString(is);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            String str_data = new String(buffer);
            return str_data;

        } catch (IOException e) {
            e.printStackTrace();
        }
return null;
    }
    public String loadText(String url, Context context){
        String text = url;
        byte[] buffer = null;
        InputStream is;
        try {
            is = context.getAssets().open(text);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str_data = new String(buffer);
        return str_data;
    }
}
