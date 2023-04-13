package ru.AntonSibgatulin.bladeslayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.AntonSibgatulin.bladeslayer.captcha.CaptchaFragment;
import ru.AntonSibgatulin.bladeslayer.register.RegFragment;

public class WindowNotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String text= null;

    public LobbyFragment lobbyFragment = null;
    public RegFragment regFragment = null;
    public LoginFragment loginFragment = null;
    public CaptchaFragment captchaFragment = null;


    public WindowNotificationFragment(String text,LobbyFragment lobbyFragment) {
        this.text = text;
        this.lobbyFragment = lobbyFragment;
    }
    public WindowNotificationFragment(String text,RegFragment regFragment) {
        this.text = text;
        this.regFragment = regFragment;
    }
    public WindowNotificationFragment(String text,LoginFragment loginFragment) {
        this.text = text;
        this.loginFragment = loginFragment;
    }
    public WindowNotificationFragment(String text,CaptchaFragment captchaFragment) {
        this.text = text;
        this.captchaFragment = captchaFragment;
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
        View view =  inflater.inflate(R.layout.fragment_window_notification, container, false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(text);
        Button close =view.findViewById(R.id.close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lobbyFragment!=null && lobbyFragment.windowNotificationFragment !=null) {
                    lobbyFragment.removeNotificationWindow();
                }
                if(regFragment!=null &&  regFragment.windowNotificationFragment !=null) {
                    regFragment.removeNotificationWindow();
                }
                if(loginFragment!=null &&  loginFragment.windowNotificationFragment !=null) {
                    loginFragment.removeNotificationWindow();
                }
                if(captchaFragment!=null &&  captchaFragment.windowNotificationFragment !=null) {
                    captchaFragment.removeNotificationWindow();
                }
            }
        });
        return view;
    }
}