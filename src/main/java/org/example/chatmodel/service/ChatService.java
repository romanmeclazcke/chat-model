package org.example.chatmodel.service;

import org.example.chatmodel.dto.ChatDto;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OllamaChatModel ollamaChatModel;

    @Autowired
    public ChatService(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }


    public String processQuestion(ChatDto chatDto) {
        try {
            OllamaOptions ollamaOptions = OllamaOptions.builder().model(OllamaModel.MISTRAL).build(); //Model used
            Prompt prompt = new Prompt(chatDto.getMessage(), ollamaOptions);
            return ollamaChatModel.call(prompt).getResult().getOutput().getText();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}