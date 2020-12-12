package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.content.Context.SENSOR_SERVICE;

public class CoinApocalypseView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private Canvas canvas;
    private Player player;
    private MainThread thread;

    private Paint paint;
    private Bitmap background;

    private SensorManager sensorManager;
    private Sensor gyroscoppSensor;

    private Dirt[] dirts;

    private String move = "none";

    private boolean gyroscopeHandling = false;

    public CoinApocalypseView(Context context, int sizeX, int sizeY) {
        super(context);
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        paint.setShader(new BitmapShader(background, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        setWillNotDraw(false);

        player = new Player(context, sizeX, sizeY);


        if (gyroscopeHandling) {
            sensorManager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
            gyroscoppSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(this, gyroscoppSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        
        dirts = new Dirt[10];
        for(int i = 0; i < dirts.length; i++) {
            dirts[i] = new Dirt(context, sizeX, sizeY);
        }

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(paint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(player.getPlayer(), player.getX(), player.getY(), paint);

        for(int i = 0; i < dirts.length; i++) {
            canvas.drawBitmap(dirts[i].getDirt(), dirts[i].getX(), dirts[i].getY(), paint);
        }
    }

    public void update() {
        if(move == "left") {
            player.moveLeft();
        } else if (move == "right") {
            player.moveRight();
        }

        for(int i = 0; i < dirts.length; i++) {
            dirts[i].moveDirt();
        }
        checkCollide();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                float xT = event.getX();
                if(xT > getWidth() / 2) {
                    Log.d("Pohyb:", "right");
                    move = "right";
                    return true;
                } else {
                    Log.d("Pohyb:", "left");
                    move = "left";
                    return true;
                }
            }

            case MotionEvent.ACTION_UP: {
                Log.d("Pohyb:", "nahoru");
                move = "none";
                return true;

            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean gyroscopeHandling = false;
        if(gyroscopeHandling) {
            //Log.d("Pohyb:", "pohyb");
            if(event.values[1] < -1.5) {
                Log.d("Pohyb:", "vlevo");
                move = "left";
            } else if (event.values[1] > 1.5) {
                Log.d("Pohyb:", "vpravo");
                move = "right";
            } else {
                Log.d("Pohyb:", "rovne");
                move = "none";
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    private double calcDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    public void checkCollide() {
        for (int i = 0; i < dirts.length; i++){
            if(dirts[i].getDirtSize()/2 + player.getPlayerSize()/2 > calcDistance(player.getX() + 49, dirts[i].getX() + 15, player.getY() + 37, dirts[i].getY() + 15)){
                dirts[i].setNewPosition();
                //playerHit();
            }
        }
    }

    private void playerHit() {
        thread.setRunning(false);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
