package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Stone {
    private Bitmap stone;

    private float posX;
    private float posY;
    private int height;
    private int stoneSize;

    Random randomPositionGenerator;

    public Stone(Context context, int sizeX, int sizeY) {
        randomPositionGenerator = new Random();
        posX = randomPositionGenerator.nextInt(sizeX - stoneSize);
        posY = -randomPositionGenerator.nextInt(1200);
        height = sizeY;
        stoneSize = 35;

        randomPositionGenerator = new Random();

        stone = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone);
        stone = Bitmap.createScaledBitmap(stone, stoneSize, stoneSize, false);
    }

    public Bitmap getStone() {return stone;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public int getStoneSize() {return stoneSize;}
    public void moveStone() {
        if (posY >= height) {
            posY = -randomPositionGenerator.nextInt(1200);
        } else {
            posY += 2;
        }
    }
    public void setNewPosition() {
        posY = -randomPositionGenerator.nextInt(1200);
    }
}
