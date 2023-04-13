package ru.AntonSibgatulin.bladeslayer.battle.npc

import android.graphics.Bitmap
import android.graphics.Canvas
import org.json.JSONObject
import ru.AntonSibgatulin.bladeslayer.Config
import ru.AntonSibgatulin.bladeslayer.MainActivity
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader
import java.util.*
import kotlin.collections.ArrayList

class BallModel {



    lateinit var json : JSONObject
    var id = "";
    var x = 0f;
    var y = 0f
    var animate = ArrayList<Bitmap>();
    var index = 0;


    var config = MainActivity.connection.config;

    constructor(json:JSONObject,images:ImageLoader){
        this.json = json
        this.x = json.getDouble("x").toFloat()
        this.y = json.getDouble("y").toFloat()
        this.animate = images.getS("animate_fireballs_0").array

        index = Random().nextInt(animate.size-1)
    }

    fun draw(data:Canvas){
        if(this.animate !=null){
            var ppx = this.x/config.START_SIZE*config.SIZE - MainActivity.connection.player.position.x+data.width/3;
            var ppy = this.y/config.START_SIZE*config.SIZE - MainActivity.connection.player.position.y+data.height/3*config.hightTop;


            data.drawBitmap(this.animate.get(index),ppx.toFloat(),ppy.toFloat(),null)

        }
    }
    fun update(json:JSONObject){
        x = (json.getDouble("x")/config.START_SIZE*config.SIZE).toFloat();
        y = (json.getDouble("y")/config.START_SIZE*config.SIZE).toFloat();

    }

}