package ru.AntonSibgatulin.bladeslayer.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONLoader {
    public JSONObject jsonObjectLoadeFromURL(String string){
        byte[] buffer = null;


        try {
            URL url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            int size = input.available();
            buffer = new byte[size];
            input.read(buffer);
            input.close();
            String str_data = new String(buffer);
            return new JSONObject(str_data);


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public static JSONObject jsonObjectLoadeFromURLStatic(String string){

     //   byte[] buffer = null;


        try {
            URL url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");


            }

            return new JSONObject(buffer.toString());
            /*
            int size = input.available();
            buffer = new byte[size];
            input.read(buffer);
            input.close();
            String str_data = new String(buffer);
             return new JSONObject(str_data);

             */


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }
}
