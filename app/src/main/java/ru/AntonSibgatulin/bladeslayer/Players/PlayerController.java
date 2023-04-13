package ru.AntonSibgatulin.bladeslayer.Players;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.battle.Animate;
import ru.AntonSibgatulin.bladeslayer.battle.Fights;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.BreathPreviewController;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.PlayerPreviewController;
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader;
import ru.AntonSibgatulin.bladeslayer.swing.Timer;

public class PlayerController {

    public Thread action = null;
    public ImageLoader imageLoader = null;
    public Canvas canvas = null;
    public PlayerPreviewController playerPreviewController = null;
    public BreathPreviewController breathPreviewController = null;
    public boolean enemy = false;
    public boolean fire = false;
    public boolean start_fire = false;
    public Vector position = new Vector();
    public Bitmap img = null;
    public Timer animation = null;
    public Bitmap animate = null;
    public Bitmap animate_shot = null;
    public int animation_top = 32;// MainActivity.config.START_SIZE;
    public String id = null;
    public int idPlayer = 0;
    public int style = 0;
    public JSONObject json = null;
    public String login = null;

    public ArrayList<Integer> key = new ArrayList<>();
    public int downKeyInt = 0;

    public void downKey(int key){
        if(key<0 || key >=this.key.size()){
            return;
        }
        setStyles(this.key.get(key)-1);
        this.downKeyInt = key;
        if(MainActivity.connection.mapMain.getMapModelFragment()!=null){
            MainActivity.connection.mapMain.getMapModelFragment().updateStyle(this.key.get(key));
        }
    }
    public void downKeyRight(){
        this.downKeyInt++;
        if(this.downKeyInt>=this.key.size()){
            this.downKeyInt=0;
        }
        this.downKey(this.downKeyInt);
    }
    public void downKeyLeft(){
        this.downKeyInt--;
        if(this.downKeyInt<0){
            this.downKeyInt=this.key.size()-1;
        }
        this.downKey(this.downKeyInt);
    }



    public int version = 0;
    public Animate animateAnimate = null;
    public HashMap<String,String> inventory = new HashMap<>();
    public int heart = 3;
    public int type = 50;
    public double w = 0;
    public double h = 0;
    public double energy = 0;
    public int id_type = 0;
    public double index = 0;
    public int health = 0;
    public int side = 0;
    public Fights fights = new Fights(this);

    /*
    public Texture animateTexture = null;


     */

