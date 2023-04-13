package ru.AntonSibgatulin.bladeslayer.battle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import ru.AntonSibgatulin.bladeslayer.Players.PlayerController;
import ru.AntonSibgatulin.bladeslayer.loader.ImageLoader;

public class MapModel extends SurfaceView implements Runnable {
    public boolean isWork = true;
    public ArrayList<ArrayList<Integer>> map = new ArrayList<>();
    public ImageLoader imageLoader = null;
    public PlayerController playerController = null;
    public Thread mainThread = new Thread(this);
    public Paint paint= new Paint();

    public MapModel(Context context, ArrayList<ArrayList<Integer>> arrayList, ImageLoader imageLoader) {
        super(context);
        this.map = arrayList;
        this.imageLoader = imageLoader;


    }

    @Override
    public void run() {
        while (isWork) {
            update();
            draw();
        }
    }

    private void update() {
    }

    private void draw() {

        SurfaceHolder holder = getHolder();
        Surface surface = holder.getSurface();
        if (surface.isValid()) {
            //Log.e("canvas","isDrawing");
            Canvas canvas = surface.lockCanvas(null);

            action(canvas);





            surface.unlockCanvasAndPost(canvas);
        }
    }

    private void action(Canvas canvas) {

        canvas.drawColor(Color.GREEN, PorterDuff.Mode.CLEAR);

        setColor(Color.GREEN);

        canvas.drawRect(10,10,50,50,paint);



    }
    public void setColor(int color){
        paint.setColor(color);
    }

    public void start() {
        mainThread.start();
    }
}
