package ru.AntonSibgatulin.bladeslayer.loader;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.nfc.Tag;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;

import android.util.Log;
import android.util.SparseArray;

import androidx.collection.ArrayMap;

import androidx.core.app.ActivityCompat;
/*
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;


 */
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.battle.Animate;
import ru.AntonSibgatulin.bladeslayer.battle.Sprite;
import ru.AntonSibgatulin.bladeslayer.battle.SpriteAnimate;

public class ImageLoader {
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public  Bitmap scaleHorizotal(Bitmap source){
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, source.getWidth()/2, source.getHeight()/2);
        //matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }
    public HashMap<String,Object> images = new HashMap<>();
    public HashMap<String,String> images_ = new HashMap<>();
    //public ArrayMap<String,Object> images = new ArrayMap<>();
    public int version = 0;
    public int compress = 75;
    public Bitmap get(String str){
        Bitmap bitmap = (Bitmap)images.get(str+"v"+version);
        if(bitmap==null){
           // Log.e("image","null");
            String url = images_.get(str+"v"+version);
            //Log.e("url",str+"v"+version);
            if(url==null)return null;
            else{
                return loadFromUrlImage(url,str,version,MainActivity.connection.mainActivity);
            }
        }
        return bitmap;
    }
    public Animate getS(String str){
        return (Animate) images.get(str+"v"+version);
    }
    /*
    public Texture getTexture(String str){
        return (Texture)images.get(str+"v"+version+"texture");
    }


     */

    public Integer get1(String str){
        return (Integer)images.get(str+"v"+version);
    }
    public void setVersion(int i){
        this.version = i;
    }

    public Bitmap loadFromUrlImage(String string, String name,int v , MainActivity mainActivity)
    {
     /*   ActivityCompat.requestPermissions(mainActivity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
*/
        if(name!=null) {
            String textName = name + "texturev" + v;
            name = name + "v" + v;
        }

        int count;

        String all_name = name+"."+(string.split("\\.")[string.split("\\.").length-1]);

            if(images.get(name)==null){
                try {
                    InputStream input = mainActivity.openFileInput(all_name);
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    input.close();
                   // if()

                    //images.put(textName,tex);
                    images.put(name,myBitmap);
                    images_.put(name,string);
                  //  Log.e(name,string);
                    return myBitmap;
                } catch (FileNotFoundException e) {
                    try{
                      //e.printStackTrace();
                    URL url = new URL(string);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                   // images.put(textName,tex);

                    images.put(name,myBitmap);
                    images_.put(name,string);
                    input = connection.getInputStream();

                    // String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    String root = Environment.getExternalStorageDirectory().toString();

                    // OutputStream output = new FileOutputStream(folder+"/");
                    FileOutputStream output = mainActivity.openFileOutput( all_name, Context.MODE_PRIVATE);
                  /* byte data[] = new byte[input.available()];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // writing data to file
                        output.write(data, 0, count);
                        //System.out.println(count);
                    }

                   */
                    myBitmap.compress(Bitmap.CompressFormat.WEBP, compress, output); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                   return myBitmap;

                    } catch (IOException e1) {
                        e1.printStackTrace();
                       // Log.e("return","null 161");
                        return null;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                  //  Log.e("return","null 168");
                    return null;
                }

            }else{
                return (Bitmap)images.get(name);
            }

    }






    public void set( String s,  Object stayTop) {
        images.put(s+"v"+version,stayTop);
    }

    public void setit( String s,  Object stayTop) {
        images.put(s,stayTop);
    }

}
