package ru.AntonSibgatulin.bladeslayer.battle

import ru.AntonSibgatulin.bladeslayer.MainActivity
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController
import ru.AntonSibgatulin.bladeslayer.battle.npc.NPCDeamon
import java.util.concurrent.ConcurrentHashMap

class MapController : Runnable {
    fun change_tile(i: Int, j: Int, k: Int) {
        this.arr[i].set(j, k);
    }
var play = true;
    var multiplayer = false;
    var thread = Thread(this)
    var npcMap = ConcurrentHashMap<String, NPCDeamon>();// HashMap<String, NPCDeamon>()
    lateinit var arr: ArrayList<ArrayList<Int>>;
    var anotherPlayers = ArrayList<PlayerController>();
    var mapModelKotlin: MapModelKotlin? = null
    var mapModelFragment:MapModelFragment?=null
    var fights: Fights = Fights()

    //var mapModelKotlin:MapModelKt? = null;
    //var mapModel:MapModel ? = null
    constructor(arr: ArrayList<ArrayList<Int>>?, multiplayer: Boolean) {
        if (arr == null) return;
        this.arr = arr!!;
        this.multiplayer = multiplayer
        thread.start()
    }

    fun add(arr: ArrayList<ArrayList<Int>>) {
        for (i in 0..(arr.size - 1)) {
            this.arr.add(arr[i]);
        }

    }

    fun updateNPC() {
        for (element in npcMap) {
            var npc = element.value
            var ballsClone = npc.balls;//.clone() as ConcurrentHashMap<String, BallModel>
            for(element in ballsClone){
                element.value.index++;
                if(element.value.index>=element.value.animate.size){
                    element.value.index=0;
                }
            }



            if (npc.id != null) {
                val ww = 160 / 1.3
                val hh = 120 / 1.3
                if (npc.images!![npc.id + "_stay_small"] == null) {
                    val bitmap = npc.getImage(
                        npc.images[npc.id + "_stay"],
                        (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                        (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                    )
                    npc.images[npc.id + "_stay_small"] = bitmap
                }
                if (npc.images[npc.id + "_stay_smallh"] == null) {
                    val bitmap = npc.getImage(
                        npc.images[npc.id + "_stay_h"],
                        (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                        (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                    )
                    npc.images[npc.id + "_stay_smallh"] = bitmap
                }
                if (npc.type == 51) {
                    if (npc.images[npc.id + "_run_small_web_small" + npc.index] == null && npc.images.get1(npc.id + "_run_length")!=null) {
                        for (i in 0 until npc.images.get1(npc.id + "_run_length")) {
                            val bitmap = npc.getImage(
                                npc.images[npc.id + "_run_small_web_" + i],
                                (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                                (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                            )
                            npc.images[npc.id + "_run_small_web_small" + i] = bitmap
                        }
                    }
                    if (npc.images[npc.id + "_run_small_web_smallh" + npc.index] == null && npc.images.get1(npc.id + "_run_length")!=null) {
                        for (i in 0 until npc.images.get1(npc.id + "_run_length")) {
                            val bitmap = npc.getImage(
                                npc.images[npc.id + "_run_small_web_" + i + "_h"],
                                (ww / MainActivity.connection.mainActivity.config.dt).toInt(),
                                (hh / MainActivity.connection.mainActivity.config.dt).toInt()
                            )
                            npc.images[npc.id + "_run_small_web_smallh" + i] = bitmap
                        }
                    }
                    npc.animate = if (npc.side == -1) {
                        npc.images[npc.id + "_run_small_web_small" + npc.index]
                    } else {
                        npc.images[npc.id + "_run_small_web_smallh" + npc.index]
                    }
                    // console.log(index)
                    // console.log(player.animate)
                    npc.index++
                    if (npc.images.get1(npc.id + "_run_length")!=null && npc.index >= npc.images.get1(npc.id + "_run_length")) npc.index = 0
                }
                if (npc.type == 50) {
                    npc.animate = if (npc.side == -1) {
                        npc.images[npc.id + "_stay_small"]
                    } else {
                        npc.images[npc.id + "_stay_smallh"]
                    }
                }
                for (i in 0..19) {
                    if (npc.type == i) {
                        if (npc.fire == false) {
                            npc.fire = true
                            npc.index = 0
                        }
                        npc.animate = npc.images[npc.id + "_" + npc.type + "_" + npc.index]
                        // console.log(index)
                        // console.log(player.animate)
                        npc.index++
                        if (npc.images.get1(npc.id + "_" + npc.type + "_length")!=null && npc.index >= npc.images.get1(npc.id + "_" + npc.type + "_length")) {
                            npc.index = 0
                            npc.type = 50
                            npc.fire = false
                        }
                    }
                }

                //  animate = npc.images.get(npc.id + "_stay");
            } else {
                npc.init()
            }
        
        }
    }

    override fun run() {
        while(play && thread.isInterrupted==false) {
            Thread.sleep(40)
            updateNPC()
        }
    }


}