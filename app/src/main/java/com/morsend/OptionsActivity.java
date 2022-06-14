package com.morsend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.morsend.function.ActionCaller;
import com.morsend.function.Settings;
import com.morsend.util.SignalSender;

import org.w3c.dom.Text;

public class OptionsActivity extends AppCompatActivity {

    private static final double MS_TIME_DURATION_MIN = 20.0;
    private static final double MS_TIME_DURATION_MAX = 220.0;

    private static final int CAPACITY_LOG_MIN = 5;
    private static final int CAPACITY_LOG_MAX = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button goBackButton = findViewById(R.id.goBack_1);
        OptionsActivity activity = this;
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.changeActivity(activity, MainActivity.class);
            }
        });

        SeekBar signalTimeDuration = findViewById(R.id.timeQDurationMsSeekBar);
        TextView labelView = findViewById(R.id.timeDurationMsInfo);
        signalTimeDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressTime(progress, labelView);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        double progressNow = (((double) Settings.getTimeQuantumIntervalMs() - MS_TIME_DURATION_MIN) / (MS_TIME_DURATION_MAX - MS_TIME_DURATION_MIN)) * 100.0;
        signalTimeDuration.setProgress((int) Math.round(progressNow));
        updateProgressTime((int) Math.round(progressNow), labelView);

        SeekBar capacitySeekBar = findViewById(R.id.capacitySeeker);
        TextView labelViewCapacity = findViewById(R.id.capacityInfo);
        capacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressCapacity(progress, labelViewCapacity);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        double progressNowCap = (((double) Settings.getMessageLogCapacity() - CAPACITY_LOG_MIN) / (CAPACITY_LOG_MAX - CAPACITY_LOG_MIN)) * 100.0;
        capacitySeekBar.setProgress((int) Math.round(progressNowCap));
        updateProgressCapacity((int) Math.round(progressNowCap), labelViewCapacity);

        Button clearMessageLog = findViewById(R.id.clearMessageLog);
        clearMessageLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.getMessageLog().clear(v.getContext());
            }
        });
    }

    private void updateProgressCapacity(int progress, TextView labelView) {
        double progressD = ((double) progress) / 100.0;
        long calculatedCapacity = Math.round(progressD * (CAPACITY_LOG_MAX - CAPACITY_LOG_MIN) + CAPACITY_LOG_MIN);
        ActionCaller.getMessageLog().rescaleLog((int) calculatedCapacity, getApplicationContext());
        labelView.setText("" + calculatedCapacity);
        Settings.save(getApplicationContext());
    }

    private void updateProgressTime(int progress, TextView labelView) {
        double progressD = ((double) progress) / 100.0;
        long calculatedMsTime = Math.round(progressD * (MS_TIME_DURATION_MAX - MS_TIME_DURATION_MIN) + MS_TIME_DURATION_MIN);
        Settings.setTimeQuantumIntervalMs(calculatedMsTime);
        labelView.setText("" + calculatedMsTime + "ms");
        Settings.save(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}