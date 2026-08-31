package com.vicdron.rockpaperscissors;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.vicdron.rockpaperscissors.databinding.JokenPow7ActivityBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JokenPow7Activity extends AppCompatActivity {

    private JokenPow7ActivityBinding binding;
    private int playerScore = 0;
    private int cpuScore = 0;
    private int winThreshold = 2; // Default
    private final String[] options = {"rock", "paper", "scissors", "lizard", "spock", "wolf", "sponge"};
    private final Map<String, Integer> spriteMap = new HashMap<>();
    private static final String PREFS_NAME = "GameStats";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = JokenPow7ActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        winThreshold = getIntent().getIntExtra("winThreshold", 2);
        AdManager.loadNativeAd(this, binding.nativeAdContainer);

        // Sprite mapping (using modern vector icons)
        spriteMap.put("rock", R.drawable.ic_rock);
        spriteMap.put("paper", R.drawable.ic_paper);
        spriteMap.put("scissors", R.drawable.ic_scissors);
        spriteMap.put("lizard", R.drawable.ic_lizard);
        spriteMap.put("spock", R.drawable.ic_spock);
        spriteMap.put("wolf", R.drawable.ic_wolf);
        spriteMap.put("sponge", R.drawable.ic_sponge);

        // Listeners
        binding.btnRock.setOnClickListener(v -> startRound("rock"));
        binding.btnPaper.setOnClickListener(v -> startRound("paper"));
        binding.btnScissors.setOnClickListener(v -> startRound("scissors"));
        binding.btnLizard.setOnClickListener(v -> startRound("lizard"));
        binding.btnSpock.setOnClickListener(v -> startRound("spock"));
        binding.btnWolf.setOnClickListener(v -> startRound("wolf"));
        binding.btnSponge.setOnClickListener(v -> startRound("sponge"));

        binding.bntBack.setOnClickListener(v -> showExitDialog());
        binding.btnRestart.setOnClickListener(v -> restartGame());

        setupButtonTouchAnimation(binding.btnRock);
        setupButtonTouchAnimation(binding.btnPaper);
        setupButtonTouchAnimation(binding.btnScissors);
        setupButtonTouchAnimation(binding.btnLizard);
        setupButtonTouchAnimation(binding.btnSpock);
        setupButtonTouchAnimation(binding.btnWolf);
        setupButtonTouchAnimation(binding.btnSponge);
        setupButtonTouchAnimation(binding.bntBack);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });

        updateScore();
    }

    private void restartGame() {
        playerScore = 0;
        cpuScore = 0;
        updateScore();
        binding.btnRestart.setVisibility(View.GONE);
        setButtonsEnabled(true);
    }

    private void showExitDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupButtonTouchAnimation(View button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }

    public void startRound(String playerChoice) {
        binding.user.setImageResource(spriteMap.get("rock"));
        binding.cpu.setImageResource(spriteMap.get("rock"));
        
        setButtonsEnabled(false);

        animateHands(() -> {
            String cpuChoice = options[new Random().nextInt(options.length)];

            binding.user.setImageResource(spriteMap.get(playerChoice));
            binding.cpu.setImageResource(spriteMap.get(cpuChoice));

            String result = getWinner(playerChoice, cpuChoice);

            if (result.equals("You won!")) {
                playerScore++;
                updateScore();
                showAnimatedResult(getString(R.string.result_won));
                performHapticFeedback(true);
            } else if (result.equals("You lost!")) {
                cpuScore++;
                updateScore();
                showAnimatedResult(getString(R.string.result_lost));
                performHapticFeedback(false);
            } else {
                showAnimatedResult(getString(R.string.result_draw));
                binding.getRoot().performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }

            if (playerScore >= winThreshold || cpuScore >= winThreshold) {
                boolean playerWonMatch = playerScore >= winThreshold;
                saveMatchResult(playerWonMatch);

                String finalWinner = playerWonMatch ?
                        getString(R.string.match_won) :
                        getString(R.string.match_lost);

                new Handler().postDelayed(() -> {
                    showAnimatedResult(finalWinner);
                    AdManager.showInterstitialWithFrequency(JokenPow7Activity.this);
                    binding.btnRestart.setVisibility(View.VISIBLE);
                }, 1500);
            } else {
                new Handler().postDelayed(() -> setButtonsEnabled(true), 1500);
            }
        });
    }

    private void setButtonsEnabled(boolean enabled) {
        binding.btnRock.setEnabled(enabled);
        binding.btnPaper.setEnabled(enabled);
        binding.btnScissors.setEnabled(enabled);
        binding.btnLizard.setEnabled(enabled);
        binding.btnSpock.setEnabled(enabled);
        binding.btnWolf.setEnabled(enabled);
        binding.btnSponge.setEnabled(enabled);
    }

    private void performHapticFeedback(boolean isWin) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            binding.getRoot().performHapticFeedback(isWin ? HapticFeedbackConstants.CONFIRM : HapticFeedbackConstants.REJECT);
        } else {
            binding.getRoot().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
    }

    private void saveMatchResult(boolean playerWon) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (playerWon) {
            int wins = prefs.getInt("total_wins", 0);
            editor.putInt("total_wins", wins + 1);
        } else {
            int losses = prefs.getInt("total_losses", 0);
            editor.putInt("total_losses", losses + 1);
        }
        editor.apply();
    }

    public void showAnimatedResult(String message) {
        binding.resultado.setText(message);
        binding.cardResult.setVisibility(View.VISIBLE);
        binding.cardResult.setAlpha(0f);
        binding.cardResult.setScaleX(0.5f);
        binding.cardResult.setScaleY(0.5f);

        binding.cardResult.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .withEndAction(() -> binding.cardResult.animate()
                        .alpha(0f)
                        .scaleX(0.8f)
                        .scaleY(0.8f)
                        .setDuration(300)
                        .setStartDelay(1000)
                        .withEndAction(() -> binding.cardResult.setVisibility(View.INVISIBLE))
                        .start())
                .start();
    }

    public void animateHands(Runnable onEnd) {
        ObjectAnimator playerAnim = ObjectAnimator.ofFloat(binding.user, "translationY", 0f, -60f, 0f);
        ObjectAnimator cpuAnim = ObjectAnimator.ofFloat(binding.cpu, "translationY", 0f, -60f, 0f);

        playerAnim.setRepeatCount(2);
        cpuAnim.setRepeatCount(2);
        playerAnim.setDuration(300);
        cpuAnim.setDuration(300);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(playerAnim, cpuAnim);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onEnd.run();
            }
        });
    }

    public String getWinner(String player, String cpu) {
        if (player.equals(cpu)) return "Draw!";

        switch (player) {
            case "rock":
                if (cpu.equals("scissors") || cpu.equals("lizard") || cpu.equals("wolf")) return "You won!";
                break;
            case "paper":
                if (cpu.equals("rock") || cpu.equals("spock") || cpu.equals("sponge")) return "You won!";
                break;
            case "scissors":
                if (cpu.equals("paper") || cpu.equals("lizard") || cpu.equals("sponge")) return "You won!";
                break;
            case "lizard":
                if (cpu.equals("paper") || cpu.equals("spock") || cpu.equals("wolf")) return "You won!";
                break;
            case "spock":
                if (cpu.equals("scissors") || cpu.equals("rock") || cpu.equals("wolf")) return "You won!";
                break;
            case "wolf":
                if (cpu.equals("paper") || cpu.equals("scissors") || cpu.equals("sponge")) return "You won!";
                break;
            case "sponge":
                if (cpu.equals("rock") || cpu.equals("lizard") || cpu.equals("spock")) return "You won!";
                break;
        }
        return "You lost!";
    }

    private void updateScore() {
        binding.score.setText(getString(R.string.scoreboard_format, playerScore, cpuScore));
    }
}