package ru.AntonSibgatulin.bladeslayer.clientconnection;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import ru.AntonSibgatulin.bladeslayer.Config;
import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.Players.Player;
import ru.AntonSibgatulin.bladeslayer.Players.PlayerController;
import ru.AntonSibgatulin.bladeslayer.R;
import ru.AntonSibgatulin.bladeslayer.battle.MapController;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.BreathPreview;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.BreathPreviewController;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.PlayerPreview;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.PlayerPreviewController;
import ru.AntonSibgatulin.bladeslayer.battle.npc.BallModel;
import ru.AntonSibgatulin.bladeslayer.battle.npc.NPCDeamon;
import ru.AntonSibgatulin.bladeslayer.loader.JSONLoader;
import ru.AntonSibgatulin.bladeslayer.shop.ShopModelKt;


public class ClientWebSocket extends WebSocketListener implements Runnable {
    public String split = ";";
    public ClientHelper clientHelper = null;//new ClientHelper(this);
    public MainActivity mainActivity = null;
    public ShopModelKt shopModel = null;
    public String save_data = null;
    public BreathPreviewController breathPreviewController = null;
    public PlayerPreviewController playerPreviewController = null;
    public ArrayList<Player> playersList = new ArrayList<>();
    public HashMap<String, Player> playersMap = new HashMap<>();
    //public MapModelKt mapMain = null;
    //public MapModelKotlin mapMain = null;
    public MapController mapMain = null;
    public Thread mainThread = new Thread(this);
    public OkHttpClient okHttpClient = null;
    public WebSocket socket = null;///okHttpClient.newWebSocket(request,this);
    public Request request = null;
    public boolean isRun = false;
    public boolean start = false;
    public String url = null;
    public String site = null;
    public int version = 200;

    public long time = 0;
    public boolean ping_send = true;

    public ArrayList<String> commands = new ArrayList<>();
    public AppCompatActivity mainCompactActivity = null;
    public boolean isLoading = false;
    public int percent = 0;
    public Config config = null;
    public PlayerController player = null;
    public boolean isOpened=false;
    public ClientWebSocket() {
        mapMain = new MapController(null, false);
    }

    public ClientWebSocket(AppCompatActivity activity, String url, String site) {
        mainCompactActivity = activity;
        this.url = url;
        okHttpClient = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        socket = okHttpClient.newWebSocket(request, this);
        this.site = site;
        breathPreviewController = new BreathPreviewController();
        playerPreviewController = new PlayerPreviewController();
        mainThread.start();

    }

    public ClientWebSocket(AppCompatActivity activity, String url, String site, MainActivity mainActivity, PlayerController player, Config config) {
        mainCompactActivity = activity;
        this.config = config;
        this.mainActivity = mainActivity;
        this.player = player;
        this.url = url;
        okHttpClient = new OkHttpClient();

        request = new Request.Builder().url(url).build();

        socket = okHttpClient.newWebSocket(request, this);
        this.site = site;

        clientHelper = new ClientHelper(this);
        breathPreviewController = new BreathPreviewController();
        playerPreviewController = new PlayerPreviewController();
        mainThread.start();

    }

