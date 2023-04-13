package ru.AntonSibgatulin.bladeslayer.battle;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;

public class LocationPreviewBattleFragment extends Fragment {
    public JSONObject jsonObject = null;
    String name = null;

    public LocationPreviewBattleFragment(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

    }

    public LocationPreviewBattleFragment() {
    }

    public TextView heartView = null,
            speedView = null, jumpView = null;
    public ImageView mainImage = null;

    public TextView count_of_players = null;

    public void changeCountOfPlayers(int count) {
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getContext()!=null) {
                    count_of_players.setText("" + count);
                }
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_preview_battle, container, false);
        try {
            int index = jsonObject.getInt("index");
            Bitmap bitmap = MainActivity.playerControllerStatic.imageLoader.get("train_" + index);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            int maxPlayer = jsonObject.getInt("maxPlayer");
            String type = jsonObject.getString("type");
            String des = jsonObject.getString("des");
            int count_of_players = jsonObject.getInt("count");

            this.count_of_players = view.findViewById(R.id.shop_energy);
            heartView = view.findViewById(R.id.shop_heart);
            speedView = view.findViewById(R.id.shop_speed);
            jumpView = view.findViewById(R.id.shop_jump);
            mainImage = view.findViewById(R.id.shop_image_player);
            TextView description = view.findViewById(R.id.desciption);
            //description.setText(des);
            this.count_of_players.setText(count_of_players + " ");
            heartView.setText(name + " ");
            speedView.setText(type + " ");
            jumpView.setText(maxPlayer + "");
            if (bitmap != null)
                mainImage.setImageBitmap(bitmap);
            Button button = view.findViewById(R.id.join_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.connection.send("lobby;play_map_multiplay;" + id + ";null");
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;

    }

    public void initilization() {

    }
}