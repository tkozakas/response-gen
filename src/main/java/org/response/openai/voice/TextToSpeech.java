package org.response.openai.voice;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * @author Tomas Kozakas
 */
public class TextToSpeech {
    private final Voice voice;

    public TextToSpeech() {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("mbrola.base");
        voice.allocate();
    }

    public void say(String text) {
        voice.speak(text);
    }
}
