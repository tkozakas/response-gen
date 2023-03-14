package org.voice.response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChatGPT {
    public static String getTextFromFile(String filepath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            Runtime.getRuntime().exec(
                    new String[]{
                            "python",
                            "chatGPT.py",
                            "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",
                            getTextFromFile("speech.txt")
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
