package com.example.moondoor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        SharedPreferences sharedPreferences = getSharedPreferences("ACCESS_TOKEN", MODE_PRIVATE);

        if(sharedPreferences.getBoolean("is_login", true) && !sharedPreferences.getString("token","").isEmpty()) {
            openDashboardIntent();
        }
        else {
            ImageView bg_caption = findViewById(R.id.bg_caption);
            TextView c_t1 = findViewById(R.id.c_t1);
            TextView c_t2 = findViewById(R.id.c_t2);
            TextView username_label = findViewById(R.id.username_label);
            TextView password_label = findViewById(R.id.password_label);
            TextView author = findViewById(R.id.author);
            EditText username_field = findViewById(R.id.username_field);
            EditText password_field = findViewById(R.id.password_field);
            Button login_btn = findViewById(R.id.login_btn);

            bg_caption.setTranslationY(-1080);
            bg_caption.setTranslationX(-1080);
            username_label.setTranslationX(-1080);
            username_field.setTranslationX(1080);
            password_label.setTranslationX(-1080);
            password_field.setTranslationX(1080);
            login_btn.setTranslationY(500);
            c_t1.setAlpha(0);
            c_t1.setScaleX(0);
            c_t1.setScaleY(0);
            c_t2.setAlpha(0);
            c_t2.setScaleX(0);
            c_t2.setScaleY(0);
            author.setScaleX(0);
            author.setScaleY(0);
            bg_caption.animate().translationY(-10).setDuration(800);
            bg_caption.animate().translationX(0).setDuration(800);
            c_t1.animate()
                    .alpha(1)
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(1000);
            c_t2.animate()
                    .alpha(1)
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(1000);
            username_label.animate()
                    .translationX(0)
                    .setDuration(1000);
            password_label.animate()
                    .translationX(0)
                    .setDuration(1000);
            username_field.animate()
                    .translationX(0)
                    .setDuration(1000);
            password_field.animate()
                    .translationX(0)
                    .setDuration(1000);
            login_btn.animate()
                    .translationY(0)
                    .setDuration(1000);
            author.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(1000);


            login_btn.setOnClickListener(view -> {
                String username = username_field.getText().toString().trim();
                String password = password_field.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    mToast("Field cannot be empty. Try Again!");
                } else {
                    String token = "736b72697073692d7331";
                    String url = "http://34.134.1.73/handle_post.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            response -> {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    int status = jsonObject.getInt("status");
                                    if (status == 200) {
                                        try {
                                            String access_token = jsonObject.getString("msg").trim();
                                            String fullname = jsonObject.getString("username").trim();
                                            SharedPreferences sharedPreferences1 = getSharedPreferences("ACCESS_TOKEN", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                                            editor.putBoolean("is_login", true);
                                            editor.putString("token", access_token);
                                            editor.putString("fullname", fullname);
                                            editor.apply();
                                            openDashboardIntent();
                                        } catch (Exception e) {
                                            mToast("Gagal Disimpan");
                                        }
                                    } else {
                                        mToast("Invalid Login. Try Again!");
                                    }
                                } catch (JSONException e) {
                                    mToast("Login Failed. Try Again!");
                                }

                            },
                            error -> mToast("Network Error. Try Again!")) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("__csrf_token", token);
                            params.put("username", username);
                            params.put("password", password);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginPage.this);
                    requestQueue.add(stringRequest);
                }
            });
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
    public void mToast(String txt) {
        Toast t = Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT);
        View v = t.getView();
        TextView tv = v.findViewById(android.R.id.message);
        v.setMinimumWidth(1080);
        v.setBackgroundResource(R.drawable.bg_red);
        tv.setTextColor(Color.parseColor("#efefef"));
        tv.setTextSize(16);
        tv.setPadding(25,25,25,25);
        t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0);
        t.show();
    }
    private void openDashboardIntent() {
        Intent intent = new Intent(LoginPage.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}
