package ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class BreathPreviewController {
    public ArrayList<BreathPreview> array = new ArrayList<>();
    public HashMap<String,BreathPreview> hash = new HashMap<>();

    public void add(BreathPreview preview){
        this.array.add(preview);
        this.hash.put(preview.id,preview);

    }
}
