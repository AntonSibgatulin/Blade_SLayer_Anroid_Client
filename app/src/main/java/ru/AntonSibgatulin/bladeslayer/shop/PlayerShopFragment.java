package ru.AntonSibgatulin.bladeslayer.shop;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;


public class PlayerShopFragment extends Fragment {

    private  int priceMagiccard=0;
    private  int price = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  public View view = null;
    public String id = null;
    public String name = null;
    public int money=0,score = 0,magicKey = 0,count_of_cards = 0;
    public JSONObject playerConfig = null;
    public int heart = 0,energy = 0,power = 0,run = 0;
    public double speed = 0,jump = 0,energy_recovery = 0;


    public TextView nameView = null,heartView = null,
    speedView = null,jumpView = null,energyView = null,powerView =  null,
    running_distanceView = null,velocity_of_descoveryVeiw= null;
    public ImageView mainImage = null;
    public Bitmap mainBitmap = null;
    public String idel = null;
    public int ids = 0;

    public PlayerShopFragment(String idel, String id,  String name, int price, int priceMagiccard, int score, int magicKey, int countOfCards,JSONObject playerConfig,
                               Bitmap mainBitmap,int ids) {
        this.idel = idel;
        this.id = id;
        this.name = name;
        this.money = money;
        this.score = score;
        this.priceMagiccard = priceMagiccard;
        this.price = price;
        this.magicKey = magicKey;
        this.count_of_cards = countOfCards;
        this.playerConfig = playerConfig;
        try {
            heart = playerConfig.getInt("health");
            speed = playerConfig.getDouble("speed");
            jump = playerConfig.getDouble("jump");
            energy = playerConfig.getInt("energy");
            power = playerConfig.getInt("power");
            run = playerConfig.getInt("run");
            energy_recovery = playerConfig.getDouble("energy_recovery");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mainBitmap = mainBitmap;
        this.ids = ids;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_shop, container, false);
        this.view = view;
        // получаем экземпляр FragmentTransaction

        nameView = view.findViewById(R.id.nameViewShop);
        heartView = view.findViewById(R.id.shop_heart);
        speedView = view.findViewById(R.id.shop_speed);
        jumpView = view.findViewById(R.id.shop_jump);
        energyView = view.findViewById(R.id.shop_energy);
        running_distanceView = view.findViewById(R.id.shop_runningdistance);
        velocity_of_descoveryVeiw = view.findViewById(R.id.shop_powerrecovery);
        mainImage =view.findViewById(R.id.shop_image_player);

        Button buyRubles = view.findViewById(R.id.buy_for_money);
        buyRubles.setText(getResources().getString(R.string.BUTTON_BUY_FOR)+" "+price+"");

        Button buymk = view.findViewById(R.id.buy_for_mk);
        buymk.setText(getResources().getString(R.string.TEXT_MAGICKEY)+" "+magicKey+"");


        buyRubles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
MainActivity.connection.send("lobby;shop_buy;players_"+ids+";money");
            }
        });


        initilization();
        //MainActivity.connection.shopModel.init();

        return view;
    }



    public void initilization()
    {
        if(mainBitmap!=null) {
            mainImage.setImageBitmap(mainBitmap);
        }

        nameView.setText(name+" ");
        heartView.setText(heart+" ");
        speedView.setText(speed+" ");
        jumpView.setText(jump+"");
        energyView.setText(energy+" ");
        running_distanceView.setText(run+" ");
        velocity_of_descoveryVeiw.setText(energy_recovery+"");


    }

    // TODO: Rename and change types of parameters
    public PlayerShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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


}