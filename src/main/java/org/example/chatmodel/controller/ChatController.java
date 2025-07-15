package org.example.chatmodel.controller;

import org.example.chatmodel.dto.ChatDto;
import org.example.chatmodel.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody ChatDto chatDto) {
        String response = chatService.processQuestion(chatDto);
        return ResponseEntity.ok(response);
    }
}
