package com.example.firstapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

import utils.GameManager;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton[] buttons;
    private ShapeableImageView[] hearts;
    private ShapeableImageView[] car;
    private ShapeableImageView[][] barriers;
    private GameManager gameManager;
    private final int barrierRows = 6;
    private final int barrierCols = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setButtonsListeners();
        gameManager = new GameManager(3, barrierRows, barrierCols);
        gameManager.init();
    }

    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer();
        int delay = 1000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateUI());
            }
        }, delay, delay);
    }

    private void updateUI() {
        gameManager.updateGame();
        if (gameManager.getGameOver()) {
            showToast("Game Over!");
            vibrate();
            gameManager.setLifeCount(3);
            gameManager.init();
            gameManager.setGameOver(false);
        }
        if (gameManager.getCrash()) {
            showLives();
            showToast("Crash!");
            gameManager.setCrash(false);
            vibrate();
        }
        showBarriers();
    }

    private void setButtonsListeners() {
        buttons[0].setOnClickListener(v -> onClickLeft());
        buttons[1].setOnClickListener(v -> onClickRight());
    }

    private void onClickRight() {
        gameManager.moveCar(GameManager.Direction.Right);
        showCar();
    }

    private void onClickLeft() {
        gameManager.moveCar(GameManager.Direction.Left);
        showCar();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showBarriers() {
        for (int i = 0; i < barrierRows; i++) {
            for (int j = 0; j < barrierCols; j++) {
                if (gameManager.getBarriers()[i][j]) {
                    barriers[i][j].setImageResource(R.drawable.barrier);
                    barriers[i][j].setVisibility(View.VISIBLE);
                } else
                    barriers[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showLives() {
        boolean[] lives = gameManager.getLives();
        for (int i = 0; i < lives.length; i++) {
            if (lives[i])
                hearts[i].setVisibility(View.VISIBLE);
            else
                hearts[i].setVisibility(View.INVISIBLE);
        }
    }

    private void showCar() {
        for (int i = 0; i < barrierCols; i++) {
            if (gameManager.getCar()[i])
                car[i].setVisibility(View.VISIBLE);
            else
                car[i].setVisibility(View.INVISIBLE);
        }
    }

    private void findViews() {
        car = new ShapeableImageView[]{
                findViewById(R.id.car_left),
                findViewById(R.id.car_center),
                findViewById(R.id.car_right)};
        hearts = new ShapeableImageView[]{
                findViewById(R.id.heart_1),
                findViewById(R.id.heart_2),
                findViewById(R.id.heart_3)};
        buttons = new FloatingActionButton[]{
                findViewById(R.id.left_button),
                findViewById(R.id.right_button)
        };
        barriers = new ShapeableImageView[][]{
                {findViewById(R.id.barrier_1_1),
                        findViewById(R.id.barrier_2_1),
                        findViewById(R.id.barrier_3_1),}
                ,
                {findViewById(R.id.barrier_1_2),
                        findViewById(R.id.barrier_2_2),
                        findViewById(R.id.barrier_3_2),
                },
                {findViewById(R.id.barrier_1_3),
                        findViewById(R.id.barrier_2_3),
                        findViewById(R.id.barrier_3_3),},

                {findViewById(R.id.barrier_1_4),
                        findViewById(R.id.barrier_2_4),
                        findViewById(R.id.barrier_3_4),
                },
                {findViewById(R.id.barrier_1_5),
                        findViewById(R.id.barrier_2_5),
                        findViewById(R.id.barrier_3_5),
                },
                {findViewById(R.id.barrier_1_6),
                        findViewById(R.id.barrier_2_6),
                        findViewById(R.id.barrier_3_6),
                }
        };
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("ObsoleteSdkInt")
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        else
            vibrator.vibrate(500);
    }
}