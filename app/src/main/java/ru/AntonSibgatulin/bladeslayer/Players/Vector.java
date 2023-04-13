package ru.AntonSibgatulin.bladeslayer.Players;

public class Vector {
    public float x = 0;
    public float y = 0;
    public Vector(float x,float y){
    this.x =x;
    this.y = y;

    }
    public Vector(){

    }
    public void setPosition(float x,float y){
        this.x = x;
        this.y = y;

    }
    public void setX(float x){
        this.x =x;

    }
    public void setY(float y){
        this.y= y;
    }
}
