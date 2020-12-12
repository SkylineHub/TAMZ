package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.BitSet;

public class Player {
    private Bitmap player;
    private float posX;
    private float posY;
    private int width;
    private int playerSize;


    public Player(Context context, int sizeX, int sizeY) {
        posX = sizeX/2 - 50;
        posY = sizeY - 200;
        width = sizeX;
        playerSize = 100;

        player = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        player = Bitmap.createScaledBitmap(player, playerSize, playerSize, false);
    }

    public Bitmap getPlayer() {return player;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public int getPlayerSize() {return playerSize;}
    public void moveLeft() {
        if(posX >= 0) {
            posX -= 5;
        }
    };
    public void moveRight() {
        if(posX <= width - playerSize) {
            posX += 5;
        }
    };
}
