package ru.AntonSibgatulin.bladeslayer.battle.npc

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.AntonSibgatulin.bladeslayer.MainActivity
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController
import ru.AntonSibgatulin.bladeslayer.Players.Vector
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.BreathPreviewController
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.PlayerPreviewController
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader
import ru.AntonSibgatulin.bladeslayer.swing.Timer
import java.util.concurrent.ConcurrentHashMap

class NPCDeamon {

    var config = MainActivity.connection.config;


    var posx: Float = 0F
    var posy: Float = 0F

    var action: Thread? = null
    var images: ImageLoader
    var playerPreviewController: PlayerPreviewController? = null
    var breathPreviewController: BreathPreviewController? = null
    var enemy = false
    var fire = false
    var position = Vector()
    var img: Bitmap? = null
    var animation: Timer? = null
    var animate: Bitmap? = null
    var animate_shot: Bitmap? = null
    var animation_top = 32 // MainActivity.config.START_SIZE;

    var lines = JSONArray();
    var balls = ConcurrentHashMap<String,BallModel>()

    var id: String = "deamon"
    var idPlayer = 0
    var style = 0
    var json: JSONObject? = null
    var version = 0
    var player: PlayerController
    var type = 51
    var w = 0.0
    var h = 0.0
    var energy = 0.0
    var id_type = 0
    var index = 0
    var health = 0
    var side = 0
     var paint= Paint()
    constructor(json: JSONObject, playerController: PlayerController) {
        this.player = playerController
        this.id = json.get("Type") as String
        w = json.getDouble("w")
        h = json.getDouble("h")
        this.images = playerController.imageLoader
        this.posx =
            (json.getDouble("posx") / MainActivity.connection.mainActivity.config.START_SIZE * MainActivity.connection.mainActivity.config.SIZE).toFloat();
        this.posy =
            (json.getDouble("posy") / MainActivity.connection.mainActivity.config.START_SIZE * MainActivity.connection.mainActivity.config.SIZE).toFloat();
        paint.setColor(Color.WHITE)
        start()

    }

    fun stay() {
        if (this.fire == false) this.type = 50
    }


