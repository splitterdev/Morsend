package com.morsend.function;

import android.content.Context;
import android.util.Log;

import com.morsend.util.FileManager;
import com.morsend.util.FileReader;
import com.morsend.util.FileWriter;
import com.morsend.util.MessageLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Settings {

    private static final String FILENAME_SETTINGS = "settings.cfg";
    private static long messageLogCapacity = 10;
    private static long timeQuantumIntervalMs = 100;

    public static long getMessageLogCapacity() {
        return messageLogCapacity;
    }

    public static void setMessageLogCapacity(long messageLogCapacity) {
        Settings.messageLogCapacity = messageLogCapacity;
    }

    public static long getTimeQuantumIntervalMs() {
        return timeQuantumIntervalMs;
    }

    public static void setTimeQuantumIntervalMs(long timeQuantumIntervalMs) {
        Settings.timeQuantumIntervalMs = timeQuantumIntervalMs;
    }

    public static void init(Context context) {
        FileManager.readFromFile(context, FILENAME_SETTINGS, new FileReader() {
            @Override
            public void doRead(BufferedReader reader) throws Exception {
                String timeDurationQMS = reader.readLine();
                String capacityMLog = reader.readLine();
                Settings.setTimeQuantumIntervalMs(Long.parseLong(timeDurationQMS));
                Settings.setMessageLogCapacity(Long.parseLong(capacityMLog));
            }
        });
    }

    public static void save(Context context) {
        FileManager.write(context, FILENAME_SETTINGS, new FileWriter() {
            @Override
            public void doWrite(OutputStreamWriter writer) throws Exception {
                writer.write(Settings.getTimeQuantumIntervalMs() + "\n");
                writer.write(Settings.getMessageLogCapacity() + "\n");
            }
        });
    }
}
