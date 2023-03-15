package org.response;

import org.response.database.Message;
import org.response.database.MessageService;
import org.response.voice.AudioRecorder;
import org.response.voice.SpeechToText;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tomas Kozakas
 */

@SpringBootApplication
public class ResponseApplication implements CommandLineRunner {
    private final MessageService messageService;

    public ResponseApplication(MessageService messageService) {
        this.messageService = messageService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ResponseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Load conversation with previous messages
        List<Message> messageList = messageService.getAll();
        if (messageList != null) {
            ProcessBuilder pb = new ProcessBuilder("python", "chatGPT.py", messageList.toString());
            pb.start();
        }

        AudioRecorder audioRecorder = new AudioRecorder();
        audioRecorder.openLine();

        System.out.println("Start speaking to chatGPT");
        do {
            audioRecorder.startRecording("audio.wav");

            SpeechToText speechToText = new SpeechToText();
            speechToText.recognize();
            String userMessage = speechToText.getText();

            if (userMessage != null) {
                if (userMessage.equals("bye") || userMessage.equals("Bye")) {
                    audioRecorder.closeLine();
                    break;
                }
                // create a ProcessBuilder for running the Python program
                ProcessBuilder pb = new ProcessBuilder("python", "chatGPT.py", userMessage);
                pb.redirectErrorStream(true); // redirect the error stream to the standard output
                // start the process
                Process process = pb.start();

                // read the output of the process
                InputStream is = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder chatbotMessage = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    chatbotMessage.append(line);
                }
                LocalDateTime localDateTime = LocalDateTime.now();
                Message message = new Message(localDateTime, userMessage, chatbotMessage.toString());
                messageService.save(message);
            }
        } while (true);
    }
}
