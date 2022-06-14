package com.morsend.util;

import android.content.Context;

import com.morsend.function.Settings;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class MessageLog {

    private static final String FILENAME_MESSAGE_LOG = "messageLog.cfg";
    private LinkedList<String> messages = new LinkedList<>();

    public MessageLog() {
    }

    public void rescaleLog(int capacity, Context context) {
        LinkedList<String> newMessages = new LinkedList<>();
        int maxCap = Math.min(capacity, messages.size());
        for (int i = 0; i < maxCap; i++) {
            newMessages.add(messages.get(i));
        }
        messages = newMessages;
        Settings.setMessageLogCapacity(capacity);
        save(context);
    }

    public void registerMessage(String message) {
        message = message.replace('\n', ' ');
        if (containsMessage(message)) {
            return;
        }
        messages.addFirst(message);
        while (messages.size() > Settings.getMessageLogCapacity()) {
            messages.removeLast();
        }
    }

    private boolean containsMessage(String message) {
        String messageUpper = message.toUpperCase();
        for (int i = 0; i < messages.size(); i++) {
            String regMsg = messages.get(i);
            if (regMsg.toUpperCase().contentEquals(messageUpper)) {
                messages.remove(i);
                messages.addFirst(regMsg);
                return true;
            }
        }
        return false;
    }

    public static MessageLog readOrCreate(Context context) {
        MessageLog log = new MessageLog();
        FileManager.readFromFile(context, FILENAME_MESSAGE_LOG, new FileReader() {
            @Override
            public void doRead(BufferedReader reader) throws Exception {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.messages.add(line);
                }
            }
        });
        return log;
    }

    public void save(Context context) {
        FileManager.write(context, FILENAME_MESSAGE_LOG, new FileWriter() {
            @Override
            public void doWrite(OutputStreamWriter writer) throws Exception {
                for (String message : messages) {
                    writer.write(message + "\n");
                }
            }
        });
    }

    public Collection<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public void clear(Context context) {
        messages = new LinkedList<>();
        save(context);
    }
}
