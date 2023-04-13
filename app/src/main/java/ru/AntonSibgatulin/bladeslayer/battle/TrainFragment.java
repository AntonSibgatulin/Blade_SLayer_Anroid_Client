package ru.AntonSibgatulin.bladeslayer.battle;

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
import android.widget.TextView;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;


public class TrainFragment extends Fragment {

    public String level = null;
    public String description = null;
    public Bitmap bitmap = null;


    public TrainFragment() {
        // Required empty public constructor
    }
    public TrainFragment(int level, String description, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.level = "Уровень " + level;
        this.description = description;
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
        View view = inflater.inflate(R.layout.fragment_train, container, false);
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
                MainActivity.connection.mainActivity.playTrain();
            }
        });

        return view;
    }
}