package com.example.coinapocalypse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    private Canvas canvas;
    private Context context;

    private String nick;
    private boolean gyroscope;

    public static final String myPreferences = "preferences";
    SharedPreferences sharedpreferences;

    private CoinApocalypseView coinApocalypseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        sharedpreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        nick = sharedpreferences.getString("nick", "Player");
        gyroscope = sharedpreferences.getBoolean("gyroscope", false);

        this.context=this;
        coinApocalypseView = new CoinApocalypseView(context, size.x, size.y, this, nick, gyroscope);
        setContentView(coinApocalypseView);
        //setContentView(R.layout.activity_game);
    }

    protected void backToMenu() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}