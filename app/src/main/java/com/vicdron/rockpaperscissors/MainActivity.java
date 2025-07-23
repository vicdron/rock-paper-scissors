package com.vicdron.rockpaperscissors;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    ImageView imgJokenPowButton;
    ImageView imgJokenPowSpockButton;

    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);

        new AdRequest.Builder().build();
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());

        imgJokenPowButton = findViewById(R.id.img_jokenpow_button);
        imgJokenPowSpockButton = findViewById(R.id.img_jokenpow_spock_button);


        imgJokenPowButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, JokenPowActivity.class);
            startActivity(i);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        imgJokenPowSpockButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, JokenPowSpockActivity.class);
            startActivity(i);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        imgJokenPowButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.7f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(100).start();
                    break;
            }
            return false;
        });

        imgJokenPowSpockButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.7f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }
}