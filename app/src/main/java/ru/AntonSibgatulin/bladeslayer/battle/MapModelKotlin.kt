package ru.AntonSibgatulin.bladeslayer.battle

import android.R.attr
import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.Button
import android.widget.TextView
import ru.AntonSibgatulin.bladeslayer.Config
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController
import ru.AntonSibgatulin.bladeslayer.battle.npc.NPCDeamon
import ru.AntonSibgatulin.bladeslayer.clientconnection.ClientWebSocket
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader
import kotlin.math.floor as floor1


class MapModelKotlin : View, Runnable {
    var pingView: TextView? = null;

    var mapController: MapController
    var backMain: Background? = null;
    var fpsView: TextView? = null;
    var paint = Paint()
    var config: Config;
    var isWork = true;
    var mainThread = Thread(this)
    var images: ImageLoader;
    var connection: ClientWebSocket;
    var debugFunction = true;
    var START_SIZE: Int
    lateinit var pauseButton: Button
    var SIZE: Float
    var dt: Double
    var hightTop = 0.0;

    var player: PlayerController

    var image_ground_tile: Bitmap;
    var image_grass: Bitmap
    var image_ground: Bitmap

    var image_coin: Bitmap
    var image_glizinia: Bitmap
    var image_glizinia2: Bitmap
    var image_grass_piece: Bitmap;

    var image_trees: Bitmap
    var image_amor: Bitmap
    var image_health: Bitmap
    var image_power: Bitmap
    var image_nitro: Bitmap
    var image_heart:Bitmap
    var image_house1: Bitmap
    var image_house2: Bitmap
    var image_house3: Bitmap
    var image_grass_left: Bitmap
    var image_grass_right: Bitmap
    var finish = false;


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


    constructor(
        context: Context,

        connection: ClientWebSocket,


        ) : super(context) {

        this.connection = connection;
        this.mapController = connection.mapMain
        this.images = connection.mainActivity.imageLoader;
        this.images.setVersion(connection.version)
        this.player = connection.player!!
        config = this.connection.config;


        SIZE = config.SIZE

        this.hightTop = config.hightTop
        dt = config.dt


        START_SIZE = config.START_SIZE

        image_grass = this.images.get("grass");
        image_ground = this.images.get("ground");

        image_grass_piece = getImage(image_grass, 19, 0, 14, 15, SIZE, SIZE)

        image_grass_left = getImage(image_grass, 0, 18 * 3, 15, 15, SIZE, SIZE)
        image_grass_right = getImage(image_grass, 18, 18 * 3, 15, 15, SIZE, SIZE)


        image_ground_tile = this.images.get("ground_tile");
        image_ground_tile = getImage(image_ground_tile, SIZE, SIZE)

        var igd = this.images.get("glizinia");

        if (igd == null) throw Exception("igd is null");

        image_glizinia =
            getImage(igd, 0, 0, igd.width, igd.height, START_SIZE * 8 / dt, START_SIZE * 8 / dt)

        var igd2 = this.images.get("glizinia2")
        if (igd2 == null) throw Exception("igd2 is null");

        image_glizinia2 =
            getImage(igd2, 0, 0, igd2.width, igd2.height, START_SIZE * 8 / dt, START_SIZE * 8 / dt)

        var it = this.images.get("trees")
        if (it == null) throw Exception("trees is null");

        image_trees =
            getImage(it, 0, 0, it.width, it.height, START_SIZE * 8 / dt, START_SIZE * 8 / dt)


        image_house1 = getImage(
            this.images.get("house1"),
            0,
            0,
            this.images.get("house1").width,
            this.images.get("house1").height,
            START_SIZE * 12 / dt,
            START_SIZE * 8 / dt
        );

        image_house2 = getImage(
            this.images.get("house2"),
            0,
            0,
            this.images.get("house2").width,
            this.images.get("house2").height,
            START_SIZE * 12 / dt,
            START_SIZE * 8 / dt
        );

        image_house3 = getImage(
            this.images.get("house3"),
            0,
            0,
            this.images.get("house3").width,
            this.images.get("house3").height,
            START_SIZE * 12 / dt,
            START_SIZE * 8 / dt
        );
        animate =
            images.get("" + player.id + "_stay_small");

        image_coin = getImage(this.images.get("coin"), SIZE, SIZE)

        image_amor = getImage(this.images.get("amor"), SIZE, SIZE)
        image_health = getImage(this.images.get("health"), SIZE, SIZE)
        image_power = getImage(this.images.get("power"), SIZE, SIZE)
        image_nitro = getImage(this.images.get("nitro"), SIZE, SIZE)
        image_heart = getImage(this.images.get("heart"),SIZE,SIZE)

    }


