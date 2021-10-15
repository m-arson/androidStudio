package com.example.moondoor;

import android.app.Dialog;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SweepUp extends BottomSheetDialogFragment {

    public SweepUp() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_sweep_up, null);
        dialog.getWindow()
                .setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        dialog.setContentView(view);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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