package com.vicdron.rockpaperscissors;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class JokenPowSpockActivity extends AppCompatActivity {

    TextView bntBack;
    private ImageView user, cpu;
    private ImageButton btnSpock;
    private TextView scoreText, resultadoText; // Renomeado para evitar conflito com método
    private int playerScore = 0;
    private int cpuScore = 0;
    private final String[] options = {"rock", "paper", "scissors", "lagarto", "spock"};
    private final Map<String, Integer> spriteMap = new HashMap<>();
    private String playerChoice;

    private InterstitialAd mInterstitialAd;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.joken_pow_spock_activity); // Certifique-se que este é o layout correto

        new AdRequest.Builder().build();
        ((AdView) findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());

        scoreText = findViewById(R.id.score);
        resultadoText = findViewById(R.id.resultado); // Inicializa o TextView do resultado
        user = findViewById(R.id.user);
        cpu = findViewById(R.id.cpu);
        ImageButton btnPedra = findViewById(R.id.btnPedra);
        ImageButton btnPapel = findViewById(R.id.btnPapel);
        ImageButton btnTesoura = findViewById(R.id.btnTesoura);
        btnSpock = findViewById(R.id.btnSpock); // Inicializa o botão Spock
        // Adicionado btnSpock e btnLizard
        ImageButton btnLizard = findViewById(R.id.btnLizard); // Inicializa o botão Lagarto
        bntBack = findViewById(R.id.bntBack);

        // Mapeamento de sprites: Adicione "lizard" e "spock"
        spriteMap.put("rock", R.drawable.rock);
        spriteMap.put("paper", R.drawable.paper);
        spriteMap.put("scissors", R.drawable.scissors);
        spriteMap.put("lagarto", R.drawable.lagarto);
        spriteMap.put("spock", R.drawable.spock);

        // Listeners para todas as 5 opções
        btnPedra.setOnClickListener(v -> startRound("rock"));
        btnPapel.setOnClickListener(v -> startRound("paper"));
        btnTesoura.setOnClickListener(v -> startRound("scissors"));
        btnSpock.setOnClickListener(v -> startRound("spock"));
        btnLizard.setOnClickListener(v -> startRound("lagarto"));

        // Botão para voltar
        bntBack.setOnClickListener(v -> {
            showInterstitialAd();
            finish();
            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);
        });

        setupButtonTouchAnimation(btnPedra);
        setupButtonTouchAnimation(btnPapel);
        setupButtonTouchAnimation(btnTesoura);
        setupButtonTouchAnimation(btnSpock);   // Adiciona animação para Spock
        setupButtonTouchAnimation(btnLizard); // Adiciona animação para Lagarto
        setupButtonTouchAnimation(bntBack); // Mantém a animação para o botão Back

        updateScore();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setupButtonTouchAnimation(View button) {
        button.setOnTouchListener((v, event) -> {
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

    public void startRound(String choice) {
        this.playerChoice = choice;

        // Define as imagens iniciais das mãos como "rock" para ambos (opcional, pode ser qualquer um)
        user.setImageResource(spriteMap.get("rock"));
        cpu.setImageResource(spriteMap.get("rock"));

        animateHands(() -> {
            // Escolha aleatória da CPU entre as 5 opções
            String cpuChoice = options[new Random().nextInt(options.length)];

            user.setImageResource(spriteMap.get(playerChoice));
            cpu.setImageResource(spriteMap.get(cpuChoice));

            String result = getWinner(playerChoice, cpuChoice);
            resultadoText.setText(result);

            if (result.equals("You won!")) {
                playerScore++;
                updateScore();
                showAnimatedResult("You won the round!");
            } else if (result.equals("You lost!")) { // Alterei a string
                cpuScore++;
                updateScore();
                showAnimatedResult("You lost the round!");
            } else { // Empate
                showAnimatedResult("Draw!");
            }
            // Lógica para verificar o vencedor final (mantida como 2 rodadas)
            if (playerScore == 2 || cpuScore == 2) {
                String finalWinner = playerScore == 2 ?
                        "Congratulations, you won the match!" :
                        "Better luck next time!";

                new Handler().postDelayed(() -> showAnimatedResult(finalWinner), 2000);

                // Reinicia a pontuação para nova partida após um pequeno atraso
                new Handler().postDelayed(() -> {
                    playerScore = 0;
                    cpuScore = 0;
                    updateScore();
                }, 2000); // 2 segundos para o usuário ver o resultado final antes de reiniciar
            }
        });
    }
    public void showAnimatedResult(String message) {
        // resultadoText já está inicializado no onCreate
        resultadoText.setText(message);
        resultadoText.setAlpha(0f);
        resultadoText.setVisibility(View.VISIBLE);
        resultadoText.setGravity(Gravity.CENTER);

        resultadoText.animate()
                .alpha(1f)
                .setDuration(250)
                .withEndAction(() -> resultadoText.animate()
                        .alpha(0f)
                        .setDuration(250)
                        .setStartDelay(500)
                        .withEndAction(() -> resultadoText.setVisibility(View.GONE))
                        .start())
                .start();
    }

    public void animateHands(Runnable onEnd) {
        ObjectAnimator playerAnim = ObjectAnimator.ofFloat(user, "translationY", 0f, -50f, 0f);
        ObjectAnimator cpuAnim = ObjectAnimator.ofFloat(cpu, "translationY", 0f, -50f, 0f);

        playerAnim.setRepeatCount(2);
        cpuAnim.setRepeatCount(2);
        playerAnim.setDuration(375);
        cpuAnim.setDuration(375);
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

        // Regras de vitória para cada opção do jogador
        switch (player) {
            case "rock": // Pedra
                if (cpu.equals("scissors") || cpu.equals("lagarto")) {
                    return "You won!"; // Pedra quebra Tesoura, Pedra esmaga Lagarto
                }
                break;
            case "paper": // Papel
                if (cpu.equals("rock") || cpu.equals("spock")) {
                    return "You won!"; // Papel cobre Pedra, Papel refuta Spock
                }
                break;
            case "scissors": // Tesoura
                if (cpu.equals("paper") || cpu.equals("lagarto")) {
                    return "You won!"; // Tesoura corta Papel, Tesoura decapita Lagarto
                }
                break;
            case "lagarto": // Lagarto
                if (cpu.equals("paper") || cpu.equals("spock")) {
                    return "You won!"; // Lagarto come Papel, Lagarto envenena Spock
                }
                break;
            case "spock": // Spock
                if (cpu.equals("scissors") || cpu.equals("rock")) {
                    return "You won!"; // Spock quebra Tesoura, Spock vaporiza Pedra
                }
                break;
        }
        return "You lost!"; // Se o jogador não venceu, a CPU venceu
    }

    @SuppressLint("SetTextI18n")
    private void updateScore() {
        scoreText.setText("You: " + playerScore + "  |  App: " + cpuScore);

    }
    private void loadInterstitialAd() {
        String interstitialId = getString(R.string.inter); // ID do anúncio
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, interstitialId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                mInterstitialAd = ad;  // Armazena o anúncio carregado na variável
                // *** REMOVA OU COMENTE A LINHA ABAIXO ***
                // mInterstitialAd.show(Player.this); // ERRO: Não chame show() aqui!

                // Opcional: Adicione um Listener para saber quando o anúncio foi fechado
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Chamado quando o anúncio é fechado
                        Log.d("AdDebug", "O anúncio intersticial foi fechado.");
                        // Aqui você pode carregar o próximo anúncio ou fazer outra ação
                        // loadInterstitialAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Chamado quando o anúncio falha ao ser exibido
                        Log.e("AdError", "Falha ao exibir o anúncio: " + adError.getMessage());
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Chamado quando o anúncio é exibido
                        Log.d("AdDebug", "O anúncio intersticial foi exibido.");
                    }
                });
                Log.d("AdDebug", "Anúncio intersticial carregado com sucesso.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e("AdError", "Falha ao carregar o anúncio: " + loadAdError.getMessage());
                mInterstitialAd = null; // Garanta que a referência seja nula em caso de falha
            }
        });
    }

    // Este método deve ser chamado em um PONTO DE TRANSIÇÃO NATURAL DO SEU APP
    private void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(JokenPowSpockActivity.this);
            // Após exibir o anúncio, você pode querer carregá-lo novamente para o próximo uso
            // loadInterstitialAd(); // Depende da sua lógica
        } else {
            Log.d("AdDebug", "Anúncio intersticial não está disponível para exibição. Carregando um novo.");
            loadInterstitialAd(); // Tente carregar novamente se não estiver disponível
        }
    }
}