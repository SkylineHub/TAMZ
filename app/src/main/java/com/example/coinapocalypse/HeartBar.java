package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HeartBar {
    private Bitmap heart;

    private float posX;
    private float posY;
    private int heartSize;

    public HeartBar(Context context, float x) {
        posX = x;
        posY = 50;
        heartSize = 50;

        heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        heart = Bitmap.createScaledBitmap(heart, heartSize, heartSize, false);
    }

    public Bitmap getHeartBar() {return heart;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public int getHeartSize() {return heartSize;}

}
