package com.morsend.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MorseEncoder {

    private static final long SHORT_SIGNAL = 1;
    private static final long LONG_SIGNAL = 3;
    private static final long EMPTY_INTERVAL = 1;
    private static final long CHAR_INTERVAL = 3;
    private static final long WORD_INTERVAL = 7;

    private static final Map<Character, ArrayList<Long>> morseEncoding = new HashMap<>();

    private static void registerCharacter(char c, Collection<Long> intervals) {
        Character character = Character.toUpperCase(c);
        morseEncoding.put(character, new ArrayList<>(intervals));
    }

    static {
        registerCharacter('A', Arrays.asList(SHORT_SIGNAL, LONG_SIGNAL));
        registerCharacter('B', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('C', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL));

        registerCharacter('D', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('E', Collections.singletonList(SHORT_SIGNAL));
        registerCharacter('F', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL));

        registerCharacter('G', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('H', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('I', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL));

        registerCharacter('J', Arrays.asList(SHORT_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));
        registerCharacter('K', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL));
        registerCharacter('L', Arrays.asList(SHORT_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));

        registerCharacter('M', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL));
        registerCharacter('N', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL));
        registerCharacter('O', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));

        registerCharacter('P', Arrays.asList(SHORT_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL));
        registerCharacter('Q', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL));
        registerCharacter('R', Arrays.asList(SHORT_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL));
        registerCharacter('S', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));

        registerCharacter('T', Collections.singletonList(LONG_SIGNAL));
        registerCharacter('U', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL));
        registerCharacter('V', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL));

        registerCharacter('W', Arrays.asList(SHORT_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));
        registerCharacter('X', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL));
        registerCharacter('Y', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));
        registerCharacter('Z', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));

        registerCharacter('1', Arrays.asList(SHORT_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));
        registerCharacter('2', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));
        registerCharacter('3', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));
        registerCharacter('4', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, LONG_SIGNAL));
        registerCharacter('5', Arrays.asList(SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('6', Arrays.asList(LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('7', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('8', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL, SHORT_SIGNAL));
        registerCharacter('9', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, SHORT_SIGNAL));
        registerCharacter('0', Arrays.asList(LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL, LONG_SIGNAL));
    }

    public static void encode(SignalSender signalSender, String message) {
        String upperCaseMessage = message.toUpperCase();
        String[] splitUpperCaseMessage = upperCaseMessage.split("\\s");
        signalSender.openSignalBuilder();
        for (int i = 0; i < splitUpperCaseMessage.length; i++) {
            String upperCaseWord = splitUpperCaseMessage[i];
            upperCaseWord = Normalizer.normalize(upperCaseWord, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            upperCaseWord = upperCaseWord.replaceAll("[^A-Z0-9]", "");
            //System.out.println("Encoding: [" + upperCaseWord + "]");
            encodeWord(signalSender, upperCaseWord, i == splitUpperCaseMessage.length - 1);
        }
        signalSender.buildSignal();
    }

    private static void encodeWord(SignalSender signalSender, String upperCaseWord, boolean lastWord) {
        int wordLength = upperCaseWord.length();
        for (int j = 0; j < wordLength; j++) {
            char character = upperCaseWord.charAt(j);
            ArrayList<Long> morseComb = morseEncoding.get(character);
            if (morseComb != null) {
                signalSender.addCharacterToType(character);
                Long interval = morseComb.get(0);
                signalSender.addSignal(interval, true);
                for (int i = 1; i < morseComb.size(); i++) {
                    signalSender.addSignal(EMPTY_INTERVAL, false);
                    interval = morseComb.get(i);
                    signalSender.addSignal(interval, true);
                }
                if (j < wordLength - 1) {
                    signalSender.addSignal(CHAR_INTERVAL, false);
                } else {
                    if (!lastWord) {
                        signalSender.addCharacterToType('_');
                    }
                    signalSender.addSignal(WORD_INTERVAL, false);
                }
            }
        }
    }
}
