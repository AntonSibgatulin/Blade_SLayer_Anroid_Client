package ru.AntonSibgatulin.bladeslayer.battle

import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Image
import ru.AntonSibgatulin.bladeslayer.Players.Vector

class SpriteAnimate {
    var image: Bitmap? = null;
    var index = 0;
    var position = Vector();
    fun draw(data:Canvas)
    {
        if(this.image!=null){
            data.drawBitmap(this.image!!,this.position.x - this.image!!.width / 2, this.position.y - this.image!!
                .height / 2,null)
        }
    }
}