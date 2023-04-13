package ru.AntonSibgatulin.bladeslayer.battle

import android.graphics.Bitmap
import android.graphics.Canvas
import ru.AntonSibgatulin.bladeslayer.MainActivity
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController
import ru.AntonSibgatulin.bladeslayer.Players.Vector
import java.util.*

class Animate : Cloneable {

    @kotlin.jvm.JvmField
    var side: Int = 0
    public var type_moving = 0;
    var array = ArrayList<Bitmap>();
    var index = 0;
    var style = 0;
    var px = 0f;
    var py = 0f;
    var angle = 0;
    var dk = 0.05;
    var radius = 15;
    var speed = 3;
    var type: String = "";
    var position = Vector();
    var mainImage: Bitmap? = null;
    var animateAnimate = ArrayList<SpriteAnimate>();
    var start_degress = 0;
    var k = 0.0;
    var period = 30;
    var degress = 0;


    constructor() {
        init(40)
    }

    public override fun clone(): Any {
        return super.clone();
    }

    fun init(period: Int) {
        this.period = period;
        this.animateAnimate = ArrayList<SpriteAnimate>();

        for (i in 0..this.period - 1) {
            this.animateAnimate.add(SpriteAnimate())
        }
    }

    fun draw(data: Canvas) {
        for (i in 0..animateAnimate.size - 1) {
            this.animateAnimate[i].draw(data)
        }
    }

