package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.Console;

public class CoinApocalypseView extends SurfaceView implements SurfaceHolder.Callback {
    private Canvas canvas;
    private Player player;
    private MainThread thread;

    private Paint paint;
    private Bitmap background;

    private boolean running = true;
    private boolean movingLeft = false;
    private boolean movingRight = false;

    private boolean touch = false;

    private String move = "none";

    public CoinApocalypseView(Context context, int sizeX, int sizeY) {
        super(context);
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        paint.setShader(new BitmapShader(background, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        setWillNotDraw(false);

        player = new Player(context, sizeX, sizeY);

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
    }

    public void update() {
        if(move == "left") {
            player.moveLeft();
        } else if (move == "right") {
            player.moveRight();
        }
        Log.d("Auto", "auto");
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
