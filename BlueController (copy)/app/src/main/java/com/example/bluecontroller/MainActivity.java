package com.example.bluecontroller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private ProgressDialog dialog;
    private BluetoothAdapter bluetoothAdapter;
    private int counter = 0;
    private boolean func1_val = false,
            func2_val = false,
            func3_val = false;
    private ImageView arrow;
    private AlertDialog pairedDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!bluetoothAdapter.isEnabled()) {
            //Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //askBluePermission.launch(intent);
           askBluetoothPermission();
        }

        dialog = new ProgressDialog(this);
        arrow = findViewById(R.id.arrow);
        arrow.setVisibility(View.INVISIBLE);

        SeekBar seekBar = findViewById(R.id.seekbar);
        TextView seekBarVal = findViewById(R.id.seekbarval);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarVal.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageView btn_top = findViewById(R.id.btn_top);
        ImageView btn_bottom = findViewById(R.id.btn_bottom);
        ImageView btn_left = findViewById(R.id.btn_left);
        ImageView btn_right = findViewById(R.id.btn_right);

        touched(btn_top);
        touched(btn_bottom);
        touched(btn_left);
        touched(btn_right);

        funcButton(findViewById(R.id.func1));
        funcButton(findViewById(R.id.func2));
        funcButton(findViewById(R.id.func3));

        ImageView btn = findViewById(R.id.config);
        btn.setOnClickListener(view -> {
            Intent innt = new Intent(MainActivity.this, DevicesMenu.class);
            startActivity(innt);
        });
        /*
        btn.setOnClickListener(view->{
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            View dview = getLayoutInflater().inflate(R.layout.menu, null);
            dlg.setView(dview);
            AlertDialog menu = dlg.create();
            menu.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            menu.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
            menu.show();
            menu.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }); */

        IntentFilter recv = new IntentFilter();
        recv.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        recv.addAction(BluetoothDevice.ACTION_FOUND);
        recv.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        recv.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(registerRecv, recv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(registerRecv);
    }
    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    private void touched(ImageView iv) {
        iv.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                switch (iv.getId()) {
                    case R.id.btn_top:
                        counter += 1;
                        break;
                    case R.id.btn_bottom:
                        counter += 2;
                        break;
                    case R.id.btn_right:
                        counter += 5;
                        break;
                    case R.id.btn_left:
                        counter += 9;
                        break;
                }
                if(counter == 1
                        || counter == 2
                        || counter == 5
                        || counter == 9
                        || counter == 6
                        || counter == 10
                        || counter == 7
                        || counter == 11) {
                    iv.setColorFilter(Color.argb(255, 255, 0, 0));
                    changeRotation();
                }
                else {
                    if(counter != 0) {
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(250);
                    }
                }
            }
            else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                switch (iv.getId()) {
                    case R.id.btn_top:
                        counter -= 1;
                        break;
                    case R.id.btn_bottom:
                        counter -= 2;
                        break;
                    case R.id.btn_right:
                        counter -= 5;
                        break;
                    case R.id.btn_left:
                        counter -= 9;
                        break;
                }
                iv.clearColorFilter();
                changeRotation();
            }
            return true;
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void funcButton(Button btn) {
        btn.setOnClickListener(view->{
            switch (btn.getId()) {
                case R.id.func1:
                    if(!func1_val) {
                        func1_val = true;
                        btn.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 255, 255, 0)));
                        btn.setTextColor(getResources().getColor(R.color.black));
                    }
                    else {
                        func1_val = false;
                        btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
                        btn.setTextColor(getResources().getColor(R.color.white));
                    }
                    break;
                case R.id.func2:
                    if(!func2_val) {
                        func2_val = true;
                        btn.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 255, 255, 0)));
                        btn.setTextColor(getResources().getColor(R.color.black));
                    }
                    else {
                        func2_val = false;
                        btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
                        btn.setTextColor(getResources().getColor(R.color.white));
                    }
                    break;
                case R.id.func3:
                    if(!func3_val) {
                        func3_val = true;
                        btn.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 255, 255, 0)));
                        btn.setTextColor(getResources().getColor(R.color.black));
                    }
                    else {
                        func3_val = false;
                        btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_700)));
                        btn.setTextColor(getResources().getColor(R.color.white));
                    }
                    break;
            }
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

    private final BroadcastReceiver registerRecv = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    devices.clear();
                    dialog.show();
                    dialog.setContentView(R.layout.progress_bar);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    dialog.dismiss();
                    devices.forEach((dev) -> System.out.println("Name : " + dev.getName() + ", Addr : " + dev.getAddress()));
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    break;
            }
        }
    };
//    private final ActivityResultLauncher<Intent> askBluePermission = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if(result.getResultCode() != -1) {
//                    setText("Bluetooth needed to be enabled!");
//                }
//            }
//    );
    private void askBluetoothPermission() {
         AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Please enable bluetooth to use application.");
            builder.setTitle("Bluetooth");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                bluetoothAdapter.enable();
                setText("Enabling bluetooth...");
                Handler handler = new Handler();
                handler.postDelayed(() -> setText("Bluetooth enable successfully."), 1500);
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.cancel();
                setText("Bluetooth needed to be enabled!");
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            alertDialog.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
            alertDialog.show();
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void setText(String val) {
        Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();
    }
    private void changeRotation() {
        switch (counter) {
            case 1:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(270).alpha(1).setDuration(50);
                break;
            case 2:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(90).alpha(1).setDuration(50);
                break;
            case 5:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(0).alpha(1).setDuration(50);
                break;
            case 9:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(180).alpha(1).setDuration(50);
                break;
            case 6:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(315).alpha(1).setDuration(50);
                break;
            case 10:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(225).alpha(1).setDuration(50);
                break;
            case 7:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(45).alpha(1).setDuration(50);
                break;
            case 11:
                arrow.setVisibility(View.VISIBLE);
                arrow.animate().rotation(135).alpha(1).setDuration(50);
                break;
            default:
                arrow.animate().alpha(0).setDuration(1000);
                break;
        }
    }
    @SuppressLint("NonConstantResourceId")
    public void handleMenu(View v) {
        switch (v.getId()) {
            case R.id.menu1:
                if(!bluetoothAdapter.isEnabled()) {
                    //Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    //askBluePermission.launch(intent);
                    askBluetoothPermission();
                }
                else {
                    //Intent intent2 = new Intent(getApplicationContext(), PairedDevicesMenu.class);
                   // startActivity(intent2);
//                    PairedDevicesMenu pairedDevicesMenu = new PairedDevicesMenu();
//                    pairedDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//                    pairedDialog.getWindow().getDecorView()
//                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
//                    pairedDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                }
                break;
            case R.id.menu2:
                setText("Menu 2");
                break;
            case R.id.menu3:
                setText("Menu 3");
                break;
            case R.id.btnClosePaired:
                pairedDialog.dismiss();
                break;
            case R.id.scanblth:
                cstm_cav();
                if(bluetoothAdapter.isDiscovering())
                    bluetoothAdapter.cancelDiscovery();
                bluetoothAdapter.startDiscovery();
                break;
        }
    }
    private void cstm_cav() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int pc;
            pc = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    + this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (pc != 0) {
                this.requestPermissions(new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        9871
                );
            }
        }
    }
}