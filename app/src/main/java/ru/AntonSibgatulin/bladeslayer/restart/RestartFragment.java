package ru.AntonSibgatulin.bladeslayer.restart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;


public class RestartFragment extends Fragment {

    public RestartFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restart, container, false);
        Button restart = view.findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mStartActivity = new Intent(MainActivity.connection.mainActivity.getApplicationContext(), MainActivity.class);
                int mPendingIntentId = 123456;
                int code = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    code =  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;// or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                   code = PendingIntent.FLAG_UPDATE_CURRENT;
                }

                PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.connection.mainActivity.getApplicationContext(), mPendingIntentId,    mStartActivity, code);
                AlarmManager mgr = (AlarmManager)MainActivity.connection.mainActivity.getApplicationContext().getSystemService(MainActivity.connection.mainActivity.getApplicationContext().ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2500, mPendingIntent);
                System.exit(0);
            }
        });
        return view;
    }
}