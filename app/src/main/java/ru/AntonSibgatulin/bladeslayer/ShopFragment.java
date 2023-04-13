package ru.AntonSibgatulin.bladeslayer;

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

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.shop.PlayerShopFragment;
import ru.AntonSibgatulin.bladeslayer.shop.ShopLoadingActivityFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {
public View view = null;


    public ShopFragment() {
        // Required empty public constructor
    }
  public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        this.view = view;
        Button buttonClose = view.findViewById(R.id.close_shop_button);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        MainActivity.connection.mainActivity.startMenu();


            }
        });
new Thread(new Runnable() {
    @Override
    public void run() {
        MainActivity.connection.shopModel.init(ShopFragment.this);
        MainActivity.connection.mainActivity.removeLoading();
    }
}).start();

        return view;
    }
}