    /*

    public ClientWebSocket(AppCompatActivity activity){
        mainActivity = activity;
        okHttpClient = new OkHttpClient();
        request = new Request.Builder().url("ws://4.tcp.eu.ngrok.io:12920").build();
        socket = okHttpClient.newWebSocket(request,this);

    }

     */


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        isOpened=true;
        for (int i = 0; i < commands.size(); i++) {
            socket.send(commands.get(i));
        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, "Connection opened", Toast.LENGTH_LONG).show();
                socket.send("Connection opened");
            }
        });
        isRun = true;
        start = true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        new Thread(new Runnable() {
            @Override
            public void run() {

              /*  try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

               */
                //Log.e("server", text);
                String str[] = text.split(split);
                if (str[0].equals("auth")) {
                    if (str[1].equals("true")) {


                    } else {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mainActivity, "Неверный логин или пароль", Toast.LENGTH_LONG).show();

                            }
                        });
                    }


                }
                if (str[0].equals("version")) {
                    version = Integer.valueOf(str[1]);
                }
                if (str[0].equals("login")) {
                    if (str[1].equals("true")) {


                    } else {
                        if (mainActivity.loginFragment != null) {
                            mainActivity.loginFragment.addNotificationWindow(mainActivity.getResources().getString(R.string.INCORRECT_PASSWORD_OR_LOGIN));

                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.loginFragment.btnLogin.setText(mainActivity.getResources().getString(R.string.TEXT_LOGIN));

                                }
                            });
                              }else{
                            mainActivity.startLoginWithNotification(mainActivity.getResources().getString(R.string.INCORRECT_PASSWORD_OR_LOGIN));
                        }

                        //  MainActivity mainActivity = (MainActivity) this.mainActivity;
                        //  mainActivity.runAuth();
                    }
                }
                if (str[0].equals("battle")) {
                    if (str[1].equals("gameOver")) {
                        /* new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        })

                        */
                        mainActivity.lobbyFragment = null;
                        send("battle;exit");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    if (mainActivity.lobbyFragment != null && mainActivity.lobbyFragment.view != null) {
                                        mainActivity.lobbyFragment.addNotificationWindow("Game Over! :( ");
                                        break;
                                    }
                                }
                            }
                        }).start();

                    }
                    if (str[1].equals("end")) {
                        send("battle;exit");
                        mainActivity.startMenuEnd(str);
                        //mainActivity.lobbyFragment.addNotificationWindow("You winer! :) \n+" + str[2] + " " + mainActivity.getResources().getString(R.string.TEXT_SCORE) + "\n+" + str[3] + " " + mainActivity.getResources().getString(R.string.TEXT_MONEY));

                    }
                    if (str[1].equals("remove_user")) {
                        int id = Integer.valueOf(str[2]);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    if (mapMain != null && mapMain.getMapModelKotlin() != null) {
                                        mapMain.getMapModelKotlin().remove_user(id);
                                        break;

                                    }
                                }
                            }
                        }).start();
                    }
                    if (str[1].equals("fire")) {
//                        Log.e("fire","fire "+str[2]);
                        if (mapMain != null && mapMain.getMultiplayer()) {
                            if (Integer.valueOf(str[2]) == mainActivity.playerController.idPlayer) {
                                mainActivity.playerController.start_fire = true;
                                mainActivity.playerController.type = mainActivity.playerController.style + 1;

                                mainActivity.playerController.initAnimate();
                            } else {
                                for (int j = 0; j < mapMain.getAnotherPlayers().size(); j++) {

                                    if (mapMain.getAnotherPlayers().get(j).idPlayer == Integer.valueOf(str[2])) {
                                        mapMain.getAnotherPlayers().get(j).start_fire = true;
                                        mapMain.getAnotherPlayers().get(j).style = Integer.valueOf(str[3]);
                                        mapMain.getAnotherPlayers().get(j).type = Integer.valueOf(str[3]) + 1;
                                        mapMain.getAnotherPlayers().get(j).initAnimate();
                                        return;

                                    }
                                }
                            }
                        } else {
                            mainActivity.playerController.start_fire = true;
                            mainActivity.playerController.type = mainActivity.playerController.style + 1;

                            mainActivity.playerController.initAnimate();
                        }
                    }

                    if (str[1].equals("enter_to_battle")) {
                        mainActivity.playerController.idPlayer = Integer.valueOf(str[2]);
                    }
                    if (str[1].equals("add_player")) {
                        PlayerController enemy = new PlayerController();

                        enemy.playerPreviewController = playerPreviewController;
                        enemy.breathPreviewController = breathPreviewController;

                        enemy.imageLoader = player.imageLoader;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(str[4]);
                            enemy.id = str[3].split("_")[0];
                            enemy.id_type = Integer.valueOf(str[3].split("_")[1]);
                            enemy.login = json.getString("login");

                            float x = (float) json.getDouble("posX");
                            float y = (float) json.getDouble("posY");
                            double w = json.getDouble("w");
                            double h = json.getDouble("h");
                            enemy.w = w;
                            enemy.h = h;
                            int health = json.getInt("health");
                            double energy = json.getDouble("energy");
                            int side = json.getInt("side");
                            enemy.side = side;
                            enemy.idPlayer = Integer.valueOf(str[2]);
                            enemy.id_type = Integer.valueOf(str[3].split("_")[1]);

                            enemy.energy = energy;
                            enemy.health = health;
                            enemy.enemy = true;
                            enemy.setPosition(x, y);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while
                                    (true) {
                                        if (mapMain != null) {
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            mapMain.getAnotherPlayers().add(enemy);
                                            enemy.start();
                                            break;
                                        }
                                    }
                                }
                            }).start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    if (str[1].equals("set_energy_me")) {

                        mainActivity.playerController.energy = Double.valueOf(str[2]);

                    }
                    if (str[1].equals("set_health_me")) {
                        mainActivity.playerController.health = Integer.valueOf(str[2]);
                    }
                    if (str[1].equals("set_heart")) {
                        if (str.length >= 4) {
                            for (int i = 0; i < mapMain.getAnotherPlayers().size(); i++) {
                                PlayerController anotherPlayer = mapMain.getAnotherPlayers().get(i);
                                if (anotherPlayer.idPlayer == Integer.valueOf(str[3])) {
                                    anotherPlayer.heart = Integer.valueOf(str[2]);
                                }
                            }
                        } else {
                            player.heart = Integer.valueOf(str[2]);

                        }
                    }
                    if (str[1].equals("add_supplies")) {
                        if (str.length >= 4) {
                            if (mapMain != null) {
                                if (player.idPlayer == Integer.valueOf(str[3])) {
                                    player.inventory.put(str[2], str[2]);
                                } else {
                                    for (int i = 0; i < mapMain.getAnotherPlayers().size(); i++) {
                                        PlayerController anotherPlayer = mapMain.getAnotherPlayers().get(i);
                                        if (anotherPlayer.idPlayer == Integer.valueOf(str[3])) {
                                            anotherPlayer.inventory.put(str[2], str[2]);

                                        }
                                    }
                                }
                            }
                        } else {
                            player.inventory.put(str[2], str[2]);

                        }
                    }
                    if (str[1].equals("remove_supplies")) {
                        if (str.length >= 4) {
                            if (player.idPlayer == Integer.valueOf(str[3])) {
                                player.inventory.remove(str[2]);
                            } else {

                                for (int i = 0; i < mapMain.getAnotherPlayers().size(); i++) {
                                    PlayerController anotherPlayer = mapMain.getAnotherPlayers().get(i);
                                    if (anotherPlayer.idPlayer == Integer.valueOf(str[3])) {
                                        anotherPlayer.inventory.remove(str[2]);

                                    }
                                }
                            }
                        } else {
                            player.inventory.remove(str[2]);

                        }
                    }
                    if (str[1].equals("shot_me")) {

                        mainActivity.playerController.health = Integer.valueOf(str[2]);
                    }

                    if (str[1].equals("change_tile") && mapMain != null) {
                        int i = Integer.valueOf(str[2]), j = Integer.valueOf(str[3]);
                        int k = Integer.valueOf(str[4]);
                        mapMain.change_tile(i, j, k);

                    }
                    if (str[1].equals("setStyle")) {
                        int num = Integer.valueOf(str[2]);
                        mainActivity.playerController.style = num;
                    }
                    if (str[1].equals("pos_stay")) {
                        if (mapMain != null && mapMain.getMultiplayer()) {
                            int id = Integer.valueOf(str[2]);
                            if (id == mainActivity.playerController.idPlayer) {
                                mainActivity.playerController.stay();
                            } else {
                                for (int j = 0; j < mapMain.getAnotherPlayers().size(); j++) {
                                    if (mapMain.getAnotherPlayers().get(j).idPlayer == Integer.valueOf(str[2])) {
                                        mapMain.getAnotherPlayers().get(j).stay();
                                        return;

                                    }
                                }
                            }
                        } else {
                            mainActivity.playerController.stay();
                        }

                    }
                    if (str[1].equals("lines")) {
                        try {
                            JSONObject jsonObject = new JSONObject(text.replace("battle;lines;", ""));
                            NPCDeamon npcDeamon = checkNpc(jsonObject.getString("id"));
                            if (npcDeamon != null) {
                                npcDeamon.setLines(jsonObject.getJSONArray("lines"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    if (str[1].equals("npcs")) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(text.replace("battle;npcs;", ""));
                            JSONArray jsonArray = jsonObject1.getJSONArray("npcs");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                NPCDeamon npcDeamon = checkNpc(jsonObject.getString("id"));
                                if (npcDeamon == null) {
                                    npcDeamon = new NPCDeamon(jsonObject, player);

                                    npcDeamon.setPlayer(mainActivity.playerController);
                                    if (mapMain != null)
                                        mapMain.getNpcMap().put(jsonObject.getString("id"), npcDeamon);

                                }
                                npcDeamon.move(jsonObject);
                                if (jsonObject.has("balls") == false) continue;
                                JSONArray balls = jsonObject.getJSONArray("balls");
                                for (int j = 0; j < balls.length(); j++) {
                                    JSONObject jsonObject2 = balls.getJSONObject(j);
                                    BallModel ballModel = npcDeamon.getBalls().get(jsonObject2.getString("id"));
                                    if (ballModel == null) {
                                        npcDeamon.getBalls().put(jsonObject2.getString("id"), new BallModel(jsonObject2, mainActivity.imageLoader));

                                    } else {
                                        ballModel.update(jsonObject2);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (str[1].equals("balls")) {
                        String idNpc = str[2];
                        NPCDeamon npcDeamon = checkNpc(idNpc);
                        if (npcDeamon != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(text.replace("battle;balls;" + idNpc + ";", ""));
                                JSONArray balls = jsonObject.getJSONArray("balls");
                                for (int i = 0; i < balls.length(); i++) {
                                    JSONObject jsonObject1 = balls.getJSONObject(i);
                                    BallModel ballModel = npcDeamon.getBalls().get(jsonObject1.getString("id"));
                                    if (ballModel == null) {
                                        npcDeamon.getBalls().put(jsonObject1.getString("id"), new BallModel(jsonObject1, mainActivity.imageLoader));

                                    } else {
                                        ballModel.update(jsonObject1);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    if (str[1].equals("balls_remove")) {

                        String idNpc = str[2];

                        NPCDeamon npcDeamon = checkNpc(idNpc);
                        if (npcDeamon != null) {
                            npcDeamon.getBalls().remove(str[3]);
                        }

                    }


                    if (str[1].equals("npc")) {
                        try {
                            JSONObject jsonObject = new JSONObject(text.replace("battle;npc;", ""));
                            NPCDeamon npcDeamon = checkNpc(jsonObject.getString("id"));
                            if (npcDeamon == null) {
                                npcDeamon = new NPCDeamon(jsonObject, player);

                                npcDeamon.setPlayer(mainActivity.playerController);
                                if (mapMain != null)
                                    mapMain.getNpcMap().put(jsonObject.getString("id"), npcDeamon);

                            }
                            npcDeamon.move(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (str[1].equals("npc_remove")) {
                        mapMain.getNpcMap().remove(str[2]);
                    }
                    if (str[1].equals("my_fight_clean")) {
                        //   Log.e("clean","clean");
                        if (str.length >= 3) {

                            int id = Integer.valueOf(str[2]);
                            if (player.idPlayer == id) {
                                player.fights.setFights(new JSONArray());
                            } else {
                                //  Log.e("clean","clean2");
                                for (int i = 0; i < mapMain.getAnotherPlayers().size(); i++) {
                                    PlayerController enemy = mapMain.getAnotherPlayers().get(i);

                                    if (enemy.idPlayer == id) {

                                        enemy.fights.setFights(new JSONArray());
                                        break;
                                    }
                                }
                            }
                        } else {
                            player.fights.setFights(new JSONArray());
                        }
                    }
                    if (str.length >= 3 && str[2].equals("fight_box")) {
                        int id = Integer.valueOf(str[1]);
                        if (player != null && player.idPlayer == id) {
                            try {
                                JSONObject jsonObject = new JSONObject(text.replace("battle;" + id + ";fight_box;", ""));
                                player.fights.setFights(jsonObject.getJSONArray("fights"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            for (int i = 0; i < mapMain.getAnotherPlayers().size(); i++) {
                                PlayerController enemy = mapMain.getAnotherPlayers().get(i);
                                if (enemy.idPlayer == id) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(text.replace("battle;" + id + ";fight_box;", ""));
                                        enemy.fights.setFights(jsonObject.getJSONArray("fights"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    if (str[1].equals("fight_box")) {
                        try {
                            JSONObject jsonObject = new JSONObject(text.replace("battle;fight_box;", ""));
                            player.fights.setFights(jsonObject.getJSONArray("fights"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (str[1].equals("pos")) {
                        // if(mapMain!=null && mapMain.getPlayer()!=player)
                        //    mapMain.setPlayer(player);

                        if (mapMain != null && mapMain.getMultiplayer()) {
                            if (Integer.valueOf(str[2]) == player.idPlayer) {
                                player.setPosition(Float.valueOf(str[3]) / (float) config.START_SIZE * (float) config.SIZE, Float.valueOf(str[4]) / (float) config.START_SIZE * (float) config.SIZE);
                                player.side = Integer.valueOf(str[5]);


                            } else {

                                for (int j = 0; j < mapMain.getAnotherPlayers().size(); j++) {
                                    if (mapMain.getAnotherPlayers().get(j).idPlayer == Integer.valueOf(str[2])) {
                                        mapMain.getAnotherPlayers().get(j).setPosition(Float.valueOf(str[3]) / config.START_SIZE * config.SIZE, Float.valueOf(str[4]) / config.START_SIZE * config.SIZE);
                                        mapMain.getAnotherPlayers().get(j).side = Integer.valueOf(str[5]);
                                        return;

                                    }
                                }

                            }

                        } else {
                            player.setPosition(Float.valueOf(str[2]) / (float) (config.START_SIZE) * (float) (config.SIZE), Float.valueOf(str[3]) / (float) (config.START_SIZE) * (float) (config.SIZE));
                            player.side = (Double.valueOf(str[4])).intValue();
                            // Log.e("pos",player.position.x+" "+player.position.y);
                        }
                        // mapMain.player = player;
                    }


                    if (str[1].equals("pos_me")) {


                        player.setPosition(Integer.valueOf(str[2]) * config.SIZE, Integer.valueOf(str[3]) * config.SIZE);
                        //if(mapMain!=null && mapMain.getPlayer()!=player)
                        //mapMain.setPlayer(player);
                    }

                    if (str[1].equals("exit")) {
                        //                        mapMain.getMapModelKotlin().stop();
                        //  player.stop();
                        if (mapMain != null) {
                            for (int i = 0; i < mapMain.getAnotherPlayers().size(); i++) {
                                mapMain.getAnotherPlayers().get(i).action.interrupt();

                            }
                            mapMain.setPlay(false);
                            try {
                                Thread.sleep(1000);
                                if (mapMain != null && mapMain.getThread() != null && mapMain.getThread().isInterrupted() == false) {
                                    mapMain.getThread().interrupt();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //mapMain.getThread().interrupt();

                          /*  for (Map.Entry<String, NPCDeamon> element : mapMain.getNpcMap().entrySet()) {
                                String key = element.getKey();
                                NPCDeamon npcDeamon = element.getValue();
                                npcDeamon.getAction().interrupt();
                            }

                           */
                        }
                        mapMain = null;
                        mainActivity.startMenu();
                    }
                    if (str[1].equals("map_add")) {
                        try {
                            JSONArray arr = new JSONObject(str[2]).getJSONArray("map");
                            ArrayList<ArrayList<Integer>> arrayList = new ArrayList<>();

                            for (int i = 0; i < arr.length(); i++) {
                                ArrayList<Integer> pod = new ArrayList<>();
                                for (int j = 0; j < arr.getJSONArray(0).length(); j++) {
                                    pod.add(arr.getJSONArray(i).getInt(j));
                                }
                                arrayList.add(pod);
                            }
                            if (mapMain == null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (true) {
                                            if (mapMain != null) {
                                                mapMain.add(arrayList);
                                                break;
                                            }
                                            try {
                                                Thread.sleep(10);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();

                                            }
                                        }
                                    }
                                }).start();
                            } else
                                mapMain.add(arrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    if (str[1].equals("map_init")) {
                        try {
                            JSONArray arr = new JSONObject(str[2]).getJSONArray("map_start");
                            ArrayList<ArrayList<Integer>> arrayList = new ArrayList<>();

                            for (int i = 0; i < arr.length(); i++) {
                                ArrayList<Integer> pod = new ArrayList<>();
                                for (int j = 0; j < arr.getJSONArray(0).length(); j++) {
                                    pod.add(arr.getJSONArray(i).getInt(j));
                                }
                                arrayList.add(pod);
                            }

                            boolean multiplayer = false;
                            JSONObject jsonObject = new JSONObject(str[2]);

                            if (jsonObject.has("multiplayer"))
                                multiplayer = jsonObject.getBoolean("multiplayer");

                            mapMain = new MapController(arrayList, multiplayer);
                            player.start();

                            mainActivity.playInit(arrayList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
                if (str[0].equals("lobby")) {
                    if (str[1].equals("train_get")) {
                        int type = Integer.valueOf(str[2]);
                        String description = text.replace("lobby;train_get;" + type + ";", "");
                        Bitmap map = (Bitmap) mainActivity.imageLoader.images.get("train_" + type + "v" + version);
                        mainActivity.runPlayWindow(type, description, map, "train");
                    }
                    if (str[1].equals("upgrade")) {
                        try {
                            player.json = new JSONObject(text.replace("lobby;upgrade;", ""));
                            player.updateId();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mainActivity.startMenuWithNotification(mainActivity.getResources().getString(R.string.PURCHASE_SUCC));
                       /* if (mainActivity.lobbyFragment != null) {
                            mainActivity.lobbyFragment.addNotificationWindow(mainActivity.getResources().getString(R.string.PURCHASE_SUCC));
                        }

                        */
                    }
                    if (str[1].equals("ping")) {
                        ping_send = false;

                        //Log.e("Ping",""+(System.currentTimeMillis()-time));
                        if (mapMain != null && mapMain.getMapModelKotlin() != null && mapMain.getMapModelKotlin().getPingView() != null)
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mapMain != null && mapMain.getMapModelKotlin() != null && mapMain.getMapModelKotlin().getPingView() != null)

                                        mapMain.getMapModelKotlin().getPingView().setText("Ping: " + (System.currentTimeMillis() - time));

                                }
                            });
                     /*   try {
                            Thread.sleep(500);
                            send("lobby;ping");
                            time=System.currentTimeMillis();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                      */


                    }
                    if (str[1].equals("change_count_of_people")) {
                        if (mainActivity.locationFragment != null) {
                            mainActivity.locationFragment.changeCountOfPeople(str[2], Integer.valueOf(str[3]));
                        }
                    }
                    if (str[1].equals("addBattle")) {
                        if (mainActivity.locationFragment != null) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(text.replace("lobby;addBattle;", ""));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mainActivity.locationFragment.add(jsonObject);
                        }
                    }
                    if (str[1].equals("maps")) {
                        try {
                            JSONObject jsonObject = new JSONObject(text.replace("lobby;maps;", ""));
                            mainActivity.runLocationFragment(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (str[1].equals("set_player")) {
                        try {
                            JSONObject jsonObject = new JSONObject(text.replace("lobby;set_player;", ""));
                            boolean send = false;
                            if (player.json.getJSONArray("players").length() < jsonObject.getJSONArray("players").length()) {
                                send = true;

                            }
                            player.json = jsonObject;

                            player.updateId();
                            String name = playerPreviewController.get(player.getPlayerIs().getString("id")).name;

                            mainActivity.startMenuWithNotification(mainActivity.getResources().getString(R.string.PURCHASE_SUCC) + " - " + name);
                           //  mainActivity.lobbyFragment.addNotificationWindow(mainActivity.getResources().getString(R.string.PURCHASE_SUCC) + " - " + name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (str[1].equals("train_get")) {
                        int type = Integer.valueOf(str[2]);
                        String description = text.replace("lobby;train_get;" + type + ";", "");
                        Bitmap map = (Bitmap) mainActivity.imageLoader.images.get("train_" + type + "v" + version);
                        mainActivity.runTrainWindow(type, description, map);

                    }
                    if (str[1].equals("player_size")) {
                        player.w = Double.valueOf(str[2]);
                        player.h = Double.valueOf(str[3]);
                    }
                    if (str[1].equals("main_get")) {
                        int type = Integer.valueOf(str[2]);
                        String description = text.replace("lobby;main_get;" + type + ";", "");
                        Bitmap map = (Bitmap) mainActivity.imageLoader.images.get("train_" + type + "v" + version);
                        mainActivity.runPlayWindow(type, description, map, "play");
                    }
                    if (str[1].equals("shop_init")) {
                        try {

                            shopModel = new ShopModelKt(new JSONObject(text.replace("lobby;shop_init;", "")), mainActivity.playerController);
                            shopModel.setLoading(true);

                            mainActivity.runShop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (str[1].equals("login")) {
                        isLoading = true;
                        mainActivity.runLoadingFragment();

                        mainActivity.saveData(save_data);
                        mainActivity.playerController = new PlayerController();
                        player = mainActivity.playerController;

                        MainActivity.playerControllerStatic = mainActivity.playerController;
                        try {
                            JSONObject jsonObject = new JSONObject(text.replace("lobby;login;", ""));
                            mainActivity.playerController.json = jsonObject;
                            mainActivity.playerController.login = jsonObject.getString("login");

                            mainActivity.playerController.breathPreviewController = breathPreviewController;
                            mainActivity.playerController.playerPreviewController = playerPreviewController;
                            mainActivity.playerController.imageLoader = mainActivity.imageLoader;
                            mainActivity.playerController.init();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        clientHelper.loadJSONSprite();
                        JSONObject jsonObject = JSONLoader.jsonObjectLoadeFromURLStatic(site + "res/index.json");// mainActivity.loaderFromAssets.getStringFromAssetFile(mainActivity, "res/index.json");


                        // JSONObject jsonObject = null;
                        try {

                            //jsonObject = new JSONObject(string);
                            long timemain = System.currentTimeMillis();
                            Iterator<String> iterator = jsonObject.keys();
                            Iterator<String> iterator1 = jsonObject.keys();
                            int all = 1;

                            while (iterator1.hasNext()) {
                                iterator1.next();
                                all++;
                            }


                            int count = 0;
                             try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            while (iterator.hasNext()) {
                                count++;

                                String key = iterator.next();
                                if (jsonObject.get(key) instanceof String) {
                                    String url = jsonObject.getString(key);
                                    if (url.startsWith("/") && url.length() > 1) {
                                        url.replace("/", "");
                                    }
                                    long time = System.currentTimeMillis();
                                    mainActivity.imageLoader.loadFromUrlImage(site + "" + url, key, version, mainActivity);
//                                    Log.e("data",(System.currentTimeMillis()-time)+" "+site+""+url+" "+bitmap.getWidth());

                                }
                                percent = count * 100 / all;

                            }
                            for (int i = 0; i < playerPreviewController.array.size(); i++) {
                                PlayerPreview playerPreview = playerPreviewController.array.get(i);
                                playerPreview.image_sources = (Bitmap) mainActivity.imageLoader.images.get(playerPreview.id + "_prevv" + version);
                            }
                            isLoading = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        mainActivity.startMenu();
                    }
                    if (str[1].equals("breath_setting")) {
                        try {
                            JSONObject mainJSON = new JSONObject(text.replace("lobby;breath_setting;", ""));
                            BreathPreview breathPreview = new BreathPreview(mainJSON);
                            breathPreviewController.add(breathPreview);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

               /* try {
                    JSONObject mainJSON = new JSONObject(text.replace("lobby;breath_setting;",""));
                    JSONArray json = mainJSON.getJSONArray("styles");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = json.getJSONObject(i);
                        String description = jsonObject.getString("des");
                        boolean has = false;
                        if (jsonObject.has("none") && jsonObject.getBoolean("none")) {
                            // continue;
                            has = jsonObject.getBoolean("none");
                        }
                        String name = jsonObject.getString("name");
                        int maxTime = jsonObject.getInt("maxTime");
                        int minTime = jsonObject.getInt("minTime");
                        int power = jsonObject.getInt("power");
                        int target = jsonObject.getInt("target");
                        double energy = jsonObject.getDouble("energy");
                        int length = 0;
                        boolean contr = false;
                        if (jsonObject.has("length")) {
                            length = jsonObject.getInt("length");

                        }
                        if (jsonObject.has("contr")) {
                            contr = jsonObject.getBoolean("contr");

                        }
                       // System.out.println(style.toString() + " " + description + " is loading");
                      //  StyleModel styleModel = new StyleModel(breath, length, contr, name, maxTime, minTime, power, energy, target,
                        //        breath.toString(), i, jsonObject, has);
                        //styles.add(styleModel);
                      //  hashMap.put(i, styleModel);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                */
                    }
                    if (str[1].equals("change_data")) {
                        String name = str[2];
                        JSONObject jsonObject = null;
                        try {

                            jsonObject = new JSONObject(text.replace("lobby;change_data;" + name + ";", ""));
                            PlayerPreview playerPreview = new PlayerPreview(jsonObject, null);
                            playerPreviewController.add(playerPreview);
                            loadJsonFromPlayer(playerPreview);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (str[1].equals("players_setting")) {

                        try {
                            //Log.e("player is null?",MainActivity.playerControllerStatic.toString());
                            JSONObject jsonObject = new JSONObject(text.replace("lobby;players_setting;", ""));
                            PlayerPreview playerPreview = new PlayerPreview(jsonObject, null);
                            playerPreviewController.add(playerPreview);
                            loadJsonFromPlayer(playerPreview);
                   /* String name = jsonObject.getString("id");

                    JSONArray styles = jsonObject.getJSONArray("styles");
                    ArrayList<EBreath> styles2 = new ArrayList<>();

                    for(int j=0;j<styles.length();j++){
                        String stri = styles.getString(j);

                        if(stri.equals(EBreath.FLAME.toString())||stri.equals(EBreath.INSECT.toString())||
                                stri.equals(EBreath.SOUND.toString())||stri.equals(EBreath.SUN.toString())||
                                stri.equals(EBreath.THUNDER.toString())|| stri.equals(EBreath.WATER.toString())){
                            styles2.add(EBreath.valueOf(stri));

                        }

                    }
                    JSONArray levels = jsonObject.getJSONArray("levels");
                    for(int j =0;j<levels.length();j++){
                        JSONObject jsonObject2 = levels.getJSONObject(j);

                        int price = jsonObject2.getInt("price");
                        double speed = jsonObject2.getDouble("speed");
                        double speed_shot = jsonObject2.getDouble("speed_shot");
                        double go = jsonObject2.getDouble("go");
                        long health = jsonObject2.getLong("health");
                        double jump = jsonObject2.getDouble("jump");
                        int run = jsonObject2.getInt("run");
                        double breath = jsonObject2.getDouble("breath");
                        Player player = new Player(styles2, speed, jump, go, speed_shot, breath, run, health, j, name,jsonObject);
                        this.playersMap.put("id_"+j,player);
                        this.playersList.add(player);


                    }






                    */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }
                if (str[0].equals("game")) {
                    if (str[1].equals("captcha")) {

                        String img = text.replace("game;captcha;data:image/png;base64,", "");

                        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        if (mainActivity.captchaFragment == null) {
                            mainActivity.initCaptchaModel(decodedBitmap);
                        } else {
                            mainActivity.captchaFragment.changeBitmap(decodedBitmap);
                        }
                    }
                    if (str[1].equals("noncaptcha")) {
                        if (mainActivity.captchaFragment != null) {
                            mainActivity.captchaFragment.addNotificationWindow(mainActivity.getResources().getString(R.string.CAPTCHA_NOT_CORRECT));

                        }
                    }
                    if (str[1].equals("captcha_fine")) {
                        if (mainActivity.captchaFragment != null) {
                            mainActivity.captchaFragment = null;
                            mainActivity.startLogin();
                        }
                    }
                }
                if (str[0].equals("reg")) {
                    if (str[1].equals("exist")) {
                        mainActivity.regFragment.addNotificationWindow(mainActivity.getResources().getString(R.string.USER_ALREADY_EXIST));
                    }
                    if (str[1].equals("password_unvalidate") || str[1].equals("login_unvalidate")) {
                        if (mainActivity.regFragment != null) {
                            mainActivity.regFragment.addNotificationWindow(mainActivity.getResources().getString(R.string.INCORRECT_PASSWORD_OR_LOGIN));
                        }
                    }
                    if (str[1].equals("in")) {
                        send("login;" + save_data);
                    }
                }


            }
        }).start();

        /*
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, "message "+text, Toast.LENGTH_LONG).show();
            }
        });

         */

    }

    private NPCDeamon checkNpc(String id) {
        if (mapMain == null) return null;
        return mapMain.getNpcMap().get(id);
    }

    void displayFiles(AssetManager mgr, String path) {
        try {
            String list[] = mgr.list(path);
            if (list != null)
                for (int i = 0; i < list.length; ++i) {
                    Log.v("Assets:", path + "/" + list[i]);
                    displayFiles(mgr, path + "/" + list[i]);
                }
        } catch (IOException e) {
            Log.v("List error:", "can't list" + path);
        }
    }


    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        isRun = false;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, "Connection closed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        isRun = false;
        mainActivity.blockApplication();
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, "Connection fail ", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    public void loadJsonFromPlayer(PlayerPreview playerPreview) {
        clientHelper.loadJsonFromPlayer(playerPreview);
    }

    public void send(String s) {
        if (isRun)
            socket.send(s);
    }


    @Override
    public void run() {
        while (false== true&& true && ping_send ) {
            time = System.currentTimeMillis();
          //  send("lobby;ping");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*
            if (!isRun && start) {
                okHttpClient = new OkHttpClient();
                request = new Request.Builder().url(url).build();
                socket = okHttpClient.newWebSocket(request, this);
                start = false;
            }
            */
        }
    }

    public void initJSONSprite() {
        JSONObject json = JSONLoader.jsonObjectLoadeFromURLStatic(site + "res/animate.json");// mainActivity.loaderFromAssets.getStringFromAssetFile(mainActivity, "res/index.json");

    }


    public void stop() {
        mainThread.stop();
        mainThread.interrupt();
        socket.close(0, "allright");

    }

/*
  final AssetManager mgr = mainActivity.getAssets();
                displayFiles(mgr, "");

                displayFiles(mgr, "/webkit");

                displayFiles(mgr, "/images");
 */


}
