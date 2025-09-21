package com.kqube.cani.Service;

import java.io.File;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
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

    // --- Handle text-based question ---
    public Object getResult(Question question){
        // Validate input
        if (question == null || question.question() == null || question.question().trim().isEmpty()) {
            return new Answer("⚠️ No question provided.");
        }

        // Build prompt safely
        Prompt prompt = new PromptTemplate(question.question()).create();
        ChatResponse response = chatModel.call(prompt);

        if (response != null && !response.getResults().isEmpty()) {
            return new Answer(response.getResults().get(0).getOutput().getText());
        }
        return new Answer("⚠️ No response received from AI.");
    }

    // --- Handle audio transcription ---
    public AssistantMessage transcribeVoice(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return new AssistantMessage("⚠️ No audio file uploaded.");
        }

        // Save uploaded file to temp dir
        File source = File.createTempFile("upload-", ".tmp");
        file.transferTo(source);

        // Convert input to mp3 format
        File mp3File = AudioConvertor.convertToMp3(source);

        // Transcribe using OpenAI model
        String text = transcriptionModel.call(new FileSystemResource(mp3File));

        return new AssistantMessage(text);
    }

	
}