    var fps = 0;
    var xxx: Float = 1F;
    var time = System.currentTimeMillis()
    var cadrs = 0;
    var animate: Bitmap? = null;
    var canvas: Canvas = Canvas()

    override fun onDraw(data: Canvas) {
        // connection.mainActivity.runOnUiThread {
        this.canvas = data;

        if (System.currentTimeMillis() - time >= 1000) {
            time = System.currentTimeMillis();
            fps = cadrs;
            cadrs = 0;
            if (fpsView != null) {
                connection.mainActivity.runOnUiThread(Runnable() {
                    run() {
                        fpsView!!.setText("FPS: " + fps);
                    }
                });
            }
        } else {
            cadrs++;
        }
        if (images == null) {
            return//@runOnUiThread
        }
        var size = SIZE;
        var px = player.position.x;

        var py = player.position.y




        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
        if (backMain != null) {
            backMain!!.action(data)
        } else {
            backMain = Background(images, data)
        }

        // canvas.drawRect(0F, 0F, canvas.width.toFloat(), canvas.height.toFloat(), paint)
        // data.drawBitmap(image_ground_tile, 50F, 50F, paint)
        //Log.e("server",""+image_ground_tile.width)
        // Log.e("fps", "" + fps)
        var startI: Int =
            (floor1(px / SIZE - this.canvas.width / SIZE / 3) - 8).toInt();
        var endI: Int =
            floor1(px / SIZE + this.canvas.width / SIZE / 3 * 2.1).toInt();


        var startE: Int =
            floor1(py / SIZE - this.canvas.height / SIZE / 3 * 1.18 + 0.7).toInt()

        var endE: Int =
            (floor1(py / SIZE + this.canvas.height / SIZE / 3 * (hightTop + 0.7)) + 8).toInt()
        if (startE < 0) startE = 0;
        if (startI < 0) startI = 0
        if (startI >= mapController.arr.size) startI = mapController.arr.size - 1;
        if (startE >= mapController.arr[0].size) startE = mapController.arr[0].size - 1;

        if (endE < 0) endE = 0;
        if (endI < 0) endI = 0
        if (endI >= mapController.arr.size) endI = mapController.arr.size - 1;
        if (endE >= mapController.arr[0].size) endE = mapController.arr[0].size - 1;

        for (i in (startI)..(endI)) {
            /* if (i < 0 || i >= mapController.arr.size) {
                 continue;
             }

             */



            for (j in (startE)..(endE)) {


                /* if (j < 0 || j >= mapController.arr[0].size) {
                     continue;
                 }

                 */
                //  px = player.position.x;

                // py = player.position.y

                var posx: Float =
                    (i * size - px + (this.canvas.width / 3))

                var posy: Float =
                    (py * -1 + j * size + this.canvas.height / 3 * hightTop).toFloat();

                if (mapController.arr[i][j] == 2 || mapController.arr[i][j] == 1) {
                    //  Log.e("position", "" + posx)


                    if (mapController.arr[i][j] == 2) {

                        if (i > 0 && mapController.arr[i - 1][j] == 2 && mapController.arr[i][j - 1] == 2) {
                            data.drawBitmap(
                                image_ground_tile,
                                posx,
                                posy,
                                paint
                            )

                        } else if (1 + i < mapController.arr.size && mapController.arr[i + 1][j] == 2 && mapController.arr[i][j - 1] == 2) {
                            data.drawBitmap(
                                image_ground_tile,
                                posx,
                                posy,
                                paint
                            )

                        } else if (mapController.arr[i][j + 1] == 2 && i + 1 < mapController.arr.size && mapController.arr[i + 1][j] == 2) {
                            data.drawBitmap(
                                image_grass_left,
                                posx,
                                posy,
                                paint
                            )

                        } else if (mapController.arr[i][j + 1] == 2 && j + 1 < mapController.arr[0].size && mapController.arr[i - 1][j] == 2 && i > 0) {
                            data.drawBitmap(
                                image_grass_right,
                                posx,
                                posy,
                                paint
                            )

                        } else {

                            //   data.drawImage()
                            data.drawBitmap(
                                image_grass_piece,
                                posx,
                                posy,
                                paint
                            );
                            // data.drawImage(image_grass_piece,0,0,image_grass_piece.width,image_grass_piece.height,i * size - this.player.position.x + this.canvas.width / 3, this.player.position.y * -1 + j * size + this.canvas.height / 3 * hightTop, SIZE, SIZE)
                            // data.drawImage(image_grass, 19, 0, 14, 15, i * size - this.player.position.x + this.canvas.width / 3, this.player.position.y * -1 + j * size + this.canvas.height / 3 * hightTop, SIZE, SIZE)
                        }
                    }


                    if (mapController.arr[i][j] == 1) {

                        // console.log(g)

                        // data.drawImage(image_ground_piece, i * size - this.player.position.x + this.canvas.width / 3, this.player.position.y * -1 + j * size + this.canvas.height / 3 * hightTop);

                        //  image_ground_piece
                        data.drawBitmap(
                            image_ground_tile,
                            (posx),
                            (posy),
                            paint
                        )


                    }


                }

                var opx: Float =
                    posx;
                var opy: Float =
                    posy;

                if (mapController.arr[i][j] == 3) {
                    //  console.log("glizinia ...")
                    data.drawBitmap(
                        image_glizinia,
                        opx.toFloat(),
                        (opy - START_SIZE * 6 / dt).toFloat(),
                        paint
                    )
                    // data.drawImage(image_glizinia, 0, 0, image_glizinia.width, image_glizinia.height, i * size - this.player.position.x + this.canvas.width / 3-image_glizinia.width/2/dt, this.player.position.y * -1 + j * size + this.canvas.height / 3 * hightTop - START_SIZE * 6 / dt, START_SIZE * 8 / dt, START_SIZE * 8 / dt);
                }
                if (mapController.arr[i][j] == 9) {
                    //  console.log("glizinia ...")
                    data.drawBitmap(
                        image_glizinia2,
                        opx.toFloat(),
                        (opy - START_SIZE * 6.5 / dt).toFloat(),
                        paint
                    );

                    //data.drawImage(image_glizinia2, 0, 0, image_glizinia2.width, image_glizinia2.height, i * size - this.player.position.x + this.canvas.width / 3, this.player.position.y * -1 + j * size + this.canvas.height / 3 * hightTop - START_SIZE * 6.5 / dt, START_SIZE * 8 / dt, START_SIZE * 8 / dt);
                }
                if (mapController.arr[i][j] == 8) {
                    // data.fillStyle = "#000000"
                    //data.fillRect( i * size - this.player.position.x + this.canvas.width / 3, this.player.position.y * -1 + j * size + this.canvas.height / 3 * hightTop, SIZE, SIZE)

                    data.drawBitmap(
                        image_trees,
                        opx.toFloat(),
                        (opy - START_SIZE * 6 / dt).toFloat(),
                        paint
                    )
                    //  console.log("glizinia ...")
                    //data.drawImage(image_trees, 0, 0, image_trees.width, image_trees.height, i * size - this.player.position.x + this.canvas.width / 3-image_trees.width/2/dt, this.player.position.y * -1 + j * size + this.canvas.height / 3 * hightTop - START_SIZE * 6 / dt, START_SIZE * 8 / dt, START_SIZE * 8 / dt);
                }


                if (mapController.arr[i][j] == 200) {

                    data.drawBitmap(image_coin, posx, posy, paint)

                }
                if (mapController.arr[i][j] == 4) {


                    data.drawBitmap(image_health, posx, posy, paint)

                }
                if (mapController.arr[i][j] == 5) {

                    data.drawBitmap(image_power, posx, posy, paint)

                }

                if (mapController.arr[i][j] == 6) {

                    data.drawBitmap(image_amor, posx, posy, paint)

                }


                if (mapController.arr[i][j] == 7) {
                    //  console.log("nitro")
                    data.drawBitmap(image_nitro, posx, posy, paint)

                }
                if (mapController.arr[i][j] == 11) {
                    data.drawBitmap(
                        image_house1,
                        posx,
                        (posy - START_SIZE * 7 / dt).toFloat(),
                        paint
                    )
                }
                if (mapController.arr[i][j] == 12) {
                    data.drawBitmap(
                        image_house2,
                        posx,
                        (posy - START_SIZE * 6 / dt).toFloat(),
                        paint
                    )
                }
                if (mapController.arr[i][j] == 13) {
                    data.drawBitmap(
                        image_house3,
                        posx,
                        (posy - START_SIZE * 7 / dt).toFloat(),
                        paint
                    )
                }


            }
        }

        //draw all npc from array,it in mapController
     //  var npc = mapController.npcMap;//.clone() as HashMap<String,NPCDeamon>
        for (entry in  mapController.npcMap.entries.iterator()) {
            // print("${entry.key} : ${entry.value}")
            entry.value.draw(data)
        }


        //draw player
        if(this.player != null){
            player.canvas = data;

        if (this.player.animate != null) {

            var ww = 160 / 1.3;
            var hh = 120 / 1.3;
            var top = 0;
            if (this.player.type == 50 && images.get1(this.player.id + "_stay_top") != null) {
                top = images.get1(this.player.id + "_stay_top");
            }
            if (this.player.type == 51 && images.get1(this.player.id + "_run_top") != null) {
                top = images.get1(this.player.id + "_run_top");
            }
            for (l in 0..20 - 1) {
                if (this.player.type == l) {
                    if (images.get1(this.player.id + "_" + l + "_top") != null)
                        top = images.get1(this.player.id + "_" + l + "_top")
                }
            }

            var playerX = this.canvas.width / 3 - ((ww - player.w) / 2 / dt)
            var playerY = this.canvas.height / 3 * hightTop - top / dt


            var px = (this.canvas.width / 3 - player.w * 0.25).toFloat()
            var py = (this.canvas.height / 3 * hightTop - START_SIZE / dt).toFloat()

            var g = 0;

            for (elemento in player.inventory.clone() as (HashMap<String,String>)){
                var value = elemento.value
                var key = elemento.key
                var element = value;
                var img_interface:Bitmap?=null;

                if (element == "amor") {
                    img_interface = image_amor

                }

                if (element == "power") {
                    img_interface = image_power

                }

                if (element == "nitro") {
                    img_interface = image_nitro

                }
                if (img_interface != null) {
                    if (this.player.inventory.size == 1) {
                        data.drawBitmap(img_interface, (px + ww / dt / 4).toFloat(),
                            (py - START_SIZE / 2 / dt - img_interface.height * 2 - 8 / dt).toFloat(),null);

                    }
                    if (this.player.inventory.size == 2) {
                        data.drawBitmap(img_interface, (px + ww / dt / 12 * (g) + (ww / dt - ww /
                                dt / 12 * this.player.inventory.size - img_interface.width * this
                            .player.inventory.size) / 4).toFloat(), (py - START_SIZE / 2 / dt -
                                img_interface.height * 2 - 8 / dt).toFloat(),null);

                    }
                    if (this.player.inventory.size == 3) {
                        data.drawBitmap(img_interface, (px + ww / dt / 12 * (g) + (ww / dt - ww /
                                dt / 12 * this.player.inventory.size - img_interface.width * this
                            .player.inventory.size) / 5).toFloat(), (py - START_SIZE / 2 / dt -
                                img_interface.height * 2 - 8 / dt).toFloat(),null);

                    }


                }
                g += 2;

            }

                var r = 0
                while (r < player.heart * 2) {
                    val img_interface = image_heart
                    if (img_interface != null) {
                        if (player.heart === 1) {
                            data.drawBitmap(
                                img_interface, (px + ww / dt / 4).toFloat(),
                                (py - START_SIZE / 2 / dt - img_interface.height - 3 / dt).toFloat(),null
                            )
                        }
                        if (player.heart === 2) {
                            data.drawBitmap(
                                img_interface,
                                (px + ww / dt / 12 * r + (ww / dt - ww /
                                        dt / 12 * player.heart - img_interface.width * player.heart) / 4).toFloat(),
                                (py - START_SIZE / 2 / dt - img_interface.height - 3 / dt).toFloat(),null
                            )
                        }
                        if (player.heart === 3) {
                            data.drawBitmap(
                                img_interface,
                                (px + ww / dt / 12 * r + (ww / dt - ww /
                                        dt / 12 * player.heart - img_interface.width * player.heart) / 5).toFloat(),
                                (py - START_SIZE / 2 / dt - img_interface.height - 3 / dt).toFloat(),null
                            )
                        }
                    }
                    r += 2
                }



            //draw health and power line on the gameView

            var color = Color.parseColor("#1a73e8")
            paint.setColor(color)
            // paint.setARGB(26,115,232,1)

            data.drawRoundRect(
                px,
                py,
                px + ((player.w * 1.5 * 100 / 100) / dt).toFloat(),
                py + (START_SIZE / 2f / dt).toFloat(), 5f, 5f, paint
            );

            if (this.player.health >= 80) {
                color = Color.parseColor("#7fbf62")
                paint.setColor(color)
            }
            if (this.player.health < 80 && this.player.health >= 50) {
                var color = Color.parseColor("#1a0dab")
                paint.setColor(color)

                // data.fillStyle = "#1a0dab"
            }

            if (this.player.health < 50 && this.player.health >= 20) {
                var color = Color.parseColor("#b77649")
                paint.setColor(color)

                // data.fillStyle = "#b77649"
            }
            if (this.player.health < 20) {
                var color = Color.parseColor("#b74949")
                paint.setColor(color)

                // data.fillStyle = "#b74949"
            }

            data.drawRoundRect(
                px,
                py,
                px + ((player.w * 1.5 * player.health / 100) / dt).toFloat(),
                py + (START_SIZE / 2f / dt).toFloat(), 5f, 5f, paint
            );






            color = Color.parseColor("#7f8001")
            paint.setColor(color)
            data.drawRoundRect(
                px,
                (py + (START_SIZE / 2f / dt) + 2 / dt).toFloat(),
                px + ((player.w * 1.5 * 100 / 100) / dt).toFloat(),
                py + (START_SIZE / 2f / dt + (START_SIZE / 2f / dt) + 2 / dt).toFloat(),
                5f,
                5f,
                paint
            );

            color = Color.parseColor("#d4d606");
            paint.setColor(color)

            data.drawRoundRect(
                px,
                (py + (START_SIZE / 2f / dt) + 2 / dt).toFloat(),
                px + ((player.w * 1.5 * player.energy / 100) / dt).toFloat(),
                py + (START_SIZE / 2f / dt + (START_SIZE / 2f / dt) + 2 / dt).toFloat(),
                9f,
                9f,
                paint
            );

            if(player.animateAnimate!=null){
                player.animateAnimate!!.draw(data)
            }
            player.fights.action(data)



            paint.textSize = 22F;
            paint.setColor(Color.BLUE)
            data.drawText(player.login,(px + ww / dt / 4 - paint.textSize*player.login.length/4).toFloat(),
                (py - START_SIZE / 6 / dt).toFloat(),paint)


            //draw bitmap of player
            if(player.animate!=null) {
                data.drawBitmap(
                    player.animate,
                    (playerX).toFloat(),
                    (playerY).toFloat(),
                    paint
                )
            }

            // Log.e("energy and helath",""+player.energy+" "+player.health)

        }
        }


        // draw enemys
        for (i in 0..mapController.anotherPlayers.size - 1) {
            var enemy_player = this.mapController.anotherPlayers[i];
            if (enemy_player.animate == null) continue;


            var ppx = enemy_player.position.x - this.player.position.x + this.canvas.width / 3;
            var ppy =
                enemy_player.position.y - this.player.position.y + this.canvas.height / 3 * hightTop;

            if (ppx >= width) continue;
            if (ppy >= height) continue;
            if (ppx <= -50) continue;
            if (ppy <= -50) continue;




            if (enemy_player.side == 1) {

            }

            var ww = 160 / 1.3;
            var hh = 120 / 1.3;
            var top = 0;

            if (enemy_player.type == 50 && images.get1(enemy_player.id + "_stay_top") != null) {
                top = images.get1(enemy_player.id + "_stay_top")
            }
            if (enemy_player.type == 51 && images.get1(enemy_player.id + "_run_top") != null) {
                top = images.get1(enemy_player.id + "_run_top")
            }



            for (l in 0..20) {
                if (enemy_player.type == l) {
                    if (images.get1(enemy_player.id + "_" + l + "_top") != null)
                        top = images.get1(enemy_player.id + "_" + l + "_top")
                }
            }




            var g = 0;

            for (elemento in enemy_player.inventory.clone() as HashMap<String,String>){


                var element = elemento.value;
                var img_interface:Bitmap?=null;

                if (element == "amor") {
                    img_interface = image_amor

                }

                if (element == "power") {
                    img_interface = image_power

                }

                if (element == "nitro") {
                    img_interface = image_nitro

                }
                if (img_interface != null) {
                    if (enemy_player.inventory.size == 1) {
                        data.drawBitmap(img_interface, (ppx + ww / dt / 8).toFloat(),
                            (ppy - START_SIZE / 2 / dt - img_interface.height  - 8 / dt).toFloat(),null);

                    }
                    if (enemy_player.inventory.size == 2) {
                        data.drawBitmap(img_interface, (ppx + ww / dt / 12 * (g) + (ww / dt - ww /
                                dt / 12 * this.player.inventory.size - img_interface.width * this
                            .player.inventory.size) / 10).toFloat(), (ppy - START_SIZE / 2 / dt -
                                img_interface.height  - 8 / dt).toFloat(),null);

                    }
                    if (enemy_player.inventory.size == 3) {
                        data.drawBitmap(img_interface, (ppx + ww / dt / 12 * (g) + (ww / dt - ww /
                                dt / 12 * this.player.inventory.size - img_interface.width * this
                            .player.inventory.size) /16).toFloat(), (ppy - START_SIZE / 2 / dt -
                                img_interface.height  - 8 / dt).toFloat(),null);

                    }


                }
                g += 2;

            }



            enemy_player.canvas = data;
            if(enemy_player.animateAnimate!=null){
                enemy_player.animateAnimate!!.draw(data)
            }



            paint.textSize = 22F;
            paint.setColor(Color.RED)
            data.drawText(enemy_player.login,(ppx + enemy_player.w/2 / dt  - paint.textSize*enemy_player.login.length/4).toFloat(),
                (ppy - START_SIZE / 6 / dt).toFloat(),paint)


            enemy_player.fights.action(data)
            var delta = 0f
            if(enemy_player.side==1){
                delta = 0f;// (ww/dt/16).toFloat()
            }
            data.drawBitmap(
                enemy_player.animate,
                (ppx - ((ww - enemy_player.w) / 2 / dt)+delta).toFloat(),
                (ppy - top / dt).toFloat(),
                null
            )


        }


        mapController.fights.action(data)
        //if(player!=null && player.action.isInterrupted()==false)
        postInvalidate()

        //invalidate()


    }


