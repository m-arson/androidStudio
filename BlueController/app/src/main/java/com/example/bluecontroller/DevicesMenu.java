package com.example.bluecontroller;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class DevicesMenu extends AppCompatActivity {
    private final String UUID = "3ccf3e1a-149d-48cc-a12e-566cca04e01f";
    protected ArrayList<BluetoothDevice> devices = new ArrayList<>();
    private BluetoothAdapter bluetoothAdapter;
    private ProgressDialog dialog;
    protected ListView lv2;
    protected DevicesListAdapter devicesListAdapter2;
    protected ArrayList<Devices> scanListDevices = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_menu);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        dialog = new ProgressDialog(this);

        lv2 = findViewById(R.id.scanList);
        ImageView closeBtn = findViewById(R.id.btnClosePaired);
        Button scanBtn = findViewById(R.id.scanblth);

        closeBtn.setOnClickListener(view-> finish());
        scanBtn.setOnClickListener(view->{
            if(!bluetoothAdapter.isEnabled()) {
                askBluetoothPermission();
            } else {
                cstm_cav();
                if(bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
            }
        });

        ArrayList<Devices> arrDevices = new ArrayList<>();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0) {
            for(BluetoothDevice b: pairedDevices) {
                Devices tmp = new Devices(b.getName() ,b.getAddress());
                arrDevices.add(tmp);
            }
            ListView lv = findViewById(R.id.pairedList);
            DevicesListAdapter devicesListAdapter = new DevicesListAdapter(this, R.layout.device_list, arrDevices);
            lv.setAdapter(devicesListAdapter);
        }

        IntentFilter recv = new IntentFilter();
        recv.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        recv.addAction(BluetoothDevice.ACTION_FOUND);
        recv.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        recv.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(registerRecv, recv);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    private final BroadcastReceiver registerRecv = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    devices.clear();
                    scanListDevices.clear();
                    dialog.getWindow()
                            .setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
                    dialog.getWindow().getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
                    dialog.show();
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                    dialog.setContentView(R.layout.progress_bar);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    dialog.dismiss();
                    if(devices.size() > 0) {
                        for (BluetoothDevice dev : devices) {
                            Devices device = new Devices(dev.getName(), dev.getAddress());
                            scanListDevices.add(device);
                        }
                        devicesListAdapter2 = new DevicesListAdapter(DevicesMenu.this, R.layout.device_list, scanListDevices);
                        lv2.setAdapter(devicesListAdapter2);
                        lv2.setOnItemClickListener(adapterBluetoothClickListener);
                    }
                    else {
                        Toast.makeText(DevicesMenu.this, "No Bluetooth Found", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    break;
            }
        }
    };
    private AdapterView.OnItemClickListener adapterBluetoothClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            pairDevice(devices.get(i));
        }
    };
    private void pairDevice(BluetoothDevice bluetoothDevice) {
        try {
            Method method = bluetoothDevice.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(bluetoothDevice, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(registerRecv);
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
    private void askBluetoothPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DevicesMenu.this);
        builder.setMessage("Please enable bluetooth to use application.");
        builder.setTitle("Bluetooth");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            bluetoothAdapter.enable();
            Toast.makeText(this, "Enabling bluetooth...", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(() -> Toast.makeText(this , "Bluetooth enable successfully.", Toast.LENGTH_SHORT).show(), 1500);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.cancel();
            Toast.makeText(this, "Bluetooth needed to be enabled!", Toast.LENGTH_SHORT).show();
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
}