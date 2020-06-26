package com.moppletop.app.logic.command;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
public class QuizCommandAnalysis {

    String cmd;
    Map<String, String> flags = new HashMap<>();

    public QuizCommandAnalysis(String raw) {
        int index = raw.indexOf(' ');

        if (index == -1) {
            this.cmd = raw;
            return;
        }

        this.cmd = raw.substring(0, index);

        StringBuilder flagKey = null;
        StringBuilder flagValue = null;
        boolean encapsulated = false;
        char ch;

        for (; index < raw.length(); index++) {
            ch = raw.charAt(index);

            if (flagKey == null) {
                if (ch == '-') {
                    flagKey = new StringBuilder();
                }
            } else if (flagValue == null) {
                if (ch == ' ') {
                    flagValue = new StringBuilder();
                    encapsulated = raw.charAt(index + 1) == '"';
                    continue;
                }

                flagKey.append(ch);
            } else {
                if (flagValue.length() == 0) {
                    if (encapsulated && ch == '"') {
                        continue;
                    } else if (!encapsulated && ch == '-') {
                        flags.put(flagKey.toString(), "true");
                        flagKey = null;
                        flagValue = null;
                        index--;
                        continue;
                    }
                }

                if (encapsulated && ch == '"' || !encapsulated && ch == ' ') {
                    flags.put(flagKey.toString(), flagValue.toString());
                    flagKey = null;
                    flagValue = null;
                    continue;
                }

                flagValue.append(ch);
            }
        }

        if (flagKey != null && flagValue != null) {
            flags.put(flagKey.toString(), flagValue.toString());
        }
    }

    public String getFlag(String key) {
        return flags.get(key);
    }

    public String getRequiredFlag(String key) {
        String flag = flags.get(key);

        if (flag == null) {
            throw new IllegalArgumentException("The " + key + " flag is required for this command!");
        }

        return flag;
    }
}