    fun update(player: PlayerController, canvas: Canvas) {


        if (this.index >= this.array.size) {
            this.index = 0;
        }
        this.index++;
        var posx =
            canvas.width / 3 + player.w / MainActivity.connection.config.dt / 2
        var posy =
            canvas.height / 3 * MainActivity.connection.config.hightTop + player.h / MainActivity.connection.config.dt / 2
    if(player.enemy){
         posx =
             player.position.x - MainActivity.connection.player.position.x + canvas.width / 3 + player.w / MainActivity.connection.config.dt / 2
         posy =
             player.position.y - MainActivity.connection.player.position.y + canvas.height / 3 * MainActivity.connection.config.hightTop + player.h / MainActivity.connection.config.dt / 2
    }
        //this.mainImage = this.array[this.index]
        //from top to right bottom
        //about from 100 to -40
        if (this.type_moving == 0) {


            /* if (this.k <= 1) {
                 this.k = 1
                 // this.degress=90;
             } else {
                 this.k += this.dk;
             }
             */
            if (side == 1) {
                if (this.k <= 2 && this.k >= 0) {
                    this.k += this.dk;
                    if (this.k >= 1) {
                        this.k = -1.0;
                        this.degress += 140
                    }
                }
                if (this.k < 0) {
                    if (this.k + this.dk < 0) {
                        this.k += this.dk;
                        this.degress += 3;
                    } else {
                        player.animateAnimate = null;

                    }
                }
            } else {
                if (this.k >= -2 && this.k <= 0) {
                    this.k -= this.dk;
                    if (this.k <= -1) {
                        this.k = 1.0;
                        this.degress -= 140
                    }
                }
                if (this.k > 0) {
                    if (this.k - this.dk > 0) {
                        this.k -= this.dk;
                        this.degress -= 3;
                    } else {
                        player.animateAnimate = null;

                    }
                }
            }
            for (i in 0..this.animateAnimate.size - 1) {

                var animateAnimate = this.animateAnimate[i]
                var ind_rand = randomIntFromInterval(0, this.array.size)
                if (ind_rand >= this.array.size) ind_rand -= this.array.size
                animateAnimate.image = this.array[ind_rand];

                animateAnimate.position.x = (posx + Math.cos(
                    from_degress_to_radian(this.degress.toDouble()) +
                            from_degress_to_radian(140 / this.period * i * this.k)
                ) * this.radius / MainActivity.connection.config.dt).toFloat()
                animateAnimate.position.y = (posy + Math.sin(
                    from_degress_to_radian(this.degress.toDouble()) +
                            from_degress_to_radian(140 / this.period * i * this.k)
                ) * this.radius / MainActivity.connection.config.dt).toFloat()

            }
        }


        //moving on circle
        //360 degress
        if (this.type_moving == 1) {

            if (side == 1) {
                if (this.k >= 1) {
                    if (this.k >= 2) {
                        this.k = -1.0;
                        // this.k=1;
                    } else {
                        this.degress += this.speed;
                        this.k += this.dk
                    }
                } else if (this.k < 1 && this.k >= 0) {
                    this.k += this.dk;
                }
                if (this.k < 0) {
                    if (this.k + this.dk < 0) {
                        this.k += this.dk;
                        this.degress += 7;
                    } else {
                        player.animateAnimate = null;

                    }
                }

            } else {
                if (this.k <= 1) {
                    if (this.k <= -2) {
                        this.k = 1.0;
                        // this.k=1;
                    } else {
                        this.degress -= this.speed;
                        this.k -= this.dk
                    }
                } else if (this.k > -1 && this.k <= 0) {
                    this.k -= this.dk;
                }
                if (this.k > 0) {
                    if (this.k - this.dk > 0) {
                        this.k -= this.dk;
                        this.degress -= 7;
                    } else {
                        player.animateAnimate = null;

                    }
                }

            }
            for (i in 0..this.animateAnimate.size - 1) {

                var animateAnimate = this.animateAnimate[i]
                var ind_rand = randomIntFromInterval(0, this.array.size)
                if (ind_rand >= this.array.size) ind_rand -= this.array.size
                animateAnimate.image = this.array[ind_rand];

                animateAnimate.position.x = (posx + Math.cos(
                    from_degress_to_radian(this.degress.toDouble()) +
                            from_degress_to_radian(360 / this.period * i * this.k)
                ) * this.radius / MainActivity.connection.config.dt).toFloat()
                animateAnimate.position.y = (posy + Math.sin(
                    from_degress_to_radian(this.degress.toDouble()) +
                            from_degress_to_radian(360 / this.period * i * this.k)
                ) * this.radius / MainActivity.connection.config.dt).toFloat()

            }
            /*// this.canvas.width / 3, this.canvas.height / 3 * hightTop, player.w / dt, player.h / dt
             this.position.x = posx + Math.cos(from_degress_to_radian(this.degress)) * this.radius
             this.position.y = posy + Math.sin(from_degress_to_radian(this.degress)) * this.radius
             */
        }
        //moving fro bottom to top
        //from minus 90 to the 90
        if (this.type_moving == 2) {

            //  this.degress=-100;
            /* if (this.k <= 1) {
                 this.k = 1
                 // this.degress=90;
             } else {
                 this.k += 0.05;
             }
             */
            if (side == 1) {
                if (this.k <= 1 && this.k >= 0) {
                    this.k += this.dk;
                    if (this.k >= 1) {
                        this.k = -1.0;
                        this.degress += 200;
                    }
                }
                if (this.k < 0) {
                    if (this.k + this.dk < 0) {
                        this.k += this.dk;
                        this.degress += 3;
                    } else {
                        player.animateAnimate = null;
                    }
                }
            } else {
                if (this.k >= -1 && this.k <= 0) {
                    this.k -= this.dk;
                    if (this.k <= -1) {
                        this.k = 1.0;
                        this.degress -= 200;
                    }
                }
                if (this.k > 0) {
                    if (this.k - this.dk > 0) {
                        this.k -= this.dk;
                        this.degress -= 3;
                    } else {
                        player.animateAnimate = null;
                    }
                }
            }
            for (i in 0..this.animateAnimate.size - 1) {

                var animateAnimate = this.animateAnimate[i]
                var ind_rand = randomIntFromInterval(0, this.array.size)
                if (ind_rand >= this.array.size) ind_rand -= this.array.size
                animateAnimate.image = this.array[ind_rand];

                animateAnimate.position.x = (posx + Math.cos(
                    from_degress_to_radian(this.degress.toDouble()) +
                            from_degress_to_radian(200 / this.period * i * this.k)
                ) * this.radius / MainActivity.connection.config.dt).toFloat()
                animateAnimate.position.y = (posy + Math.sin(
                    from_degress_to_radian(this.degress.toDouble()) +
                            from_degress_to_radian(200 / this.period * i * this.k)
                ) * this.radius / MainActivity.connection.config.dt).toFloat()

            }
        }
        if (this.type_moving == 3) {

            //  this.degress=-100;
            /*   if (this.k <= 1) {
                   this.k = 1
                    this.degress+=this.speed;
               } else {
                   this.k += 0.05;
               }

              if(this.k<=1){
               this.k += 0.05;
              }
              */
            if (side == 1) {
                this.degress += this.speed;
            } else {
                this.degress -= this.speed;
            }
            for (i in 0..this.animateAnimate.size - 1) {

                var animateAnimate = this.animateAnimate[i]
                var ind_rand = randomIntFromInterval(0, this.array.size)
                if (ind_rand >= this.array.size) ind_rand -= this.array.size
                animateAnimate.image = this.array[ind_rand];

                animateAnimate.position.x =
                    (posx - player.w / 3 / MainActivity.connection.config.dt - i * animateAnimate.image!!.width / 4 / MainActivity.connection.config.dt).toFloat()
                animateAnimate.position.y = (posy + Math.sin(
                    (from_degress_to_radian(this.degress.toDouble()) +
                            from_degress_to_radian(360.0 / this.period.toDouble() * i.toDouble())) * 4
                ) * this.radius / MainActivity.connection.config.dt).toFloat()

            }
        }

        if (this.type_moving == 4) {
             this.degress += this.speed;

            for (i in 0..this.animateAnimate.size - 1) {

                var animateAnimate = this.animateAnimate[i]
                var ind_rand = randomIntFromInterval(0, this.array.size)
                if (ind_rand >= this.array.size) ind_rand -= this.array.size
                animateAnimate.image = this.array[ind_rand];

                animateAnimate.position.x =
                    (posx + player.w / MainActivity.connection.config.dt - i * animateAnimate.image!!.width / MainActivity.connection.config.dt).toFloat()
                animateAnimate.position.y = posy.toFloat()
            }
            if (this.degress >= 360) {
                player.animateAnimate = null;

            }
        }
    }

    fun randomIntFromInterval(i: Int, j: Int): Int {
        return i + Random().nextInt(j)
    }

    fun from_degress_to_radian(degress: Double): Double {
        return degress * Math.PI / 180;

    }
}