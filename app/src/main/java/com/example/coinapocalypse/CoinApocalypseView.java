package com.example.coinapocalypse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.media.MediaPlayer;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.content.Context.SENSOR_SERVICE;

public class CoinApocalypseView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private Canvas canvas;
    private Player player;
    private MainThread thread;

    private DBHelper mydb;

    private MediaPlayer mediaPlayer;

    private Paint paint;
    private Bitmap background;
    private Bitmap pause;

    private SensorManager sensorManager;
    private Sensor gyroscoppSensor;

    private GameActivity gameActivity;

    private int width;
    private int height;
    private int heartX;

    private Dirt[] dirts;
    private Stone[] stones;
    private Coin coin;
    private Heart heart;
    private HeartBar[] heartBar;
    private CoinBar coinBar;
    private int coins = 0;

    private boolean running = true;

    private String move = "none";
    private String playerNick;

    TextPaint textPaint;

    private boolean gyroscopeHandling;

    public CoinApocalypseView(Context context, int sizeX, int sizeY, GameActivity myActivity, String nick, Boolean gyroscope) {
        super(context);
        getHolder().addCallback(this);

        gameActivity = myActivity;

        mydb = new DBHelper(context);

        playerNick = nick;
        gyroscopeHandling = gyroscope;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        paint.setShader(new BitmapShader(background, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        setWillNotDraw(false);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(24 * getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFFFFFFFF);

        pause = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
        pause = Bitmap.createScaledBitmap(pause, 50, 50, false);

        width = sizeX;
        height = sizeY;

        if (gyroscopeHandling) {
            sensorManager = (SensorManager)context.getSystemService(SENSOR_SERVICE);
            gyroscoppSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, gyroscoppSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        player = new Player(context, sizeX, sizeY);

        heart = new Heart(context, sizeX, sizeY);

        heartBar = new HeartBar[3];
        heartX = 50;
        for(int i = 0; i < heartBar.length; i++) {
            Log.d("New object", "object" + i);
            heartBar[i] = new HeartBar(context, heartX);
            heartX += 75;
        }

        coin = new Coin(context, sizeX, sizeY);

        coinBar = new CoinBar(context);

        dirts = new Dirt[6];
        for(int i = 0; i < dirts.length; i++) {
            dirts[i] = new Dirt(context, sizeX, sizeY);
        }

        stones = new Stone[4];
        for(int i = 0; i < stones.length; i++) {
            stones[i] = new Stone(context, sizeX, sizeY);
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

        canvas.drawBitmap(pause, width - 100, 50, paint);

        canvas.drawBitmap(player.getPlayer(), player.getX(), player.getY(), paint);

        canvas.drawBitmap(coin.getCoin(), coin.getX(), coin.getY(), paint);

        canvas.drawBitmap(coinBar.getCoinBar(), coinBar.getX(), coinBar.getY(), paint);

        canvas.drawBitmap(heart.getHeart(), heart.getX(), heart.getY(), paint);

        for(int i = 0; i < player.getLives(); i++) {
            canvas.drawBitmap(heartBar[i].getHeartBar(), heartBar[i].getX(), heartBar[i].getY(), paint);
        }

        for(int i = 0; i < dirts.length; i++) {
            canvas.drawBitmap(dirts[i].getDirt(), dirts[i].getX(), dirts[i].getY(), paint);
        }

        for(int i = 0; i < stones.length; i++) {
            canvas.drawBitmap(stones[i].getStone(), stones[i].getX(), stones[i].getY(), paint);
        }

        coins = coin.getCoins();
        int width = (int) textPaint.measureText(String.valueOf(coins));
        StaticLayout staticLayout = new StaticLayout(String.valueOf(coins), textPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate(125, 120);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    public void update() {
        if(move == "left") {
            player.moveLeft();
        } else if (move == "right") {
            player.moveRight();
        }

        coin.moveCoin();

        if(player.getLives() < 3) {
            heart.moveHeart();
        }

        for(int i = 0; i < dirts.length; i++) {
            dirts[i].moveDirt();
        }

        for(int i = 0; i < stones.length; i++) {
            stones[i].moveStone();
        }

        checkCollide();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                float xT = event.getX();
                float yT = event.getY();
                Log.d("X", String.valueOf(xT));
                Log.d("Y", String.valueOf(yT));
                if(xT > width - 100 && xT < width - 50 && yT > 50 && yT < 100) {
                    Log.d("Pause", "pause");
                    if(running) {
                        thread.onPause();
                        running = !running;
                        Log.d("Pause", String.valueOf(running));
                        return true;
                    } else if (!running) {
                        //thread.setRunning(true);
                        //thread.run();
                        thread.onResume();
                        running = !running;
                        Log.d("Pause", String.valueOf(running));
                        return true;
                    }
                }
                if(xT > getWidth() / 2) {
                    //Log.d("Pohyb:", "right");
                    move = "right";
                    return true;
                } else {
                    //Log.d("Pohyb:", "left");
                    move = "left";
                    return true;
                }
            }

            case MotionEvent.ACTION_UP: {
                //Log.d("Pohyb:", "nahoru");
                move = "none";
                return true;

            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(gyroscopeHandling) {
            //Log.d("Pohyb:", "pohyb");
            if(event.values[0] < -1.0) {
                Log.d("Pohyb:", "vlevo");
                move = "right";
            } else if (event.values[0] > 1.0) {
                Log.d("Pohyb:", "vpravo");
                move = "left";
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
        if(coin.getCoinSize()/2 + player.getPlayerSize()/2 > calcDistance(player.getX() + 49, coin.getX() + 15, player.getY() + 37, coin.getY() + 15)){
            coin.setNewPosition();
            coin.addCoin();
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.coin);
            mediaPlayer.start();
        }

        if(heart.getHeartSize()/2 + player.getPlayerSize()/2 > calcDistance(player.getX() + 49, heart.getX() + 15, player.getY() + 37, heart.getY() + 15)){
            heart.setNewPosition();
            player.addLive();
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.heal);
            mediaPlayer.start();
        }

        for (int i = 0; i < dirts.length; i++){
            if(dirts[i].getDirtSize()/2 + player.getPlayerSize()/2 > calcDistance(player.getX() + 49, dirts[i].getX() + 15, player.getY() + 37, dirts[i].getY() + 15)){
                dirts[i].setNewPosition();
                playerHit();
            }
        }

        for (int i = 0; i < stones.length; i++){
            if(stones[i].getStoneSize()/2 + player.getPlayerSize()/2 > calcDistance(player.getX() + 49, stones[i].getX() + 15, player.getY() + 37, stones[i].getY() + 15)){
                stones[i].setNewPosition();
                playerHit();
            }
        }
    }

    private void playerHit() {

        //thread.setRunning(false);
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.hit);
        mediaPlayer.start();

        player.removeLive();
        Log.d("Zivoty", String.valueOf(player.getLives()));
        if(player.getLives() == 0) {
            mydb.insertItem(playerNick, coin.getCoins());
            thread.setRunning(false);
            gameActivity.backToMenu();
        }
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
