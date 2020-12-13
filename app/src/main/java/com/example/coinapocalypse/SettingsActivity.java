package com.example.coinapocalypse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    public static final String myPreferences = "preferences";
    SharedPreferences sharedpreferences;
    TextView name;
    Switch gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name = findViewById(R.id.editTextNick);
        gyroscope = findViewById(R.id.switchGyroscope);

        sharedpreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        name.setText(sharedpreferences.getString("nick", "Player"));
        gyroscope.setChecked(sharedpreferences.getBoolean("gyroscope", false));

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sharedpreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("nick", name.getText().toString());
                editor.putBoolean("gyroscope", gyroscope.isChecked());
                editor.commit();
            }
        });
        Button buttonScore = findViewById(R.id.buttonBack2);
        buttonScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}