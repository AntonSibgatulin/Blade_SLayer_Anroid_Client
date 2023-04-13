package ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BreathPreview {
    public  JSONArray styles;
    public  String id = null;
    public JSONObject jsonObject = null;

    public BreathPreview(JSONObject jsonObject){
        this.jsonObject = jsonObject;
        try {
            this.id = jsonObject.getString("id");
            this.styles = jsonObject.getJSONArray("styles");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
