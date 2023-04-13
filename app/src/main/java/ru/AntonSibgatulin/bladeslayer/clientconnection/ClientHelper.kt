package ru.AntonSibgatulin.bladeslayer.clientconnection

import android.graphics.Bitmap
import org.json.JSONArray
import org.json.JSONObject
import ru.AntonSibgatulin.bladeslayer.battle.Animate
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.PlayerPreview
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader
import ru.AntonSibgatulin.bladeslayer.loader.JSONLoader


class ClientHelper {
    var site = ""
    var clientWebSocket: ClientWebSocket
    var images: ImageLoader

var map:Map<String,String> = HashMap<String,String>()


    constructor( clientWebSocket: ClientWebSocket) {

        this.site = clientWebSocket.site;
        this.clientWebSocket = clientWebSocket
        images = clientWebSocket.mainActivity.imageLoader
        images.setVersion(clientWebSocket.version)
    }
    fun loadJSONSprite(){
        var json = JSONLoader.jsonObjectLoadeFromURLStatic(site+"res/animate.json")
        var iterator = json.keys();
        var length = 0;

        while(iterator.hasNext()){
            var str = iterator.next();
           length++;

        }
         iterator = json.keys();
        var general = 0;
        while(iterator.hasNext()){
            var names = iterator.next();
            var jlement = json[names] as JSONArray;
            for(l in 0..jlement.length()-1){
                general ++ ;
                var element = jlement[l]as JSONObject
                var URL = element["url"] as String;
                var index = 0;
                var image = images.loadFromUrlImage(site+""+URL,"sprite_attack_"+general,clientWebSocket.version,clientWebSocket.mainActivity);

                var animate = Animate()

                if(element.has("countX") && element.has("dtX")){
                    for(i in 0..element["countX"]as Int-1){
                        if(element.has("countY") && element.has("dtY")){
                            for(d in 0..element["countY"]as Int-1){
                                index ++;
                                var newSizeX = 1;
                                var newSizeY = 1
                                if(element.has("newSizeX")){
                                    newSizeX = element["newSizeX"] as Int

                                }
                                if(element.has("newSizeY")){
                                    newSizeY = element["newSizeY"] as Int

                                }

                                var img =getImage(image!!, i * element["dtX"] as Int, d * element["dtY"] as Int, element["dtX"] as Int, element["dtY"] as Int, 32 * newSizeX / clientWebSocket.config.dt, 32 * newSizeY / clientWebSocket.config.dt)
                              // Log.e("error",names + "_sprite_" + element["name"] as String + "_" + index)
                                images.set(names + "_sprite_" + element["name"] as String + "_" + index, img)
                                animate.array.add(img)
                            }
                        }else{
                            index++;
                            var newSizeX = 1;
                            var newSizeY = 1
                            if(element.has("newSizeX")){
                                newSizeX = element["newSizeX"] as Int

                            }
                            if(element.has("newSizeY")){
                                newSizeY = element["newSizeY"] as Int

                            }

                            var img = getImage(image, i * element["dtX"] as Int, 0, element["dtX"] as Int, element["dtX"]as Int, 32 *
                                    newSizeX / clientWebSocket.config.dt,
                                32 * newSizeY / clientWebSocket.config.dt);

                            animate.array.add(img)
                            images.set(names + "_sprite_" + element["name"]as String + "_" + index, img)

                        }



                    }
                    images.set(names + "_sprite_len_" + element["name"]as String, index)
                }

                images.set("animate_" + element["name"]as String + "_" + l, animate)
            }
        }

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


    fun loadJsonFromPlayer(playerPreview: PlayerPreview) {
        var json =
            JSONLoader.jsonObjectLoadeFromURLStatic(site + "img/union/" + playerPreview.id + "/index.json?id=");
        if (json.getJSONObject("run").has("folder") == false || json.has("styles") == false) {
            return;
        }
        if (json.has("stay_top")) {
            ///images.images.put(
            //    playerPreview.id + "_stay_top", json.getInt("stay_top")
            //)
            images.set(playerPreview.id + "_stay_top", json.getInt("stay_top"))

        }
        var run = json.getJSONObject("run")
        var folderToString = run.getString("folder")
        var folder_run = run.getString("folder") + "_small_web"
        var start = run.getInt("start")
        var end = run.getInt("end")
        var index = 0;
        var bitmap = images.loadFromUrlImage(
            site + "img/union/" + playerPreview.id + "/stay_small_web.png?id=",
            playerPreview.id + "_stay",
            clientWebSocket.version,
            clientWebSocket.mainActivity
        );
        if(bitmap!=null) {
            images.set(playerPreview.id + "_stay_h", images.scaleHorizotal(bitmap));
        }



        for (i in (start)..(end)) {
            var bitmap = images.loadFromUrlImage(
                site + "img/union/" + playerPreview.id.toString() + "/" + (folderToString + "_small_web").toString() + "/index_00" + (if (i < 100) if (i > 9) "0$i" else "00$i" else i).toString() + ".png?id=",
                playerPreview.id.toString() + "_" + folder_run + "_" + index,
                clientWebSocket.version,
                clientWebSocket.mainActivity
            )
            if(bitmap!=null)
            images.set(playerPreview.id.toString() + "_" + folder_run + "_" + index+"_h", images.scaleHorizotal(bitmap));

            index++
        }


        if (run.has("stay_top")) {
            images.set(playerPreview.id + "_run_top", run.getInt("stay_top"))
        }
        images.set(playerPreview.id + "_run_length", (end - start))
        var ends = json.getJSONArray("styles").length()-1

        for (i in 0..(ends)) {
            var json1 = (json.getJSONArray("styles")[i] as JSONObject)
            var id = json1["id"] as Int

            var end = json1["end"] as Int
            var index = 0
            for (j in (json1["start"] as Int)..(end)) {
               var bitmap =  images.loadFromUrlImage(site+
                    "img/union/" + playerPreview.id.toString() + "/" + (json1["folder"] as String + "_small_web").toString() + "/index_00" + (if (j < 100) if (j > 9) "0$j" else "00$j" else j).toString() + ".png?id=",
                    playerPreview.id.toString() + "_" + id + "_" + index,
                    clientWebSocket.version,
                    clientWebSocket.mainActivity
                )
                if (bitmap!=null)
                images.set(playerPreview.id.toString() + "_" + id + "_" + index+"_h", images.scaleHorizotal(bitmap));

                index++
            }
            images.set(
                playerPreview.id + "_" + id + "_length",
                (json1["end"] as Int - json1["start"] as Int)
            )

            if (json1.has("stay_top"))
                images.set(playerPreview.id + "_" + id + "_top", json1["stay_top"] as Int)

        }
        playerPreview.json_anim = json


    }
}