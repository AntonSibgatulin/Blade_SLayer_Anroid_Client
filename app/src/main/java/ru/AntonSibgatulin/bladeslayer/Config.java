package ru.AntonSibgatulin.bladeslayer;

public class Config {
public int START_SIZE = 24;
    public double SIZE_CHANGE_CANVAS_PHONE = 1.6; //1.3
    public double scale = 3.2;
    public double dt = 0;
    public float SIZE = 0;
    public double hightTop = 1.55;
    public int innerWidth = 0;
    public int innerHeight = 0;


    public String country=null;
    public String lan="";
    public double rounded(double number){
        String result = String.format("%.2f",number);
        return Double.valueOf(result.replace(",","."));
    }
}
