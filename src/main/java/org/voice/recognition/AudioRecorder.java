package org.voice.recognition;

import javax.sound.sampled.*;
import java.io.File;

public class AudioRecorder {
    public static int RECORD_TIME = 5000; // record for 5 seconds

    public static void main(String[] args) {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Line not supported");
            System.exit(-1);
        }
        try {
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            System.out.println("Recording...");

            AudioInputStream ais = new AudioInputStream(line);
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File("audio.wav"));

            System.out.println("Recording complete.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
