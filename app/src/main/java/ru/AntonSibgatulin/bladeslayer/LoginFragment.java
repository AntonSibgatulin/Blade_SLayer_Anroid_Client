package ru.AntonSibgatulin.bladeslayer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.register.RegFragment;

public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public EditText login = null;
    public EditText password = null;
    public Button btnLogin = null;
    public WindowNotificationFragment windowNotificationFragment=null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
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
    public void addNotificationWindow(String text) {
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {


                FragmentManager fragmentManager = MainActivity.connection.mainActivity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                windowNotificationFragment = new WindowNotificationFragment(text, LoginFragment.this);
// Replace whatever is in the fragment_container view with this fragment
                transaction.add(R.id.notification_window, windowNotificationFragment);
                //  transaction.replace(R.id.fragmentMain, lobbyFragment, null);

// Commit the transaction
                transaction.commit();

            }
        });
    }

    public void removeNotificationWindow() {
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {


                FragmentManager fragmentManager = MainActivity.connection.mainActivity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.remove(windowNotificationFragment);

                transaction.commit();

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        login = view.findViewById(R.id.login_signin);
        password = view.findViewById(R.id.password_signin);
        btnLogin = view.findViewById(R.id.buttonLogin);
        //  if(btnLogin!=null)
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                 if (!btnLogin.getText().equals(getResources().getString(R.string.TEXT_LOADING) + "...")) {
                    MainActivity.connection.send("login;" + login.getText().toString() + ";" + password.getText().toString() + ";" + MainActivity.connection.config.country);
                    MainActivity.connection.save_data = login.getText().toString() + ";" + password.getText().toString();
                    MainActivity.connection.mainActivity.saveData(login.getText().toString() + ";" + password.getText().toString());

                }
                if (getContext() != null) {
                    btnLogin.setText(getResources().getString(R.string.TEXT_LOADING) + "...");
                }

            }

        });


        Button button = view.findViewById(R.id.btnreg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.mainActivity.initRegModel();
            }
        });


        return view;
    }
}