    fun update() {

        //  reinit()
    }


    fun stop() {
        isWork = false;
//        mainThread.stop()

        mainThread.interrupt()

    }

    fun start() {
        isWork = true
        mainThread.start()
    }

    /*
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawColor(Color.GREEN, PorterDuff.Mode.CLEAR)
            paint.setColor(Color.GREEN)
            canvas.drawRect(0F, 0F, 10F, 10F,paint)
        }
        */
/*
    fun draw() {
        // var holder = holder;
        //  var surface = holder.surface;
        if (holder.surface.isValid) {
            var canvas = holder.surface.lockCanvas(null) // surface.lockCanvas(null);

            // synchronized(holder) {
            paint(canvas);
            //}


            holder.surface.unlockCanvasAndPost(canvas)


        }
    }


 */
    private fun setColor(color: Int) {
        paint.setColor(color)
    }


    override fun run() {
        //  while (isWork) {
        // Thread.sleep(20)
        // xxx+=0.5F
        //Log.e("xxx",""+xxx+" "+fps)
        // update();

        //  draw();

        //  }
    }


    fun remove_user(id:Int) {
        for (i in mapController.anotherPlayers) {
            var enemy_player = i
            if (enemy_player.idPlayer == id) {
              mapController.anotherPlayers.remove(i);
                break;

            }
        }
        //this.anotherPlayers.delete(id)
    }
}