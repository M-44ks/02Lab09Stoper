package com.example.a02lab09stoper;

import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView timeView;
    private Button startButton, stopButton, resetButton;

    private long startTime = 0;
    private long elapsedTime = 0;
    private boolean running = false;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Inicjalizacja widoków
        timeView = findViewById(R.id.time_view);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        resetButton = findViewById(R.id.reset_button);

        // Ustawienie listenerów dla przycisków
        startButton.setOnClickListener(v -> startTimer());
        stopButton.setOnClickListener(v -> stopTimer());
        resetButton.setOnClickListener(v -> resetTimer());

        // Przywracanie stanu po rotacji
        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean("running");
            elapsedTime = savedInstanceState.getLong("elapsedTime");
            startTime = savedInstanceState.getLong("startTime");

            if (running) {
                startTimer();
            } else {
                updateTime();
            }
        }

        runTimer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("running", running);
        outState.putLong("elapsedTime", elapsedTime);
        outState.putLong("startTime", startTime);
    }

    private void startTimer() {
        if (!running) {
            startTime = SystemClock.elapsedRealtime() - elapsedTime;
            running = true;
        }
    }

    private void stopTimer() {
        if (running) {
            elapsedTime = SystemClock.elapsedRealtime() - startTime;
            running = false;
        }
    }

    private void resetTimer() {
        running = false;
        elapsedTime = 0;
        updateTime();
    }

    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    elapsedTime = SystemClock.elapsedRealtime() - startTime;
                }
                updateTime();
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void updateTime() {
        int totalSeconds = (int) (elapsedTime / 1000);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timeView.setText(timeString);
    }
}
