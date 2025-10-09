package com.kqube.cani.Service;

import java.io.File;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kqube.cani.Impl.Answer;
import com.kqube.cani.Impl.AudioConvertor;
import com.kqube.cani.Impl.Question;

@Service
public class CaniService {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private OpenAiAudioTranscriptionModel transcriptionModel;

    @Autowired
    private OpenAiAudioSpeechModel speechModel;  // for TTS (text → audio)

    // --- Handle text-based question ---
    public Object getResult(Question question) {
        if (question == null || question.question() == null || question.question().trim().isEmpty()) {
            return new Answer("⚠️ No question provided.");
        }

        Prompt prompt = new PromptTemplate(question.question()).create();
        ChatResponse response = chatModel.call(prompt);

        if (response != null && !response.getResults().isEmpty()) {
            return new Answer(response.getResults().get(0).getOutput().getText());
        }
        return new Answer("⚠️ No response received from AI.");
    }

    // --- Handle audio transcription (audio → text) ---
    public String transcribeVoice(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return "⚠️ No audio file uploaded.";
        }

        File source = File.createTempFile("upload-", ".tmp");
        file.transferTo(source);

        File mp3File = AudioConvertor.convertToMp3(source);

        return transcriptionModel.call(new FileSystemResource(mp3File));
    }

    // --- Handle full voice mode (audio in → audio out) ---
    public byte[] voiceMode(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No audio file uploaded.");
        }

        File source = null;
        File mp3File = null;
        try {
            source = File.createTempFile("upload-", ".tmp");
            file.transferTo(source);

            mp3File = AudioConvertor.convertToMp3(source);

            String question = transcriptionModel.call(new FileSystemResource(mp3File));
            if (question == null || question.trim().isEmpty()) {
                question = "I couldn't hear you clearly. Please repeat.";
            }

            Prompt prompt = new PromptTemplate(question).create();
            ChatResponse response = chatModel.call(prompt);
            String answer = "Sorry, I couldn't generate an answer.";
            if (response != null && !response.getResults().isEmpty()) {
                answer = response.getResults().get(0).getOutput().getText();
            }

            SpeechPrompt speechPrompt = new SpeechPrompt(answer);
            SpeechResponse speechResponse = speechModel.call(speechPrompt);

            return speechResponse.getResult().getOutput();
        } finally {
            // best-effort cleanup
            if (source != null) java.nio.file.Files.deleteIfExists(source.toPath());
            if (mp3File != null) java.nio.file.Files.deleteIfExists(mp3File.toPath());
        }
    }
}
