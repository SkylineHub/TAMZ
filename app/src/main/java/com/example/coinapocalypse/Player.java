package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.BitSet;

public class Player {
    private Bitmap player;
    private float posX;
    private float posY;


    public Player(Context context, int sizeX, int sizeY) {
        posX = sizeX/2 - 50;
        posY = sizeY - 200;

        player = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        player = Bitmap.createScaledBitmap(player, 100, 100, false);
    }

    public Bitmap getPlayer() {return player;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public void moveLeft() {posX -= 5;};
    public void moveRight() {posX += 5;};
}
