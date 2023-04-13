package ru.AntonSibgatulin.bladeslayer.battle

import android.graphics.Canvas
import android.util.Log
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader


class Background {
    var images: ImageLoader
    var canvas: Canvas
    var player:PlayerController
    var v = 0.0
    var dv = 0.0
    var fastMountain = ArrayList<Sprite>();
    var slowMountain = ArrayList<Sprite>();
    var cloudy = ArrayList<Sprite>()
    var sky:Sprite

    constructor(images: ImageLoader, canvas: Canvas) {
        this.images = images
        this.canvas = canvas
        this.player = PlayerController()
        for (i in 0..3) {
            var img = this.images.get("mountain")
            var w = canvas.width / 100.0 * 60.0;
            Log.e("img","w "+w+" img w "+img.width+" img h "+img.height);

            var h = (img.height / (img.width / w)) / 100 * 90
            var dt = (w / 100 * 95).toInt()
            var sprite =  Sprite(img,
                (-dt + dt * i).toFloat(),
                (canvas.height - h).toFloat(), w.toInt(), h.toInt(), 0, 0, img.width, img.height, dt);
            this.fastMountain.add(sprite)

        }

        for (i in 0..3) {


            var sprite = Sprite(
                this.images["mountain2"],
                0F,
                (canvas.height - canvas.height / 100 * 80).toFloat(),
                (this.images["mountain2"].width / (this.images["mountain2"].height / (canvas.height / 100.0 * 90.0))).toInt(),
                canvas.height / 100 * 90,
                0,
                0,
                this.images["mountain2"].width,
                this.images["mountain2"].height,
                (this.images["mountain2"].width / (this.images["mountain2"].height / (canvas.height / 100.0 * 90.0)) / 100.0 * 80.0).toInt()
            )
            this.slowMountain.add(sprite)

        }

        var size_cloud = 45;
        for (i in 0..15) {
            var img = (this.images.get("cloud2")); // i%2==0?(this.images.get("cloud")):(this.images.get("cloud2"))
            var w = img.width / (img.height / (canvas.height / 100 * size_cloud));
            var posy = 0F
            if(i%2==0){
                posy = (canvas.height / 100 * -10).toFloat()
            }else{
                posy = (canvas.height / 100 * 5).toFloat()
            }
            var cloud =  Sprite(img, ((-5 * w) + w * i).toFloat(), posy /*size_cloud/3*2)*/ , img.width / (img.height / (canvas.height / 100 * size_cloud)), canvas.height / 100 * size_cloud, 0, 0, img.width, img.height, 0)
            //cloud.posx = cloud.w*i
            cloud.delta = w * 2
            // cloud.delta = 2*(this.images.get("cloud2").width / (this.images.get("cloud2").height / (canvas.height / 100 * size_cloud)));
            this.cloudy.add(cloud)
        }
        this.sky = Sprite(this.images.get("background2"), 0F, 0F, canvas.width, canvas.height, 0, 0, this.images.get("background2").width, this.images.get("background2").height, 0)

    }
    fun action(data: Canvas){
        this.sky.draw(data)

        for (i in 0..slowMountain.size-1) {
            this.slowMountain[i].posx -= (this.v).toFloat();
            var el = this.slowMountain[i]
            if (Math.abs(el.posx - el.x0) >= el.delta) {
                el.posx = el.x0;


            }
            this.slowMountain[i].draw(data)
        }



        for (i in 0..fastMountain.size-1) {
            this.fastMountain[i].posx -= (this.v).toFloat();
            var el = this.fastMountain[i]
            if (Math.abs(el.posx - el.x0) >= el.delta) {
                el.posx = el.x0;


            }
            this.fastMountain[i].draw(data)
        }
        for (i in 0..cloudy.size-1) {

            this.cloudy[i].draw(data)
            this.cloudy[i].posx -= this.v.toFloat();
            this.cloudy[i].posx -= 0.3F;
            if (Math.abs(this.cloudy[i].posx - this.cloudy[i].x0) >= this.cloudy[i].delta) {
                this.cloudy[i].posx = this.cloudy[i].x0;


            }
        }

    }

}