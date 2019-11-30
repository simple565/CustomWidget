package com.maureen.customwidget;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

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
        circularProgressBar.setDuration(5000);
        circularProgressBar.setStyle(Paint.Style.FILL);
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        circularProgressBar.setInterpolator(new LinearOutSlowInInterpolator());
        circularProgressBar.setMaxRadius(500);
        circularProgressBar.start();
        circularProgressBar.setCenterColor(R.color.colorAccent);
        circularProgressBar.setBackgroundArcColor(R.color.colorPrimary);
    }
}
