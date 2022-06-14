package com.morsend.function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.morsend.util.FlashLight;
import com.morsend.util.MessageLog;
import com.morsend.util.MorseEncoder;
import com.morsend.util.SignalSender;

public class ActionCaller {

    private static final SignalSender signalSender = new SignalSender();
    private static MessageLog messageLog = null;
    private static boolean stroboscope = false;
    private static boolean flashLight = false;

    private ActionCaller() {}

    public static void setMessageTyper(TextView textView) {
        signalSender.setWriteableTextView(textView);
    }

    public static void sendMessage(String message, Context context) {
        MorseEncoder.encode(signalSender, message);
        messageLog.registerMessage(message);
        messageLog.save(context);
    }

    public static void sendMessage(EditText component, Context context) {
        String message = component.getText().toString();
        MorseEncoder.encode(signalSender, message);
        messageLog.registerMessage(message);
        messageLog.save(context);
    }

    public static void quickSOS() {
        MorseEncoder.encode(signalSender, "SOS");
    }

    public static void toggleStroboscope() {
        stroboscope = !stroboscope;
        flashLight = false;
        if (stroboscope) {
            signalSender.stroboscope(1, 1);
        } else {
            signalSender.restartTimer();
        }
    }

    public static void toggleFlash() {
        signalSender.restartTimer();
        stroboscope = false;
        flashLight = !flashLight;
        FlashLight.setLight(flashLight);
    }

    public static void stopMessage() {
        signalSender.restartTimer();
    }

    public static void init(Context context) {
        messageLog = MessageLog.readOrCreate(context);
    }

    public static void exit(Context context) {
        messageLog.save(context);
    }

    public static void changeActivity(Activity currentActivity, Class<? extends Activity> activityClass) {
        Intent i = new Intent(currentActivity, activityClass);
        currentActivity.startActivity(i);
    }

    public static SignalSender getSignalSender() {
        return signalSender;
    }

    public static MessageLog getMessageLog() {
        return messageLog;
    }
}
