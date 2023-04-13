package ru.AntonSibgatulin.bladeslayer.swing;

import android.os.CountDownTimer;

public class Timer  extends CountDownTimer
{
    public ActionPerformed actionPerformed = null;
    public Timer(ActionPerformed actionPerformed,int interval) {
        super(230000000, interval); //interval/ticks each second.
        this.actionPerformed = actionPerformed;
        start();
    }

    @Override
    public void onFinish() {
        this.start();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(actionPerformed!=null)
       actionPerformed.actionPreformed();

    }
}