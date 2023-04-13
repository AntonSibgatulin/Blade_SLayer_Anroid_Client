package ru.AntonSibgatulin.bladeslayer;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.Players.PlayerController;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.PlayerPreviewController;


public class LobbyFragment extends Fragment {
    public View view = null;
    TextView base_name = null;//view.findViewById(R.id.base_name);
    TextView base_about = null;//view.findViewById(R.id.base_about);

    TextView base_coins = null;
    TextView base_magicCard = null;
    TextView base_score = null;
    ImageView rang_img = null;
    TextView rang_name = null;
    Button shop_button_lobby = null;
    AppCompatImageView base_img = null;// view.findViewById(R.id.base_img);
    Button boxLevelButton = null;//view.findViewById(R.id.view4);
    public RelativeLayout notWin = null;
public   WindowNotificationFragment windowNotificationFragment = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public LobbyFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.connection.mainActivity.lobbyFragment = this;
        String country = MainActivity.connection.config.country;
        Locale locale = new Locale(country);

        Locale.setDefault(locale);
// Create a new configuration object
        Configuration configure = new Configuration();
// Set the locale of the new configuration
        configure.locale = locale;
// Update the configuration of the Accplication context
        getResources().updateConfiguration(
                configure,
                getResources().getDisplayMetrics()
        );


    }

    public void addNotificationWindow(String text) {
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {


                FragmentManager fragmentManager = MainActivity.connection.mainActivity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                 windowNotificationFragment = new WindowNotificationFragment(text, LobbyFragment.this);
// Replace whatever is in the fragment_container view with this fragment
                transaction.add(R.id.notification_window, windowNotificationFragment);
                //  transaction.replace(R.id.fragmentMain, lobbyFragment, null);

// Commit the transaction
                transaction.commit();

            }
        });
    }

    public void removeNotificationWindow() {
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {


                FragmentManager fragmentManager = MainActivity.connection.mainActivity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                 transaction.remove(windowNotificationFragment);

                transaction.commit();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lobby, container, false);

     /*   TextView textView1 = view.findViewById(R.id.level_button);
        textView1.setText("Level "+type);

      */
        Button help = view.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.mainActivity.runHelpWindow();
            }
        });
        this.view = view;
        this.notWin = view.findViewById(R.id.notification_window);

        base_name = view.findViewById(R.id.base_name);
        base_about = view.findViewById(R.id.base_about);
        base_img = view.findViewById(R.id.base_img);
        boxLevelButton = view.findViewById(R.id.view4);
        shop_button_lobby = view.findViewById(R.id.shop_button_lobby);

        base_coins = view.findViewById(R.id.base_coins);
        base_magicCard = view.findViewById(R.id.magicKey_score);
        base_score = view.findViewById(R.id.base_score);
        rang_img = view.findViewById(R.id.panel_rang_img);
        rang_name = view.findViewById(R.id.base_rang);

        Button playButton = view.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.send("lobby;inited");
            }
        });
        Button nextPlayerButton = view.findViewById(R.id.next);
        Button backPlayerButton = view.findViewById(R.id.back);

        boxLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerController playerController = MainActivity.playerControllerStatic;
                if (playerController == null) return;
                JSONObject jsonObject = playerController.getPlayerIs();
                try {
                    String id = jsonObject.getString("id");
                    int type = jsonObject.getInt("type");
                    JSONObject jsonObject1 = playerController.playerPreviewController.get(id).json;
                    JSONObject jsonObject2 = jsonObject1.getJSONArray("levels").getJSONObject(type);
                    Bitmap bitmap = playerController.playerPreviewController.get(id).image_sources;
                   ;
                    MainActivity.connection.mainActivity.runleveldata(jsonObject1.getString("name" + MainActivity.connection.config.lan).split(" ")[0], jsonObject2, bitmap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        shop_button_lobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.connection.mainActivity.startLoadingShop();

                    }
                }).start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MainActivity.connection.send("lobby;shop_init");

            }
        });
        nextPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPlayer();

            }
        });
        backPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPlayer();
            }
        });


        updateUIwithInfotmationLobby();
        return view;
    }

    public void updateUIwithInfotmationLobby() {
        /*for (Map.Entry<String, PlayerPreview> map : MainActivity.playerControllerStatic.playerPreviewController.hash.entrySet()) {

        }*/
        int type = 0;
        try {
            type = MainActivity.playerControllerStatic.getPlayerIs().getInt("type");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (boxLevelButton != null)
            boxLevelButton.setText(getResources().getString(R.string.BUTTON_LEVEL) + " " + type);

        PlayerPreviewController playerPreviewController = MainActivity.playerControllerStatic.playerPreviewController;
        String name = playerPreviewController.get(MainActivity.playerControllerStatic.id).name;
        String description = playerPreviewController.get(MainActivity.playerControllerStatic.id).text;
        JSONObject jsonPlayer = MainActivity.playerControllerStatic.json;

        try {
            int money = jsonPlayer.getInt("money");
            setTextViewItalic("" + money, base_coins);
            int score = jsonPlayer.getInt("score");
            setTextViewItalic("" + score, base_score);


            int magicKey = jsonPlayer.getInt("magicKey");
            setTextViewItalic("" + magicKey, base_magicCard);
            setTextViewItalic(MainActivity.connection.mainActivity.rangArray[jsonPlayer.getInt("rang")], rang_name);

            Bitmap rang_image = (Bitmap) MainActivity.playerControllerStatic.imageLoader.images.get("rang_" + jsonPlayer.getInt("rang") + "v" + MainActivity.connection.version);
            this.rang_img.setImageBitmap(rang_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setTextViewItalic(name, base_name);
        setTextViewItalic(description, base_about);


        Bitmap image = (Bitmap) MainActivity.playerControllerStatic.imageLoader.images.get(MainActivity.playerControllerStatic.id + "_prev" + "v" + MainActivity.connection.version);

        if (image == null) {
            new Throwable("Image is null!!!");
        } else {
            base_img.setImageBitmap(image);

            //imageView.setImageDrawable(drawable);
        }
    }

    public void setTextViewItalic(String text, TextView view) {
       /* SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);*/
        view.setText(text);


    }

    public void nextPlayer() {
        try {
            PlayerController playerController = MainActivity.playerControllerStatic;
            PlayerPreviewController playerPreviewController = MainActivity.playerControllerStatic.playerPreviewController;
            JSONObject jsonPlayer = playerController.json;
            JSONObject jsonPlayerObject = playerController.getPlayerIs();
            int jsonPlayerFromBaseIndex = playerController.getPlayerIsIndex() + 1;

            playerController.updateDataIs(playerController.getPlayerIsIndex(), false);
            jsonPlayerObject.put("is", false);

            JSONObject jsonPlayerFromBase = playerController.getPlayerIndex(jsonPlayerFromBaseIndex);

            if (jsonPlayerFromBase == null) {
                return;

            }
            MainActivity.connection.send("lobby;change;" + jsonPlayerFromBase.getString("id"));
            jsonPlayerFromBase.put("is", true);
            playerController.updateDataIs(jsonPlayerFromBaseIndex, true);
            MainActivity.playerControllerStatic.updateId();
            updateUIwithInfotmationLobby();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void backPlayer() {
        try {
            PlayerController playerController = MainActivity.playerControllerStatic;
            PlayerPreviewController playerPreviewController = MainActivity.playerControllerStatic.playerPreviewController;
            JSONObject jsonPlayer = playerController.json;
            JSONObject jsonPlayerObject = playerController.getPlayerIs();
            int jsonPlayerFromBaseIndex = playerController.getPlayerIsIndex() - 1;

            playerController.updateDataIs(playerController.getPlayerIsIndex(), false);
            jsonPlayerObject.put("is", false);

            JSONObject jsonPlayerFromBase = playerController.getPlayerIndex(jsonPlayerFromBaseIndex);

            if (jsonPlayerFromBase == null) {
                return;

            }
            MainActivity.connection.send("lobby;change;" + jsonPlayerFromBase.getString("id"));
            jsonPlayerFromBase.put("is", true);
            playerController.updateDataIs(jsonPlayerFromBaseIndex, true);
            MainActivity.playerControllerStatic.updateId();
            updateUIwithInfotmationLobby();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void init() {
        PlayerController playerController = MainActivity.playerControllerStatic;
        JSONObject jsonPlayer = playerController.json;
        JSONObject jsonPlayerFromBase = playerController.getPlayerIs();

    }


}