    fun init() {
        val jsonPlayer: JSONObject = this.json ?: return
        var player: JSONObject? = null
        try {
            player = getPlayerIs()
            // this.id = player!!.getString("id")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun setPosition(x: Float, y: Float) {
        if (this.position.x > x) {
            if (this.fire == false) {
                this.type = 51
            }

            //this.side = -1;
        }
        if (this.position.x < x) {
            if (this.fire == false) {
                this.type = 51
            }

            //  this.side = 1;
        }
        if (this.position.x < x || this.position.x > x) {
            if (!this.enemy) {
                // backMain.moving = true;
            }
        } else {
            if (!this.enemy) {
                // backMain.moving = false;
            }
        }
        if (this.fire == false) {
            if (this.position.x == x) {
                this.type = 50
            }
        }
        this.position.x = x
        this.position.y = y
    }

    //loader image
    fun getPlayerIndex(i: Int): JSONObject? {
        var i = i
        try {
            val players = json!!.getJSONArray("players")
            if (i >= players.length()) {
                i = 0
            }
            if (i < 0) i = players.length() - 1
            return players.getJSONObject(i)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    fun getPlayerIs(): JSONObject? {
        try {
            for (i in 0 until json!!.getJSONArray("players").length()) {
                val jsonObject = json!!.getJSONArray("players").getJSONObject(i)
                if (jsonObject.getBoolean("is")) {
                    return jsonObject
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }


    fun getPlayerIsIndex(): Int {
        try {
            for (i in 0 until json!!.getJSONArray("players").length()) {
                val jsonObject = json!!.getJSONArray("players").getJSONObject(i)
                if (jsonObject.getBoolean("is")) {
                    return i
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    fun draw(canvas: Canvas) {
        var data = canvas
        var ppx = this.posx - this.player!!.position.x + canvas.width / 3;
        var ppy =
            this.posy - this.player!!.position.y + canvas.height / 3 * MainActivity.connection.config.hightTop;
       if (player != null && animate != null) {

            var ww = 160 / 1.3;
            var hh = 120 / 1.3;
            var top = 0;
            if (type == 50 && images.get1(id + "_stay_top") != null) {
                top = images.get1(id + "_stay_top")
            }
            if (type == 51 && images.get1(id + "_run_top") != null) {
                top = images.get1(id + "_run_top")
            }
            for (l in 0..20) {
                if (type == l) {
                    if (images.get1(id + "_" + l + "_top") != null)
                        top = images.get1(id + "_" + l + "_top")
                }
            }

           for(i in 0..lines.length()-1){
                if(i>=lines.length())continue;
               var line = lines.get(i) as JSONObject;
               var ppx = line.getDouble("x")/config.START_SIZE*config.SIZE - MainActivity.connection.player.position.x + data.width/3
               var ppy = line.getDouble("y")/config.START_SIZE*config.SIZE - MainActivity.connection.player.position.y + data.height/3*config.hightTop

               var ppx1 = line.getDouble("endx")/config.START_SIZE*config.SIZE - MainActivity.connection.player.position.x + data.width/3
               var ppy1 = line.getDouble("endy")/config.START_SIZE*config.SIZE - MainActivity.connection.player.position.y + data.height/3*config.hightTop

               data.drawLine(ppx.toFloat(),ppy.toFloat(),ppx1.toFloat(),ppy1.toFloat(),paint)

           }
           var ballsClone = balls;//.clone() as HashMap<String,BallModel>
           var ic = 0;
           for(element in ballsClone){
                if(ic>=ballsClone.size)break;
               element.value.draw(data)
               ic++;

           }




            // data.drawImage(this.player.animate, 0, 0, this.player.animate.width, this.player.animate.height, this.canvas.width / 3 - ((ww - player.w) / 2 / dt), this.canvas.height / 3 * hightTop - top / dt, ww / dt, hh / dt)
    if(animate!=null)
            data.drawBitmap(
                animate!!,

                (ppx - ((ww - player.w + 15) / 2 / MainActivity.connection.config.dt)).toFloat(),
                (ppy - (hh - player.h + 12) / MainActivity.connection.config.dt).toFloat(),
                null
            )

        }

    }


    var start = false
    fun start() {
        if (start) return
        start = true
        action = Thread(Runnable{})
        if(true==true)return;
        action = Thread(Runnable {
            run {

                while (!action!!.isInterrupted) {
                    try {
                        Thread.sleep(40)

                        var ballsClone = balls;//.clone() as ConcurrentHashMap<String, BallModel>
                        for(element in ballsClone){
                            element.value.index++;
                            if(element.value.index>=element.value.animate.size){
                                element.value.index=0;
                            }
                        }



                        if (id != null) {
                            val ww = 160 / 1.3
                            val hh = 120 / 1.3
                            if (images!![id + "_stay_small"] == null) {
                                val bitmap = getImage(
                                    images[id + "_stay"],
                                    (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                                    (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                                )
                                images[id + "_stay_small"] = bitmap
                            }
                            if (images[id + "_stay_smallh"] == null) {
                                val bitmap = getImage(
                                    images[id + "_stay_h"],
                                    (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                                    (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                                )
                                images[id + "_stay_smallh"] = bitmap
                            }
                            if (type == 51) {
                                if (images[id + "_run_small_web_small" + index] == null) {
                                    for (i in 0 until images.get1(id + "_run_length")) {
                                        val bitmap = getImage(
                                            images[id + "_run_small_web_" + i],
                                            (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                                            (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                                        )
                                        images[id + "_run_small_web_small" + i] = bitmap
                                    }
                                }
                                if (images[id + "_run_small_web_smallh" + index] == null) {
                                    for (i in 0 until images.get1(id + "_run_length")) {
                                        val bitmap = getImage(
                                            images[id + "_run_small_web_" + i + "_h"],
                                            (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                                            (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                                        )
                                        images[id + "_run_small_web_smallh" + i] = bitmap
                                    }
                                }
                                animate = if (side == -1) {
                                    images[id + "_run_small_web_small" + index]
                                } else {
                                    images[id + "_run_small_web_smallh" + index]
                                }
                                // console.log(index)
                                // console.log(player.animate)
                                index++
                                if (index >= images.get1(id + "_run_length")) index = 0
                            }
                            if (type == 50) {
                                animate = if (side == -1) {
                                    images[id + "_stay_small"]
                                } else {
                                    images[id + "_stay_smallh"]
                                }
                            }
                            for (i in 0..19) {
                                if (type == i) {
                                    if (fire == false) {
                                        fire = true
                                        index = 0
                                    }
                                    animate = images[id + "_" + type + "_" + index]
                                    // console.log(index)
                                    // console.log(player.animate)
                                    index++
                                    if (index >= images.get1(id + "_" + type + "_length")) {
                                        index = 0
                                        type = 50
                                        fire = false
                                    }
                                }
                            }

                            //  animate = images.get(id + "_stay");
                        } else {
                            init()
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }

        })
        action!!.start()
    }


    fun getImage(bitmap: Bitmap?, nx: Int, ny: Int): Bitmap? {
        if(bitmap==null)return null;
        return Bitmap.createScaledBitmap(bitmap!!, nx, ny, false)
    }

    fun PlayerController() {}

    fun updateDataIs(i: Int, `is`: Boolean) {
        var i = i
        try {
            val players = json!!.getJSONArray("players")
            if (i >= players.length()) {
                i = 0
            }
            if (i < 0) i = players.length() - 1
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        try {
            json!!.getJSONArray("players").getJSONObject(i).put("is", `is`)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun updateId() {
        try {
            id = getPlayerIs()!!.getString("id")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun stop() {
        // action.stop();
        action!!.interrupt()
    }

    fun move(json: JSONObject) {
      //  this.id = json.get("Type") as String
        this.posx =
            (json.getDouble("posx") / MainActivity.connection.mainActivity.config.START_SIZE * MainActivity.connection.mainActivity.config.SIZE).toFloat();
        this.posy =
            (json.getDouble("posy") / MainActivity.connection.mainActivity.config.START_SIZE * MainActivity.connection.mainActivity.config.SIZE).toFloat();
        this.side = json["side"] as Int;
    }
}