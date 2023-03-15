package org.voice;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class AudioRecorder {
    private final AudioFormat format;
    private final DataLine.Info info;

    public AudioRecorder() throws LineUnavailableException {
        format = new AudioFormat(16000, 16, 1, true, false);
        info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Line not supported");
        }
    }

    public void startRecording(int timeInSeconds, String fileName) {
        try {
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            System.out.println("Recording...");

            long startTime = System.currentTimeMillis();
            long duration = timeInSeconds * 1000L; // milliseconds

            // Use a buffer to capture the recorded audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Read data from the line and write it to the buffer
            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 5];
            while (System.currentTimeMillis() - startTime < duration) {
                numBytesRead = line.read(data, 0, data.length);
                out.write(data, 0, numBytesRead);
            }

            // Wait for the line to drain before stopping and closing it
            line.drain();
            line.stop();
            line.close();

            // Create an AudioInputStream from the buffered data
            AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(out.toByteArray()), format, out.size());

            // Write the audio data to a file
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(fileName));

            System.out.println("Recording complete.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
