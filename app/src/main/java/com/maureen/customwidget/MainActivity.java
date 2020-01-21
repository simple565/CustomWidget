package com.maureen.customwidget;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircularProgressBar circularProgressBar = findViewById(R.id.sesame_view);
        circularProgressBar.setMatchNumText("12");
        circularProgressBar.setWaveColor(ContextCompat.getColor(this, R.color.colorAccent));
        circularProgressBar.setWaveWidth(16);
        circularProgressBar.setInterpolator(new LinearOutSlowInInterpolator());
        circularProgressBar.start();
        circularProgressBar.setCenterColor(R.color.colorAccent);
        circularProgressBar.setBackgroundArcColor(R.color.colorPrimary);
    }
}
