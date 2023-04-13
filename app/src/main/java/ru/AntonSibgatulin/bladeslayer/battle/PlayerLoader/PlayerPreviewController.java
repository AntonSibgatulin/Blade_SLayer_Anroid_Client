package ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerPreviewController {
    public ArrayList<PlayerPreview> array = new ArrayList<>();
    public HashMap<String,PlayerPreview > hash = new HashMap<>();
    public void add(PlayerPreview playerPreview){
    boolean add = true;
    for(int i =0;i<array.size();i++){
        PlayerPreview pp = array.get(i);
        if(pp==null)continue;

        if(pp.id.equals(playerPreview.id)){
            array.remove(pp);
            array.add(playerPreview);
            add = false;
        }


    }
        if (add) {

            this.array.add(playerPreview);
        }
        this.hash.put(playerPreview.id, playerPreview);

    }

    public PlayerPreview get(String id) {
        return this.hash.get(id);
    }
}
