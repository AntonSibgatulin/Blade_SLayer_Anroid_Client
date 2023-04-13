package ru.AntonSibgatulin.bladeslayer.shop;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;

public class ShopLoadingActivityFragment extends Fragment {
    public int count = 0;
    public String[] array = {".", "..", "...", "...."};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShopLoadingActivityFragment() {

        MainActivity.loader = this;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_loading_activity, container, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MainActivity.connection.shopModel ==null || MainActivity.connection.shopModel.isLoading()) {
                    try {
                        Thread.sleep(500);
                        TextView textView = view.findViewById(R.id.loadingShop);
                        count++;
                        if (count >= array.length) count = 0;
                        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(getContext()!=null) {
                                    textView.setText(getResources().getString(R.string.TEXT_LOADING) + " " + array[count]);

                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }
        }).start();


        return view;
    }


}
