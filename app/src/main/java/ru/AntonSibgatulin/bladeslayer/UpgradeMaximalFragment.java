package ru.AntonSibgatulin.bladeslayer;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
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

public class UpgradeMaximalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public View view = null;
    public String id = null;
    public String name = null;
    public TextView nameTextView = null;

    public JSONObject playerConfig = null;
    public int heart = 0, energy = 0, power = 0, run = 0;
    public double speed = 0, jump = 0, energy_recovery = 0;

    public int minRang = 0;
    public int money = 0;


    public TextView nameView = null, heartView = null,
            speedView = null, jumpView = null, energyView = null, powerView = null,
            running_distanceView = null, velocity_of_descoveryVeiw = null;
    public ImageView mainImage = null;
    public Bitmap mainBitmap = null;
    public boolean buy = false;


    public UpgradeMaximalFragment(String id, String name, JSONObject playerConfig,
                                  Bitmap mainBitmap) {

        this.id = id;
        this.name = name;


        this.playerConfig = playerConfig;
        try {

            heart = playerConfig.getInt("health");

            speed = playerConfig.getDouble("speed");
            jump = playerConfig.getDouble("jump");

            energy = playerConfig.getInt("energy");
            power = playerConfig.getInt("power");

            run = playerConfig.getInt("run");
            energy_recovery = playerConfig.getDouble("energy_recovery");

            money = playerConfig.getInt("price");
            minRang = playerConfig.getInt("min_rang");
            buy = playerConfig.getBoolean("buy");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mainBitmap = mainBitmap;
    }


    private void initilization() {
        if (mainBitmap != null)
            mainImage.setImageBitmap(mainBitmap);
        nameTextView.setText(getResources().getString(R.string.PRICE_FOR_IT) + " " + money + ".\n" + getResources().getString(R.string.TEXT_MINIMAL_RANG) + " " + MainActivity.connection.mainActivity.rangArray[minRang]);
        nameView.setText(name + " ");
        heartView.setText("+" + heart + " ");
        speedView.setText("+" + speed + " ");
        jumpView.setText("+" + jump + "");
        energyView.setText("+" + energy + " ");
        running_distanceView.setText("+" + run + " ");
        velocity_of_descoveryVeiw.setText("+" + energy_recovery + "");


    }


    public UpgradeMaximalFragment() {
        // Required empty public constructor
    }

/*
    public UpgradeMaximalFragment(){

    }


 */

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpgradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpgradeFragment newInstance(String param1, String param2) {
        UpgradeFragment fragment = new UpgradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade_maximal, container, false);
        this.view = view;
        // получаем экземпляр FragmentTransaction
        nameTextView = view.findViewById(R.id.info);

        nameView = view.findViewById(R.id.nameViewShop);
        heartView = view.findViewById(R.id.shop_heart);
        speedView = view.findViewById(R.id.shop_speed);
        jumpView = view.findViewById(R.id.shop_jump);
        energyView = view.findViewById(R.id.shop_energy);
        running_distanceView = view.findViewById(R.id.shop_runningdistance);
        velocity_of_descoveryVeiw = view.findViewById(R.id.shop_powerrecovery);
        mainImage = view.findViewById(R.id.shop_image_player);
        Button clsButton = view.findViewById(R.id.close_button);
        clsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.mainActivity.startMenu();
            }
        });
        Button improve = view.findViewById(R.id.improve_button);
        if(buy=false){
            improve.setVisibility(View.INVISIBLE);
        }
    else{
        improve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.connection.send("lobby;upgrade;" + MainActivity.connection.player.id);
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }

                 */
              //  MainActivity.connection.mainActivity.startMenu();
            }
        });
        }

        initilization();
        //MainActivity.connection.shopModel.init();

        return view;
        //return inflater.inflate(R.layout.fragment_upgrade_maximal, container, false);
    }
}