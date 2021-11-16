package com.example.moondoor;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {
    
    private final static String HOSTNAME = "tcp://34.134.1.73:1883";
    private final static String USERNAME = "arson";
    private final static String PASSWORD = "s4m0NoOO_K!t3dz";
    
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
        TextView acc = findViewById(R.id.acc);
        TextView log = findViewById(R.id.log);
        
        String clientId = MqttClient.generateClientId();

        client = new MqttAndroidClient(this.getApplicationContext(),
                HOSTNAME,
                clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    sub();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            client.connect();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                },0,2000);
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String text0 = new String(message.getPayload());
                if(text0.matches("alert")) {
                    textView.setText(new String(message.getPayload()));
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("WARNING, door opens without verification")
                        .setContentText("Please check your home condition.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    notificationManager.notify(1, builder.build());
                } else {
                    String[] arr_data = text0.split("/");
                    if(arr_data[0].matches("RDA0MTE3MTMyNi9BcnNvbiBNYXJpYW51cw")) {
                        acc.setText(arr_data[2]);
                        log.setText(arr_data[3]);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        
        
        key_btn.setOnClickListener(view -> {
            pub();
            key_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_btn_clicked));
            open_door.start();
            new Handler().postDelayed(() -> {
                key_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_btn));
                close_door.start();
            }, 3000);
        });

        act_log.setOnClickListener(view-> {
            SweepUp sweepUp = new SweepUp();
            sweepUp.show(getSupportFragmentManager(), sweepUp.getTag());
        });
    }
    public void pub() {
        String topic = "Skripsi/D041171326/Status";
        String payload = "RDA0MTE3MTMyNi9BcnNvbiBNYXJpYW51cw/opendoor=on";
        byte[] encodedPayload;
        try {
            encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void sub() {
        String topic = "Skripsi/D041171326/Alert";
        String topic1 = "Skripsi/D041171326/Status";
        int qos = 0;
        try {
            client.subscribe(topic, qos);
            client.subscribe(topic1, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
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
