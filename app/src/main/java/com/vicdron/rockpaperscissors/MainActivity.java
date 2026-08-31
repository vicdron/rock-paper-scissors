package com.vicdron.rockpaperscissors;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.vicdron.rockpaperscissors.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private static final String PREFS_NAME = "GameStats";
    private int winThreshold = 2; // Default best of 3

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdManager.loadNativeAd(this, binding.nativeAdContainer);
        AdManager.loadInterstitial(this);

        binding.btnBest3.setOnClickListener(v -> winThreshold = 2);
        binding.btnBest5.setOnClickListener(v -> winThreshold = 3);

        binding.cardClassic.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, JokenPowActivity.class);
            i.putExtra("winThreshold", winThreshold);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.cardSpock.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, JokenPowSpockActivity.class);
            i.putExtra("winThreshold", winThreshold);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.card7.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, JokenPow7Activity.class);
            i.putExtra("winThreshold", winThreshold);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        setupTouchAnimation(binding.cardClassic);
        setupTouchAnimation(binding.cardSpock);
        setupTouchAnimation(binding.card7);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void updateStats() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int wins = prefs.getInt("total_wins", 0);
        int losses = prefs.getInt("total_losses", 0);

        binding.txtTotalWins.setText(getString(R.string.total_wins, wins));
        binding.txtTotalLosses.setText(getString(R.string.total_losses, losses));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupTouchAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.98f).scaleY(0.98f).alpha(0.9f).setDuration(100).start();
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