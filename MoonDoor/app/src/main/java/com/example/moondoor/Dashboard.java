package com.example.moondoor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Dashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("ACCESS_TOKEN", MODE_PRIVATE);
        RelativeLayout key_btn = findViewById(R.id.btn_control);
        TextView user_name = findViewById(R.id.user_name);
        Button act_log = findViewById(R.id.act_btn);
        ImageView btn_menu = findViewById(R.id.btn_menu);
        user_name.setText(sharedPreferences.getString("fullname",""));
        MediaPlayer open_door = MediaPlayer.create(this, R.raw.open_door);
        MediaPlayer close_door = MediaPlayer.create(this, R.raw.closing_door);
        key_btn.setOnClickListener(view -> {
            key_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_btn_clicked));
            open_door.start();
            new Handler().postDelayed(() -> {
                key_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_btn));
                close_door.start();
            }, 3000);
        });
        btn_menu.setOnClickListener(view-> {
            Menu menu = new Menu();
            menu.show(getSupportFragmentManager(), menu.getTag());
        });

        act_log.setOnClickListener(view-> {
            SweepUp sweepUp = new SweepUp();
            sweepUp.show(getSupportFragmentManager(), sweepUp.getTag());
        });
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

    @SuppressLint("NonConstantResourceId")
    public void clickFunc(View v) {
        MenuHandler menuHandler;
        switch (v.getId()) {
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Dashboard.this.finish();
                            }
                        })
                        .show();
                break;
            case R.id.change_pin:
                menuHandler = new MenuHandler("change_pin");
                menuHandler.show(getSupportFragmentManager(), menuHandler.getTag());
                break;
            case R.id.change_password:
                menuHandler = new MenuHandler("change_password");
                menuHandler.show(getSupportFragmentManager(), menuHandler.getTag());
                break;
            case R.id.show_pin:
                menuHandler = new MenuHandler("show_pin");
                menuHandler.show(getSupportFragmentManager(), menuHandler.getTag());
                break;
        }
    }
    public void btn_handler(View v) {
        switch(v.getId()) {
            case R.id.btn_change_pin:
                Toast.makeText(this, "Hooolaaaa", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}