package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Heart {
    private Bitmap heart;

    private float posX;
    private float posY;
    private int width;
    private int height;
    private int heartSize;
    private int speed;

    Random randomPositionGenerator;

    public Heart(Context context, int sizeX, int sizeY) {
        randomPositionGenerator = new Random();
        posX = randomPositionGenerator.nextInt(sizeX - heartSize);
        posY = -randomPositionGenerator.nextInt(1200);
        width = sizeX;
        height = sizeY;
        heartSize = 45;

        randomPositionGenerator = new Random();

        heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        heart = Bitmap.createScaledBitmap(heart, heartSize, heartSize, false);
    }

    public Bitmap getHeart() {return heart;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public int getHeartSize() {return heartSize;}
    public void moveHeart() {
        if (posY >= height) {
            posY = -randomPositionGenerator.nextInt(15200);
        } else {
            posY += 5;
        }
    }
    public void setNewPosition() {
        posX = randomPositionGenerator.nextInt(width - heartSize);
        posY = -randomPositionGenerator.nextInt(1200);
    }
}
