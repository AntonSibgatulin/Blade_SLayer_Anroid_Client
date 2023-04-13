package ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader;


import org.json.JSONObject;

public class StyleModel {
    public long length = 0;
    public boolean contr = false;
    public String name = null;
    public EBreath style = null;
    public int maxTime = 0;
    public int minTime = 0;
    public int power = 0;
    public double energy = 0;
    public int target = 0;
    public String styles = null;
    public int type = 0;
    public JSONObject jsonObject = null;
    public boolean has = false;

    public StyleModel(EBreath style, long length, boolean contr, String name, int maxTime, int minTime, int power,
                      double energy, int target, String styles, int type, JSONObject jsonObject, boolean has) {

        this.style = style;
        this.length = length;
        this.contr = contr;
        this.name = name;
        this.maxTime = maxTime;
        this.minTime = minTime;
        this.power = power;
        this.energy = energy;
        this.target = target;
        this.styles = styles;
        this.type = type;
        this.jsonObject = jsonObject;
        this.has = has;

    }

}
