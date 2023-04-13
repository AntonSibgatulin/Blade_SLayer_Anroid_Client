package ru.AntonSibgatulin.bladeslayer.shop;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MagicKeyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MagicKeyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  String idel =null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public int price = 0;
    public int count = 0;

    public MagicKeyFragment(String idel,int price, int count) {
        this.idel = idel;
        this.price = price;
        this.count = count;

    }
    public MagicKeyFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_magic_key, container, false);

        TextView textView = view.findViewById(R.id.shop_count);
        textView.setText(count+"");
        TextView textView2 = view.findViewById(R.id.shop_price);
        textView2.setText(price+"₽");
        Button buyRubles = view.findViewById(R.id.buy_for_money);
        buyRubles.setText(""+price+"₽");


        return view;
    }
}