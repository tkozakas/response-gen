package org.response.openai.voice;

import lombok.SneakyThrows;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Tomas Kozakas
 */
public class AudioRecorder {
    private final AudioFormat format;
    private final DataLine.Info info;
    private TargetDataLine line;

    public AudioRecorder() throws LineUnavailableException {
        format = new AudioFormat(16000, 16, 1, true, false);
        info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Line not supported");
        }
    }

    public void startRecording(String fileName) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Set a silence threshold (in dB)
            int silenceThresholdDb = -60; // adjust as needed

            // Set the length of silence required to stop recording (in milliseconds)
            long silenceDurationMs = 1000;

            // Read data from the line and write it to the buffer until stopped
            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 5];
            long lastSoundMillis = System.currentTimeMillis();
            boolean hasSound = false;
            while (true) {
                numBytesRead = line.read(data, 0, data.length);
                out.write(data, 0, numBytesRead);

                // Check if the recording has stopped (i.e., if there's no sound)
                double rms = calculateRMS(data);
                double db = 20 * Math.log10(rms / 32767.0);
                if (db > silenceThresholdDb) {
                    lastSoundMillis = System.currentTimeMillis();
                    hasSound = true;
                }
                if (hasSound && (System.currentTimeMillis() - lastSoundMillis) > silenceDurationMs) {
                    break;
                }
                // System.out.println("Sounds: " + hasSound + ", db:" + db);
            }

            // Create an AudioInputStream from the buffered data
            AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(out.toByteArray()), format, out.size());

            // Write the audio data to a file
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(fileName));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static double calculateRMS(byte[] data) {
        long sum = 0;
        for (byte b : data) {
            sum += b * b;
        }
        double mean = (double) sum / data.length;
        return Math.sqrt(mean);
    }

    @SneakyThrows
    public void stopRecording() {
        Files.deleteIfExists(Paths.get("audio.wav"));
        line.stop();
        line.close();
    }

    @SneakyThrows
    public void openLine() {
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
    }
}
