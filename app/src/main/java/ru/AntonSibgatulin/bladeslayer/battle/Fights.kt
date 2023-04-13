package ru.AntonSibgatulin.bladeslayer.battle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.json.JSONArray
import org.json.JSONObject
import ru.AntonSibgatulin.bladeslayer.MainActivity
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController

class Fights {
    var paint = Paint()
    var fights: JSONArray = JSONArray()

    lateinit var player: PlayerController

    constructor(player: PlayerController) {
        fights = JSONArray()
        //#53f35fb0
        //var color = Color.parseColor("#4dfe2a73")
        var color = Color.parseColor("#53f35fb0")
        paint.setColor(color)
        this.player = player
    }

    constructor() {

    }

    fun action(data: Canvas) {
        for (i in 0..fights.length() - 1) {
            var element = fights[i] as JSONObject
            var x =
                element.getDouble("x") / MainActivity.connection.config.START_SIZE * MainActivity.connection.config.SIZE - MainActivity.connection.player.position.x + data.width / 3
            var y =
                element.getDouble("y") / MainActivity.connection.config.START_SIZE * MainActivity.connection.config.SIZE - MainActivity.connection.player.position.y + data.height / 3 * MainActivity.connection.config.hightTop
            var w =
                element.getDouble("w") / MainActivity.connection.config.START_SIZE * MainActivity.connection.config.SIZE
            var h =
                element.getDouble("h") / MainActivity.connection.config.START_SIZE * MainActivity.connection.config.SIZE
            //   var color = Color.parseColor("#000000")
            //   paint.setColor(color)
            if (player.enemy) {
                var color = Color.parseColor("#f35353b0")
                paint.setColor(color)
            }
            data.drawRect(
                x.toFloat(),
                y.toFloat(),
                (x + w / MainActivity.connection.config.dt).toFloat(),
                (y + h / MainActivity.connection.config.dt).toFloat(),
                paint
            );
        }
    }
}