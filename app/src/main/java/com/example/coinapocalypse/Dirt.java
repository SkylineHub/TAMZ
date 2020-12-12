package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Dirt {
    private Bitmap dirt;

    private float posX;
    private float posY;
    private int height;
    private int dirtSize;

    Random randomPositionGenerator;

    public Dirt(Context context, int sizeX, int sizeY) {
        randomPositionGenerator = new Random();
        posX = randomPositionGenerator.nextInt(sizeX - dirtSize);
        posY = -randomPositionGenerator.nextInt(1200);
        height = sizeY;
        dirtSize = 25;

        randomPositionGenerator = new Random();

        dirt = BitmapFactory.decodeResource(context.getResources(), R.drawable.dirt);
        dirt = Bitmap.createScaledBitmap(dirt, dirtSize, dirtSize, false);
    }

    public Bitmap getDirt() {return dirt;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public int getDirtSize() {return dirtSize;}
    public void moveDirt() {
        if (posY >= height) {
            posY = -randomPositionGenerator.nextInt(1200);
        } else {
            posY += 3;
        }
    }
    public void setNewPosition() {
        posY = -randomPositionGenerator.nextInt(1200);
    }
}
