package ru.AntonSibgatulin.bladeslayer.captcha;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;
import ru.AntonSibgatulin.bladeslayer.WindowNotificationFragment;

public class CaptchaFragment extends Fragment {


    public Button enter = null;
    public EditText text = null;
    public ImageView imageView = null;
    public WindowNotificationFragment windowNotificationFragment = null;
    public Bitmap mainBitmap = null;

    public CaptchaFragment() {
        // Required empty public constructor
    }

    public CaptchaFragment(Bitmap bitmap) {
        mainBitmap = bitmap;
        // Required empty public constructor
    }

    public void addNotificationWindow(String text) {
        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {


                FragmentManager fragmentManager = MainActivity.connection.mainActivity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                windowNotificationFragment = new WindowNotificationFragment(text, CaptchaFragment.this);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.connection.mainActivity.captchaFragment = this;
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
        View view = inflater.inflate(R.layout.fragment_captcha, container, false);
        imageView = view.findViewById(R.id.password_signin);
        this.text = view.findViewById(R.id.login_signin);
        this.enter = view.findViewById(R.id.buttonLogin);
        if (imageView != null) {
            if (mainBitmap != null) {
                imageView.setImageBitmap(mainBitmap);
            }
        }

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.getText().length()>0) {
                    MainActivity.connection.send("captcha;" + text.getText());
                    text.setText("");
                }
            }
        });
        return view;
    }
    public void changeBitmap (Bitmap bitmap){
        this.mainBitmap = bitmap;

        MainActivity.connection.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (imageView != null) {
                    if (mainBitmap != null) {
                        imageView.setImageBitmap(mainBitmap);
                    }
                }
            }
        });

    }
}