    public void initAnimate() {
        ImageLoader images = imageLoader;

        String breath = null;
        if(this.enemy==false) {
            breath = getBreathofPlayer();
        }else{
            try {
               breath = playerPreviewController.hash.get(this.id).json.getJSONArray("styles").getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // console.log(breath)
        // var breath = playerPreviewController.hash.get(this.id).json.styles[0]
        int timer_period = 40;
        JSONObject jsonBreath = breathPreviewController.hash.get(breath).jsonObject;
        JSONObject style = null;
        try {
            style = jsonBreath.getJSONArray("styles").getJSONObject(this.style);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Integer type_player = this.id_type;
        if (type_player == null) {
            JSONObject jsonPlayerFromBase = getPlayerIs();
            try {
                type_player = jsonPlayerFromBase.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (type_player == 0) type_player = 1;
        double time = 0;
        try {
            time = style.getInt("minTime") + ((style.getInt("maxTime") - style.getInt("minTime")) / type_player);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int delta_ = 1;

        double dt_index = (time / timer_period);


        Log.e("info", "" + breath + " " + this.style + " " + dt_index + " " + time);

        PlayerController player = this;
        if (breath.equals("THUNDER")) {
            if (player.style == 0) {
                Animate animation1 = images.getS("animate_thunderball_0");
                Animate animation = (Animate) animation1.clone();

                animation.init(4);
                animation.setType_moving(4);

                animation.side = this.side;
                animation.setDegress(0);
                animation.setSpeed(15);

                this.animateAnimate = animation;
            }
            if (player.style == 1) {
                Animate animation1 = images.getS("animate_thunderball_0");
                Animate animation = (Animate) animation1.clone();

                animation.init(4);
                animation.setType_moving(4);

                animation.side = this.side;
                animation.setDegress(0);
                animation.setSpeed(15);

                this.animateAnimate = animation;
            }
            if (player.style == 2) {
                Animate animation1 = images.getS("animate_thunderball_0");
                Animate animation = (Animate) animation1.clone();
                animation.init(4);
                animation.setType_moving(4);

                animation.side = this.side;
                animation.setDegress(0);
                animation.setSpeed(15);

                this.animateAnimate = animation;
            }

            if (player.style == 3) {
                Animate animation1 = images.getS("animate_thunderball_0");
                Animate animation = (Animate) animation1.clone();
                animation.init(4);
                animation.setType_moving(4);

                animation.side = this.side;
                animation.setDegress(0);
                animation.setSpeed(15);

                this.animateAnimate = animation;
            }
        }
        if (breath.equals("INSECT")) {
            if (player.style == 1) {
                Animate animation1 = images.getS("animate_insectball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(1);
                animation.setSpeed(3);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(2 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 2) {
                Animate animation1 = images.getS("animate_insectball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(0);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1 / dt_index);
                this.animateAnimate = animation;
            }

            if (player.style == 3) {
                Animate animation1 = images.getS("animate_insectball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(2);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1 / dt_index);
                this.animateAnimate = animation;
            }
        }
        if (breath.equals("BEAST")) {
            if (player.style == 1) {
                Animate animation1 = images.getS("animate_beastball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(0);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 4) {
                Animate animation1 = images.getS("animate_beastball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(1);
                animation.setSpeed(3);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 8) {
                Animate animation1 = images.getS("animate_beastball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(2);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1 / dt_index);
                this.animateAnimate = animation;
            }
        }
        if (breath.equals("WATER")) {
            if (player.style == 1) {
                Animate animation1 = images.getS("animate_waterball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(1);
                animation.setSpeed(3);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(3 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 3) {
                Animate animation1 = images.getS("animate_waterball_0");
               // if(animation1==null)return;
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(0);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1.5 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 6) {
                    /* Animate animation1 = images.getS("animate_waterball_0");
                     Animate animation = (Animate) animation1.clone();
                     animation.type_moving = 3
                     animation.speed = 3
                     animation.dk = 0.05
                     animation.degress = -90
                     animation.side = this.side
                     animation.radius = 10
                     this.animateAnimate = animation
                     */
            }
            if (player.style == 8) {
                Animate animation1 = images.getS("animate_waterball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(0);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 9) {
                Animate animation1 = images.getS("animate_waterball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(2);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1.2 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 7) {
                Animate animation1 = images.getS("animate_waterball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(2);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(1 / dt_index);
                this.animateAnimate = animation;
            }
        }


        if (breath.equals("FLAME")) {
            if (player.style == 1) {
                Animate animation1 = images.getS("animate_fireball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(1);
                animation.setSpeed(3);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.setRadius(56);
                animation.side = this.side;
                animation.setDk(3 / dt_index);
                this.animateAnimate = animation;


            }

            if (player.style == 2) {
                Animate animation1 = images.getS("animate_fireball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(0);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(2 / dt_index);
                this.animateAnimate = animation;
            }
            if (player.style == 4) {
                Animate animation1 = images.getS("animate_fireball_0");
                Animate animation = (Animate) animation1.clone();
                animation.setType_moving(2);
                animation.setSpeed(5);
                animation.setDk(0.05);
                animation.setDegress(-90);
                animation.side = this.side;
                animation.setRadius(56);
                animation.setDk(2 / dt_index);
                this.animateAnimate = animation;
            }

        }
    }

    @Nullable
    private String getBreathofPlayer() {
        JSONObject jsonObject = getPlayerIs();
        try {
            return jsonObject.getJSONArray("breath").getJSONObject(0).getString("id");
        } catch (JSONException e) {
            return null;
        }
    }


    public void stay() {
        if (this.fire == false)
            this.type = 50;
    }

    public void setStyles(Integer style){
        MainActivity.connection.send("battle;change;"+style);
    }
    public void setStyle(int style) {

        this.style = style;

    }

    public void init() {
        JSONObject jsonPlayer = this.json;
        if (jsonPlayer == null) return;
        JSONObject player = null;
        try {
            player = getPlayerIs();
            this.id = player.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void setPosition(float x, float y) {
        if (this.position.x > x) {
            if (this.fire == false) {
                this.type = 51;
            }

            //this.side = -1;
        }
        if (this.position.x < x) {
            if (this.fire == false) {
                this.type = 51;
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
                this.type = 50;
            }
        }


        this.position.x = x;
        this.position.y = y;
    }

    //loader image
    public JSONObject getPlayerIndex(int i) {
        try {
            JSONArray players = json.getJSONArray("players");
            if (i >= players.length()) {
                i = 0;
            }
            if (i < 0) i = players.length() - 1;
            JSONObject jsonObject = players.getJSONObject(i);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getPlayerIs() {
        try {
            for (int i = 0; i < json.getJSONArray("players").length(); i++) {
                JSONObject jsonObject = json.getJSONArray("players").getJSONObject(i);
                if (jsonObject.getBoolean("is")) {
                    return jsonObject;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public int getPlayerIsIndex() {
        try {
            for (int i = 0; i < json.getJSONArray("players").length(); i++) {
                JSONObject jsonObject = json.getJSONArray("players").getJSONObject(i);
                if (jsonObject.getBoolean("is")) {
                    return i;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    boolean start = false;

    public void start() {
        if (start) return;
        start = true;
        action = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!action.isInterrupted()) {
                    try {
                        Thread.sleep(40);
                        ImageLoader images = imageLoader;
                        if (animateAnimate != null && canvas!=null) {
                            animateAnimate.update(PlayerController.this,canvas);
                        }

                        if (id != null) {
                            double ww = 160 / 1.3;
                            double hh = 120 / 1.3;

                            if (images.get(id + "_stay_small") == null) {
                                Bitmap bitmap = getImage(images.get(id + "_stay"), (int) (ww / MainActivity.connection.mainActivity.config.dt), (int) (hh / MainActivity.connection.mainActivity.config.dt));
                                images.set(id + "_stay_small", bitmap);

                            }
                            if (images.get(id + "_stay_smallh") == null) {
                                Bitmap bitmap = getImage(images.get(id + "_stay_h"), (int) (ww / MainActivity.connection.mainActivity.config.dt), (int) (hh / MainActivity.connection.mainActivity.config.dt));
                                images.set(id + "_stay_smallh", bitmap);

                            }

                            if (fire == false && start_fire == false) {
                                if (type == 51) {
                                    if (images.get(id + "_run_small_web_small" + (int) (index)) == null && images.get1(id + "_run_length")!=null) {
                                        for (int i = 0; i < images.get1(id + "_run_length"); i++) {
                                            Bitmap bitmap = getImage(images.get(id + "_run_small_web_" + i), (int) (ww / MainActivity.connection.mainActivity.config.dt), (int) (hh / MainActivity.connection.mainActivity.config.dt));
                                            images.set(id + "_run_small_web_small" + i, bitmap);
                                        }
                                    }
                                    if (images.get(id + "_run_small_web_smallh" + (int) (index)) == null && images.get1(id + "_run_length")!=null) {
                                        for (int i = 0; i < images.get1(id + "_run_length"); i++) {
                                            Bitmap bitmap = getImage(images.get(id + "_run_small_web_" + i + "_h"), (int) (ww / MainActivity.connection.mainActivity.config.dt), (int) (hh / MainActivity.connection.mainActivity.config.dt));
                                            images.set(id + "_run_small_web_smallh" + i, bitmap);
                                        }
                                    }
                                    if (side == -1) {
                                        //  animateTexture = images.getTexture(id + "_run_small_web_small" + index);
                                        animate = images.get(id + "_run_small_web_small" + (int) (index));
                                    } else {

                                        animate = images.get(id + "_run_small_web_smallh" + (int) (index));
                                        // animateTexture = images.getTexture(id + "_run_small_web_smallh" + index);
                                    }
                                    // console.log(index)
                                    // console.log(player.animate)
                                    index++;
                                    if (images.get1(id + "_run_length")!=null && (int) (index) >= images.get1(id + "_run_length")) index = 0;
                                }

                                if (type == 50) {

                                    if (side == -1) {

                                        animate = images.get(id + "_stay_small");
                                        //  animateTexture = images.getTexture(id + "_stay_small");
                                        //Log.e("size",""+images.images.size());
                                        //Log.e("Data",animateTexture.toString());
                                    } else {
                                        animate = images.get(id + "_stay_smallh");
                                        //  animateTexture = images.getTexture(id + "_stay_smallh");
                                        //Log.e("Data",animateTexture.toString());
                                    }

                                }
                            } else {
                                type = style + 1;
                            }


                            for (int i = 0; i < 20; i++) {
                                if (type == i) {
                                    if (start_fire == true && fire == false) {
                                        fire = true;
                                        index = 0;
                                    }


                                    /*
                                    if (fire == false) {

                                        fire = true;
                                        index = 0;
                                    }

                                     */

                                    if (images.get(id + "_" + type + "_" + (int) (index) + "_small") == null) {
                                        Bitmap bitmap = getImage(images.get(id + "_" + type + "_" + (int) (index)), (int) (ww / MainActivity.connection.mainActivity.config.dt), (int) (hh / MainActivity.connection.mainActivity.config.dt));
                                        images.set(id + "_" + type + "_" + (int) (index) + "_small", bitmap);

                                    }

                                    if (images.get(id + "_" + type + "_" + (int) (index) + "_smallh") == null) {
                                        Bitmap bitmap = getImage(images.get(id + "_" + type + "_" + (int) (index) + "_h"), (int) (ww / MainActivity.connection.mainActivity.config.dt), (int) (hh / MainActivity.connection.mainActivity.config.dt));
                                        images.set(id + "_" + type + "_" + (int) (index) + "_smallh", bitmap);

                                    }
                                    if (side == -1) {
                                        animate = images.get(id + "_" + type + "_" + (int) (index) + "_small");
                                    } else {
                                        animate = images.get(id + "_" + type + "_" + (int) (index) + "_smallh");

                                    }
                                    // animateTexture = images.getTexture(id + "_" + type + "_" + index);
                                    // console.log(index)
                                    // console.log(player.animate)
                                    String breath = null;

                                    if(enemy==false) {
                                        breath = getBreathofPlayer();
                                    }else{
                                        try {
                                            breath = playerPreviewController.hash.get(id).json.getJSONArray("styles").getString(0);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    // console.log(breath)
                                    // var breath = playerPreviewController.hash.get(this.id).json.styles[0]
                                    int timer_period = 40;
                                    JSONObject jsonBreath = breathPreviewController.hash.get(breath).jsonObject;
                                    JSONObject style = null;
                                    try {
                                        style = jsonBreath.getJSONArray("styles").getJSONObject(PlayerController.this.style);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    Integer type_player = PlayerController.this.id_type;
                                    if (type_player == null) {
                                        JSONObject jsonPlayerFromBase = getPlayerIs();
                                        try {
                                            type_player = jsonPlayerFromBase.getInt("type");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (type_player == 0) type_player = 1;
                                    double time = 0;
                                    try {
                                        time = style.getInt("minTime") + ((style.getInt("maxTime") - style.getInt("minTime")) / type_player);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (images.get1(id + "_" + type + "_length") != null) {

                                        double dt_index = (timer_period / (time / images.get1(id + "_" + type + "_length")));

                                        index += dt_index;
                                        if (index >= images.get1(id + "_" + type + "_length")) {
                                            index = 0;
                                            type = 50;
                                            fire = false;
                                            start_fire = false;
                                        }

                                    }else{
                                        index++;
                                    }



                                }
                            }

                            //  animate = images.get(id + "_stay");
                        } else {

                            init();
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        action.start();
    }


    public Bitmap getImage(Bitmap bitmap, int nx, int ny) {
        if (bitmap == null) return null;
        return Bitmap.createScaledBitmap(bitmap, nx, ny, false);
    }

    public PlayerController() {


    }

    public void updateDataIs(int i, boolean is) {
        try {
            JSONArray players = json.getJSONArray("players");
            if (i >= players.length()) {
                i = 0;
            }
            if (i < 0) i = players.length() - 1;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.getJSONArray("players").getJSONObject(i).put("is", is);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateId() {
        try {
            id = getPlayerIs().getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        // action.stop();
        action.interrupt();

    }
}
