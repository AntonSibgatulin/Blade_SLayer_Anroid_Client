package ru.AntonSibgatulin.bladeslayer.Players;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoPlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoPlayerFragment() {
        // Required empty public constructor
    }

    public View view = null;
    public String id = null;
    public String name = null;

    public JSONObject playerConfig = null;
    public int heart = 0, energy = 0, power = 0, run = 0;
    public double speed = 0, jump = 0, energy_recovery = 0;


    public TextView nameView = null, heartView = null,
            speedView = null, jumpView = null, energyView = null, powerView = null,
            running_distanceView = null, velocity_of_descoveryVeiw = null;
    public ImageView mainImage = null;
    public Bitmap mainBitmap = null;


    public double round(double number) {
        double value = 34.777774;
        double scale = Math.pow(10, 2);
        double result = Math.ceil(value * scale) / scale;
    return result;
    }


    public InfoPlayerFragment(String id, String name, JSONObject playerConfig,
                              Bitmap mainBitmap) {

        this.id = id;
        this.name = name;


        this.playerConfig = playerConfig;
        try {
            heart = playerConfig.getInt("health");
            speed = round(playerConfig.getDouble("speed"));
            jump = round(playerConfig.getDouble("jump"));
            energy = playerConfig.getInt("energy");
            power = playerConfig.getInt("power");
            run = playerConfig.getInt("run");
            energy_recovery = playerConfig.getDouble("energy_recovery");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mainBitmap = mainBitmap;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoPlayerFragment newInstance(String param1, String param2) {
        InfoPlayerFragment fragment = new InfoPlayerFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_player, container, false);
        this.view = view;
        // получаем экземпляр FragmentTransaction
        Button trainButton = view.findViewById(R.id.train_button);
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.send("lobby;train_get;" + MainActivity.connection.config.innerWidth + ";" + MainActivity.connection.config.innerHeight);
            }
        });
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


        Button button = view.findViewById(R.id.upgrade_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject jsonPlayerFromBase = MainActivity.connection.player.getPlayerIs();
                try {
                    String id = jsonPlayerFromBase.getString("id");
                    int myPlayerIdIndex = jsonPlayerFromBase.getInt("type");
                    JSONObject json = MainActivity.connection.playerPreviewController.get(id).json;
                    String name = MainActivity.connection.playerPreviewController.get(id).name.split(" ")[1];
                    JSONArray levels = json.getJSONArray("levels");
                    JSONObject jsonPlayer = MainActivity.connection.player.json;

                    if(levels.length()>myPlayerIdIndex+1){
                        JSONObject jsonMain = levels.getJSONObject(myPlayerIdIndex+1);
                        JSONObject jsonSecond = levels.getJSONObject(myPlayerIdIndex);
                        long dt_h = jsonMain.getInt("health")-jsonSecond.getInt("health");
                        double dt_speed = MainActivity.connection.config.rounded(jsonMain.getDouble("speed")-jsonSecond.getDouble("speed"));
                        int dt_energy = jsonMain.getInt("energy") - jsonSecond.getInt("energy");
                        int dt_power = jsonMain.getInt("power")-jsonSecond.getInt("power");
                        double dt_energy_recovery = MainActivity.connection.config.rounded(jsonMain.getDouble("energy_recovery") - jsonSecond.getDouble("energy_recovery"));
                        double dt_jump = MainActivity.connection.config.rounded(jsonMain.getDouble("jump") - jsonSecond.getDouble("jump"));
                        long run = jsonMain.getInt("run") - jsonSecond.getInt("run");
                        boolean buy = false;
                        boolean notEnoughMoney = false;
                        boolean notEnoughRang = false;
                        if(jsonMain.getInt("min_rang") >= jsonPlayer.getInt("rang")) {
                            notEnoughRang=true;
                        }
                        if(jsonMain.getInt("price")>= jsonPlayer.getInt("money")) {
                            notEnoughMoney=true;
                        }
                            if(jsonMain.getInt("min_rang") <= jsonPlayer.getInt("rang") && jsonMain.getInt("price")<= jsonPlayer.getInt("money")){
                            buy = true;
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("health",dt_h);
                        jsonObject.put("speed",dt_speed);
                        jsonObject.put("energy",dt_energy);
                        jsonObject.put("power",dt_power);
                        jsonObject.put("energy_recovery",dt_energy_recovery);
                        jsonObject.put("jump",dt_jump);
                        jsonObject.put("run",run);
                        jsonObject.put("buy",buy);
                        jsonObject.put("notEnoughMoney",notEnoughMoney);
                        jsonObject.put("notEnoughRang",notEnoughRang);
                        jsonObject.put("price",jsonMain.getInt("price"));
                        jsonObject.put("min_rang",jsonMain.getInt("min_rang"));

                        Bitmap bitmap = MainActivity.connection.mainActivity.imageLoader.get(id+"_prev");

                        MainActivity.connection.mainActivity.runupgradedata(name,jsonObject,bitmap);

                    }else{
                            JSONObject jsonSecond = levels.getJSONObject(myPlayerIdIndex);
                            long dt_h = jsonSecond.getInt("health");
                            double dt_speed = MainActivity.connection.config.rounded(jsonSecond.getDouble("speed"));
                            int dt_energy = jsonSecond.getInt("energy");
                            int dt_power =jsonSecond.getInt("power");
                            double dt_energy_recovery = MainActivity.connection.config.rounded(jsonSecond.getDouble("energy_recovery"));
                            double dt_jump = MainActivity.connection.config.rounded(jsonSecond.getDouble("jump"));
                            long run = jsonSecond.getInt("run");
                            boolean buy = false;
                            boolean notEnoughMoney = false;
                            boolean notEnoughRang = false;

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("health",dt_h);
                            jsonObject.put("speed",dt_speed);
                            jsonObject.put("energy",dt_energy);
                            jsonObject.put("power",dt_power);
                            jsonObject.put("energy_recovery",dt_energy_recovery);
                            jsonObject.put("jump",dt_jump);
                            jsonObject.put("run",run);
                            jsonObject.put("buy",buy);
                            jsonObject.put("notEnoughMoney",notEnoughMoney);
                            jsonObject.put("notEnoughRang",notEnoughRang);


                            Bitmap bitmap = MainActivity.connection.mainActivity.imageLoader.get(id+"_prev");

                            MainActivity.connection.mainActivity.runupgradedatamaximal(name,jsonObject,bitmap);

                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //MainActivity.connection.mainActivity.runupgradedata();
            }
        });


        initilization();
        //MainActivity.connection.shopModel.init();

        return view;
    }

    private void initilization() {
        if (mainBitmap != null)
            mainImage.setImageBitmap(mainBitmap);

        nameView.setText(name + " ");
        heartView.setText(heart + " ");
        speedView.setText(speed + " ");
        jumpView.setText(jump + "");
        energyView.setText(energy + " ");
        running_distanceView.setText(run + " ");
        velocity_of_descoveryVeiw.setText(energy_recovery + "");


    }

}