package org.voice;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

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

    public void openLine() {
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeLine() {
        line.stop();
        line.close();
    }

    public void startRecording(String fileName) {
        try {
            // Use a buffer to capture the recorded audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Create a flag to indicate when to stop recording
            boolean stopped = false;

            // Read data from the line and write it to the buffer until stopped
            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 5];
            while (!stopped) {
                numBytesRead = line.read(data, 0, data.length);
                out.write(data, 0, numBytesRead);

                // Check if the recording has stopped (i.e., if there's no sound)
                int silenceThreshold = 120; // adjust as needed
                boolean hasSound = false;
                for (byte b : data) {
                    if (b > silenceThreshold || b < -silenceThreshold) {
                        hasSound = true;
                        break;
                    }
                }
                if (!hasSound) {
                    stopped = true;
                }
            }

            // Create an AudioInputStream from the buffered data
            AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(out.toByteArray()), format, out.size());

            // Write the audio data to a file
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
