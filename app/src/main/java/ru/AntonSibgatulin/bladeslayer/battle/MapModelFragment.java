package ru.AntonSibgatulin.bladeslayer.battle;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;
import ru.AntonSibgatulin.bladeslayer.battle.PlayerLoader.StylesFragment;
import ru.AntonSibgatulin.bladeslayer.clientconnection.ClientWebSocket;


public class MapModelFragment extends Fragment /*extends AndroidFragmentApplication*/ {



    private boolean bol = false;

    public TextView numTextView = null;

    public MapModelFragment() {
        // Required empty public constructor
    }

    public ClientWebSocket clientWebSocket = null;
    public ArrayList<ArrayList<Integer>> arr = null;


    public MapModelFragment(Point point, ClientWebSocket clientWebSocket, ArrayList<ArrayList<Integer>> arr) {
        this.clientWebSocket = clientWebSocket;

        this.point = point;
        this.arr = arr;
    }

    public Point point = null;

    public MapModelFragment(Point point) {
        // Required empty public constructor
        this.point = point;
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
    public  void updateStyle(int style){
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numTextView.setText(""+style);
            }
        });
    }
    public  void updateStyle(String style){
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getContext()!=null) {
                    numTextView.setText("" + style);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_model, container, false);


        //MapFragment mapFragment = new MapFragment();
        //  AndroidFragmentApplication cfg = new AndroidFragmentApplication();


        //   view.add
        // RelativeLayout linearLayout = view.findViewById(R.id.PlaceForGame);
        //linearLayout.addView(mapFragment.initializeForView(new MapModel(MainActivity.connection)));
        // GLSurfaceView20 glSurfaceView = view.findViewById(R.id.glSurface);

        // MapModel mapModel = new MapModel(clientWebSocket);

        //.setRenderer(new MapModel(clientWebSocket));
        //applicationAdapter
        // getFragmentManager().beginTransaction().add(R.id.PlaceForGame1,  mapFragment);

        // MapModel mapModel = new MapModel(view.getContext(), null, null);
        //  mapModel.start();

        numTextView = view.findViewById(R.id.num);
        ImageButton rightb = view.findViewById(R.id.right);
        ImageButton leftb = view.findViewById(R.id.left);
        rightb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.player.downKeyRight();
            }
        });
        leftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.player.downKeyLeft();
            }
        });
        Button exit = view.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.send("battle;exit");
            }
        });
        // clientWebSocket.mapMain = new MapModelKt(view.getContext(), clientWebSocket);
        clientWebSocket.mapMain.setMapModelFragment(this);
        clientWebSocket.mapMain.setMapModelKotlin(new MapModelKotlin(view.getContext(), clientWebSocket));
       // clientWebSocket.mapMain.setMapModelKotlin(new MapModelKt(view.getContext(), clientWebSocket));
        ImageButton left = view.findViewById(R.id.left_button);
        TextView fps = view.findViewById(R.id.fps);
        TextView ping = view.findViewById(R.id.ping);

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        MainActivity.connection.send("battle;down;left");
                        break;
                    case MotionEvent.ACTION_UP:
                        MainActivity.connection.send("battle;up;left");
                        break;

                }
                return false;
            }
        });


        ImageButton right = view.findViewById(R.id.right_button);

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        MainActivity.connection.send("battle;down;right");
                        break;
                    case MotionEvent.ACTION_UP:
                        MainActivity.connection.send("battle;up;right");
                        break;

                }
                return false;
            }
        });


        ImageButton jump = view.findViewById(R.id.jump_button);

        jump.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        MainActivity.connection.send("battle;down;up");
                        break;


                }
                return false;
            }
        });
        clientWebSocket.mapMain.getMapModelKotlin().setFpsView(fps);
        clientWebSocket.mapMain.getMapModelKotlin().setPingView(ping);



        ImageButton speed_button = view.findViewById(R.id.speed);
        speed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MapModelFragment.this.bol){
                    MapModelFragment.this.bol = false;
                    speed_button.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.superpower));
                   // speed_button.setText(""+getResources().getString(R.string.TEXT_ON));
                    MainActivity.connection.send("battle;up;shift");

                }else{
                    MapModelFragment.this.bol = true;
                    MainActivity.connection.send("battle;down;shift");
                    speed_button.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.doublepower));

                    // speed_button.setText(""+getResources().getString(R.string.TEXT_OFF));

                }
            }
        });
        ImageButton fire_button = view.findViewById(R.id.fire_button);
        fire_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    MainActivity.connection.send("battle;down;fire");

            }
        });
        LinearLayout styles = view.findViewById(R.id.styles);
        JSONObject jsonPlayer = MainActivity.connection.player.json;

        JSONObject jsonPlayerFromBase = MainActivity.connection.player.getPlayerIs();
            /*var id = jsonPlayerFromBase.id;
            var type = jsonPlayerFromBase.type;

            var jsonCharcter = playerPreviewController.hash.get(id).json;
           */
        String breath = null;
        MainActivity.connection.player.key=new ArrayList<>();
        try {
            breath = jsonPlayerFromBase.getJSONArray("breath").getJSONObject(0).getString("id");
            JSONObject jsonBreath = MainActivity.connection.breathPreviewController.hash.get(breath).jsonObject;
            JSONArray JSONArrayStyles =jsonPlayerFromBase.getJSONArray("breath").getJSONObject(0).getJSONArray("styles");
            for(int i = 0;i< JSONArrayStyles.length();i++){
                int index = jsonPlayerFromBase.getJSONArray("breath").getJSONObject(0).getJSONArray("styles").getInt(i);

            if(index >=jsonBreath.getJSONArray("styles").length()){
                continue;
                //index = jsonBreath.getJSONArray("styles").length()-1;
            }
               // if (jsonBreath.getJSONArray("styles"). == null) continue;
                String style_name = jsonBreath.getJSONArray("styles").getJSONObject(index).getString("des"+MainActivity.connection.config.lan);

                //  var style_img_src = images.get(breath + "_" + index).src;
                if (jsonBreath.getJSONArray("styles").getJSONObject(index).has("none") && jsonBreath.getJSONArray("styles").getJSONObject(index).getBoolean("none")) continue;

                StylesFragment stylesFragment = new StylesFragment(style_name,i);
                MainActivity.connection.player.key.add(i+1);
                /*
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.styles,stylesFragment);
                fragmentTransaction.commit();

                 */


            }
            MainActivity.connection.player.downKey(0);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* speed_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        MainActivity.connection.send("battle;down;shift");
                        break;
                    case MotionEvent.ACTION_UP:
                        MainActivity.connection.send("battle;up;shift");
                        break;

                }
                return false;
            }
        });

        */



        Button pause_button = view.findViewById(R.id.pause_button);
        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (pause_button.getText().equals(getResources().getString(R.string.BUTTON_PAUSE)+"")) {

                    pause_button.setText(getResources().getString(R.string.BUTTON_RESUME));
                   clientWebSocket.send("battle;pause");


               }
               else  if (pause_button.getText().equals(getResources().getString(R.string.BUTTON_RESUME)+"")) {
                    clientWebSocket.send("battle;resume");
                    pause_button.setText(getResources().getString(R.string.BUTTON_PAUSE));
                }
            }
        });
        clientWebSocket.mapMain.getMapModelKotlin().setPauseButton(pause_button);
        clientWebSocket.mapMain.getMapModelKotlin().start();

          RelativeLayout linearLayout = view.findViewById(R.id.PlaceForGame);
      linearLayout.addView(clientWebSocket.mapMain.getMapModelKotlin());

/*
        clientWebSocket.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
        */
       /* RelativeLayout linearLayout = view.findViewById(R.id.PlaceForGame);
        clientWebSocket.mapMain.setMapModel(new MapModel(clientWebSocket));
       // AndroidApplicationConfiguration androidApplicationConfiguration = new AndroidApplicationConfiguration();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        // androidApplicationConfiguration.useGyroscope=true;
        linearLayout.addView(initializeForView(clientWebSocket.mapMain.getMapModel(),config));


        */
        return view;
    }
/*
    @Override
    public void exit() {

    }

 */
}