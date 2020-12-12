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

public class CoinApocalypseView extends SurfaceView implements SurfaceHolder.Callback {
    private Canvas canvas;
    private Player player;
    private MainThread thread;

    private Paint paint;
    private Bitmap background;

    private Dirt[] dirts;

    private String move = "none";

    private boolean dirtDone = false;

    public CoinApocalypseView(Context context, int sizeX, int sizeY) {
        super(context);
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        paint.setShader(new BitmapShader(background, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        setWillNotDraw(false);

        player = new Player(context, sizeX, sizeY);

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
