package ru.AntonSibgatulin.bladeslayer.battle;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WindowPlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WindowPlayFragment extends Fragment {


    public WindowPlayFragment() {
        // Required empty public constructor
    }

    public String level = null;
    public String description = null;
    public Bitmap bitmap = null;
public String type= null;
    public WindowPlayFragment(int level, String description, Bitmap bitmap,String type) {
        this.bitmap = bitmap;
        this.level = ""+ level;
        this.description = description;
        this.type = type;

    }


    public static WindowPlayFragment newInstance(String param1, String param2) {
        WindowPlayFragment fragment = new WindowPlayFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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

        this.level = getResources().getString(R.string.BUTTON_LEVEL)+" " + level;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_window_play, container, false);


        Button cls = view.findViewById(R.id.close_button);
        Button play = view.findViewById(R.id.play_button);



        Button multiplayer = view.findViewById(R.id.multiply_button);
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.send("lobby;get_map_data");
            }
        });



        TextView name = view.findViewById(R.id.name_window);
        name.setText(level);
        TextView description = view.findViewById(R.id.text_level);
        description.setText(this.description);
        if (bitmap != null) {
            ImageView imageView = view.findViewById(R.id.img_level);
            imageView.setImageBitmap(bitmap);
        }

        cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.mainActivity.startMenu();

            }
        });

        play.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
                    MainActivity.connection.mainActivity.startLoadingShop();
                    if (type.equals("train")) {

                        // MainActivity.connection.send("lobby;train;" + MainActivity.connection.config.innerWidth + ";" + MainActivity.connection.config.innerHeight);
                        MainActivity.connection.mainActivity.playTrain();
                    } else if (type.equals("play")) {
                        MainActivity.connection.mainActivity.play();
                    }

            }
            });

            // Inflate the layout for this fragment
        return view;
        }
    }