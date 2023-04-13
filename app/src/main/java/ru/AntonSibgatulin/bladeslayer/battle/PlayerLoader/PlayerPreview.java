package ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader;

import android.graphics.Bitmap;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import ru.AntonSibgatulin.bladeslayer.MainActivity;

public class PlayerPreview {
    public String id = null;
    public String name = null;
    public String text = null;

    public JSONObject json = null;
    public Bitmap image_sources = null;

    public JSONObject json_anim = null;

    public PlayerPreview(JSONObject json,Bitmap bitmap){
        try {
            this.id = json.getString("id");
            this.json = json;
            this.name = json.getString("name"+ MainActivity.connection.config.lan);
            this.text = json.getString("text"+MainActivity.connection.config.lan);
            this.image_sources = bitmap;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
