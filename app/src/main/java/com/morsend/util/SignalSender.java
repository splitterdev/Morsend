package com.morsend.util;

import android.widget.TextView;

import com.morsend.function.Settings;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class SignalSender {

    private TextView textView = null;

    public SignalSender() {}

    private Timer timer = null;
    private LinkedList<SignalItem> itemList;
    private LinkedList<CharItem> charList;
    private long currentTextPoint = 0;

    private static class SignalItem {
        public SignalItem(long quantumDuration, boolean value) {
            this.quantumDuration = quantumDuration;
            this.value = value;
        }
        private final long quantumDuration;
        private final boolean value;

        public long getQuantumDuration() {
            return quantumDuration;
        }

        public boolean getValue() {
            return value;
        }
    }

    private static class CharItem {
        public CharItem(long delay, char character) {
            this.delay = delay;
            this.character = character;
        }
        private final long delay;
        private final char character;

        public long getDelay() {
            return delay;
        }

        public char getCharacter() {
            return character;
        }
    }

    private static class TextCompletionTimerTask extends TimerTask {
        private final char character;
        private final TextView view;
        public TextCompletionTimerTask(TextView view, char character) {
            this.character = character;
            this.view = view;
        }
        @Override
        public void run() {
            String prevText = view.getText().toString();
            view.setText(prevText + Character.toUpperCase(character));
        }
    }

    private static class FlashLightSignalTimerTask extends TimerTask {
        private final boolean enabled;
        public FlashLightSignalTimerTask(boolean enabled) {
            this.enabled = enabled;
        }
        @Override
        public void run() {
            FlashLight.setLight(enabled);
        }
    }

    public void stroboscope(long quantumTimeOn, long quantumTimeOff) {
        restartTimer();
        long fullTime = (quantumTimeOn + quantumTimeOff) * Settings.getTimeQuantumIntervalMs();
        timer.schedule(new FlashLightSignalTimerTask(true), 0, fullTime);
        timer.schedule(new FlashLightSignalTimerTask(false), quantumTimeOn * Settings.getTimeQuantumIntervalMs(), fullTime);
    }

    public void setWriteableTextView(TextView textView) {
        this.textView = textView;
    }

    public void openSignalBuilder() {
        itemList = new LinkedList<>();
        charList = new LinkedList<>();
        currentTextPoint = 0;
    }

    public void addSignal(long quantumDuration, boolean lightEnable) {
        itemList.add(new SignalItem(quantumDuration, lightEnable));
        currentTextPoint += quantumDuration;
    }

    public void addCharacterToType(char character) {
        charList.add(new CharItem(currentTextPoint, character));
    }

    public void buildSignal() {
        restartTimer();
        if (!itemList.isEmpty()) {
            SignalItem firstItem = itemList.remove();
            timer.schedule(new FlashLightSignalTimerTask(firstItem.getValue()), 0);
            long timeCounter = firstItem.getQuantumDuration() * Settings.getTimeQuantumIntervalMs();
            for (SignalItem item : itemList) {
                timer.schedule(new FlashLightSignalTimerTask(item.getValue()), timeCounter);
                timeCounter += item.getQuantumDuration() * Settings.getTimeQuantumIntervalMs();
            }
        }
        if (textView != null) {
            textView.setText("");
            for (CharItem charItem : charList) {
                timer.schedule(new TextCompletionTimerTask(textView, charItem.getCharacter()), charItem.getDelay() * Settings.getTimeQuantumIntervalMs());
            }
        }
    }

    public void restartTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            FlashLight.setLight(false);
        }
        timer = new Timer();
    }
}
