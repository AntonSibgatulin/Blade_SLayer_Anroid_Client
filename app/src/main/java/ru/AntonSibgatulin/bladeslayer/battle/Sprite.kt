package ru.AntonSibgatulin.bladeslayer.battle

import android.graphics.Bitmap
import android.graphics.Canvas

class Sprite {
    var posx=0F
    var posy=0F
    var img:Bitmap
    var w=0
    var h =0
    var dx=0
    var dy = 0
    var ddx= 0;
    var ddy = 0
    var delta = 0
    var images:Bitmap?=null
    var x0 = 0F
    var y0 = 0F
    var draw = fun(data:Canvas) {
        //     data.drawImage(this.img, this.dx, this.dy, this.ddx, this.ddy, this.posx, this.posy, this.w, this.h);
        data.drawBitmap(this.images!!, this.posx, this.posy,null);
    }
    constructor(bitmap: Bitmap,posx:Float,posy:Float,w:Int,h:Int,dx:Int,dy:Int,ddx:Int,ddy:Int,delta:Int){
        this.img = bitmap
        this.x0 = posx;
        this.y0 = posy;
        this.posx = posx;
        this.posy = posy
        this.w = w;
        this.h = h;
        this.dx = dx;
        this.dy = dy;
        this.ddx = ddx;
        this.ddy = ddy
        this.delta = delta
        this.images = getImage(this.img, this.dx, this.dy, this.ddx, this.ddy, this.w, this.h)

    }
    fun getImage(b: Bitmap, x: Int, y: Int, wx: Int, wy: Int, nxs: Double, nys: Double): Bitmap {
        var bitmap: Bitmap = Bitmap.createBitmap(b, x, y, wx, wy);
        bitmap = Bitmap.createScaledBitmap(bitmap, nxs.toInt(), nys.toInt(), true)

        return bitmap;
    }

    fun getImage(b: Bitmap, x: Int, y: Int, wx: Int, wy: Int, nxs: Int, nys: Int): Bitmap {
        var bitmap: Bitmap = Bitmap.createBitmap(b, x, y, wx, wy);
        bitmap = Bitmap.createScaledBitmap(bitmap, nxs.toInt(), nys.toInt(), true)

        return bitmap;
    }

    fun getImage(b: Bitmap, x: Int, y: Int, wx: Int, wy: Int, nxs: Float, nys: Float): Bitmap {
        var bitmap: Bitmap = Bitmap.createBitmap(b, x, y, wx, wy);
        bitmap = Bitmap.createScaledBitmap(bitmap, nxs.toInt(), nys.toInt(), true)

        return bitmap;
    }

    fun getImage(b: Bitmap, nx: Double, ny: Double): Bitmap {
        return Bitmap.createScaledBitmap(b, (nx).toInt(), (ny).toInt(), true)
    }

    fun getImage(b: Bitmap, nx: Float, ny: Float): Bitmap {
        return Bitmap.createScaledBitmap(b, (nx).toInt(), (ny).toInt(), true)
    }
}