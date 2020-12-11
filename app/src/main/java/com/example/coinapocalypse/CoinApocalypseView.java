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
import android.view.View;

import java.io.Console;

public class CoinApocalypseView extends View implements Runnable{
    private Canvas canvas;
    private Player player;

    private Paint paint;
    private Bitmap background;

    private boolean running = true;
    private boolean movingLeft = false;
    private boolean movingRight = false;

    private boolean touch = false;

    public CoinApocalypseView(Context context, int sizeX, int sizeY) {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        paint.setShader(new BitmapShader(background, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        setWillNotDraw(false);

        player = new Player(context, sizeX, sizeY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(paint);
        canvas.drawBitmap(player.getPlayer(), player.getX(), player.getY(), paint);
    }

    @Override
    public void run() {
        while (running){
            update();
        }
    }

    private void update() {
        if(movingLeft == true) {
            player.moveLeft();
        }

        if(movingRight == true) {
            player.moveRight();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if(touch = false) {
                    float xT = event.getX();

                    if (xT > getHeight() / 2){
                        movingRight = true;
                        Log.d("Move", "Down");
                    } else {
                        movingLeft = true;
                    }
                }
            }
            case MotionEvent.ACTION_UP: {
                if(touch = true) {
                    float xT = event.getX();

                    if (xT > getHeight() / 2){
                        movingRight = false;
                        Log.d("Move", "Up");
                    } else {
                        movingLeft = false;
                    }
                }
                touch = !touch;
            }
        }
        return super.onTouchEvent(event);
    }
}
