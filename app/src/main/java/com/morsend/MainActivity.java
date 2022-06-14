package com.morsend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.morsend.function.ActionCaller;
import com.morsend.function.Settings;
import com.morsend.util.FlashLight;

public class MainActivity extends AppCompatActivity {

    private static String editTextMessageCached = null;
    public static void setEditTextMessage(String message) {
        editTextMessageCached = message;
    }

    private void applyCachedMessage(EditText editText) {
        editText.setText(editTextMessageCached);
        editTextMessageCached = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlashLight.openDevice(this);
        ActionCaller.init(getApplicationContext());
        Settings.init(getApplicationContext());
        setContentView(R.layout.activity_main);

        EditText messageEdit = findViewById(R.id.editTextMessage);
        applyCachedMessage(messageEdit);

        Button messageSend = findViewById(R.id.sendMessageButton);
        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.sendMessage(messageEdit, getApplicationContext());
            }
        });

        Button messageStop = findViewById(R.id.stopMessage);
        messageStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.stopMessage();
            }
        });

        Button messageSOS = findViewById(R.id.quickSosSignal);
        messageSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.quickSOS();
            }
        });

        Button stroboscopeButton = findViewById(R.id.stroboscopeButton);
        stroboscopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.toggleStroboscope();
            }
        });

        Button flashLightButton = findViewById(R.id.toggleFlashButton);
        flashLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.toggleFlash();
            }
        });

        MainActivity activity = this;

        Button recentMessagesButton = findViewById(R.id.recentMessagesButton);
        recentMessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.changeActivity(activity, RecentMessagesActivity.class);
            }
        });

        Button optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionCaller.changeActivity(activity, OptionsActivity.class);
            }
        });

        ActionCaller.setMessageTyper(findViewById(R.id.messageTyper));
    }

    @Override
    protected void onDestroy() {
        FlashLight.closeDevice();

        EditText messageEdit = findViewById(R.id.editTextMessage);
        if (messageEdit != null) {
            setEditTextMessage(messageEdit.getText().toString());
        }

        super.onDestroy();
    }
}