package org.example.chatmodel.controller;

import org.example.chatmodel.dto.ChatDto;
import org.example.chatmodel.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


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

    @PostMapping("/text-to-speech/download")
    public Mono<ResponseEntity<byte[]>> textToSpeech(@RequestBody ChatDto chatDto) {
        return chatService.synthesizeAudioFile(chatDto)
                .map(audioBytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audio.mp3\"")
                        .contentType(MediaType.valueOf("audio/mpeg"))
                        .body(audioBytes));
    }

//    @PostMapping(value = "/text-to-speech/stream/real-time", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<Flux<DataBuffer>> streamAudio(@RequestBody ChatDto chatDto) {
//        Flux<DataBuffer> audioStream = chatService.streamAudio(chatDto);
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType("audio/mpeg"))
//                .body(audioStream);
//    }
}
