package com.example.moondoor;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class MenuHandler extends BottomSheetDialogFragment {
    protected String id;
    public MenuHandler(String id) {
        this.id = id;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = null;
        switch (id) {
            case "show_pin":
                view = View.inflate(getContext(), R.layout.show_pin, null);
                Button btn_show_pin = view.findViewById(R.id.btn_show_pin);
                btn_show_pin.setOnClickListener(view1-> {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(getActivity()).getApplicationContext(), "CH_ID_0001")
                            .setSmallIcon(R.drawable.ic_home)
                            .setContentTitle("Test Title")
                            .setContentText("Text Content")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());
                });
                break;
            case "change_pin":
                view = View.inflate(getContext(), R.layout.change_pin, null);
                break;
            case "change_password":
                view = View.inflate(getContext(), R.layout.change_password, null);
                break;
            default:
                break;
        }
        dialog.getWindow()
                .getDecorView()
                    .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        dialog.getWindow()
                .setFlags(
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        dialog.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setContentView(view);

        assert view != null;
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        ImageView btn = view.findViewById(R.id.btn_close_fragment);
        btn.setOnClickListener(view1 -> dismiss());
        return dialog;
    }
}