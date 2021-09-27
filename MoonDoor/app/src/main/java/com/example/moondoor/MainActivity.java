package com.example.moondoor;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView test_1 = findViewById(R.id.test_1);
        ImageView home = findViewById(R.id.home_logo);
        ImageView bg = findViewById(R.id.main_bg);
        int h = Resources.getSystem().getDisplayMetrics().heightPixels;
        h = (h/2) + 100;
        test_1.setAlpha(1.0f);
        home.setAlpha(1.0f);
        test_1.animate()
                .alpha(0.0f)
                .translationY(h)
                .setDuration(1000)
                .setStartDelay(3000);
        home.animate()
                .alpha(0.0f)
                .translationY(h)
                .setDuration(1000)
                .setStartDelay(3000);
        bg.animate()
                .translationY(-2260)
                .setDuration(1000)
                .setStartDelay(3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}