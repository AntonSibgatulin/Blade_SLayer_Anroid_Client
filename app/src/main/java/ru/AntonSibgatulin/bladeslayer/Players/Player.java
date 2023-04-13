package ru.AntonSibgatulin.bladeslayer.Players;



import org.json.JSONObject;

import java.util.ArrayList;

import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.EBreath;

public class Player {
    public JSONObject jsonObject = null;
    public ArrayList<EBreath> breaths = null;
    public double speed = 0;
    public double jump = 0;
    public double go = 0;
    public double speed_shot = 0;
    public double breath = 0;
    public int run = 0;
    public long health = 0;
    public int type = 0;
    public String name = null;

    public Player(ArrayList<EBreath> breaths , double speed, double jump, double go, double speed_shot,
                  double breath, int run, long health, int type, String name, JSONObject jsonObject){
        this.jsonObject = jsonObject;
        this.breaths = breaths;
        this.speed = speed;
        this.jump = jump;
        this.go = go;
        this.speed_shot = speed_shot;
        this.breath = breath;
        this.run = run;
        this.health = health;
        this.type = type;
        this.name = name;



    }


}