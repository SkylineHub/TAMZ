package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Coin {
    private Bitmap coin;

    private float posX;
    private float posY;
    private int width;
    private int height;
    private int coinSize;
    private int coins = 0;

    Random randomPositionGenerator;

    public Coin(Context context, int sizeX, int sizeY) {
        randomPositionGenerator = new Random();
        posX = randomPositionGenerator.nextInt(sizeX - coinSize);
        posY = -randomPositionGenerator.nextInt(1200);
        width = sizeX;
        height = sizeY;
        coinSize = 25;

        randomPositionGenerator = new Random();

        coin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        coin = Bitmap.createScaledBitmap(coin, coinSize, coinSize, false);
    }

    public Bitmap getCoin() {return coin;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public int getCoinSize() {return coinSize;}
    public void moveCoin() {
        if (posY >= height) {
            posY = -randomPositionGenerator.nextInt(2200);
        } else {
            posY += 4;
        }
    }
    public void setNewPosition() {
        posX = randomPositionGenerator.nextInt(width - coinSize);
        posY = -randomPositionGenerator.nextInt(1200);
    }
    public void addCoin() {coins++;};
    public int getCoins() {return coins;}
}
