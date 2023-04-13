package ru.AntonSibgatulin.bladeslayer.battle;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;


public class LocationFragment extends Fragment {

public JSONObject jsonObject = null;
public HashMap<String,LocationPreviewBattleFragment> battleFragmentHashMap = new HashMap<>();
    public LocationFragment(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        Button buttonClose = view.findViewById(R.id.close_button);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.mainActivity.locationFragment = null;
                MainActivity.connection.mainActivity.startMenu();
            }
        });
        Button create = view.findViewById(R.id.create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name="Map#"+new Random().nextInt(1000000);
                MainActivity.connection.send("lobby;create_map_multiplay;{\"type\":\"TM\",\"name\":\"" + name + "\",\"maxPlayer\":10}");
            }
        });
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("maps");
            for(int i = 0;i<jsonArray.length();i++){
               JSONObject jsonObject = jsonArray.getJSONObject(i);
                LocationPreviewBattleFragment locationPreviewBattleFragment = new LocationPreviewBattleFragment(jsonObject);
                battleFragmentHashMap.put(""+jsonObject.getInt("id"),locationPreviewBattleFragment);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().add(R.id.data_element,locationPreviewBattleFragment).commit();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void changeCountOfPeople(String s, Integer valueOf) {
        LocationPreviewBattleFragment locationPreviewBattleFragment = battleFragmentHashMap.get(s);
        locationPreviewBattleFragment.changeCountOfPlayers(valueOf);
    }

    public void add(JSONObject jsonObject) {
        try {
            LocationPreviewBattleFragment locationPreviewBattleFragment = new LocationPreviewBattleFragment(jsonObject);

            battleFragmentHashMap.put(""+jsonObject.getInt("id"),locationPreviewBattleFragment);
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager==null)return;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if(fragmentTransaction==null)return;
            fragmentTransaction.add(R.id.data_element,locationPreviewBattleFragment).commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}