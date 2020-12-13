package com.example.coinapocalypse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CoinBar {
    private Bitmap coin;

    private float posX;
    private float posY;
    private int coinSize;

    public CoinBar(Context context) {
        posX = 50;
        posY = 125;
        coinSize = 50;

        coin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        coin = Bitmap.createScaledBitmap(coin, coinSize, coinSize, false);
    }

    public Bitmap getCoinBar() {return coin;}
    public float getX() {return posX;}
    public float getY() {return posY;}
    public int getCoinSize() {return coinSize;}

}
