package org.example.chatmodel.service;

import org.example.chatmodel.config.HumeTtsConfig;
import org.example.chatmodel.dto.ChatDto;
import org.example.chatmodel.dto.HumeTtsRequestDto;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.image.DataBuffer;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final OllamaChatModel ollamaChatModel;
    private final WebClient webClient;
    private final HumeTtsConfig humeTtsConfig;

    @Autowired
    public ChatService(OllamaChatModel ollamaChatModel,HumeTtsConfig humeTtsConfig) {
        this.ollamaChatModel = ollamaChatModel;
        this.humeTtsConfig = humeTtsConfig;

        ExchangeStrategies strategies = ExchangeStrategies.builder() // config max in-memory size (default value is 256KB)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(humeTtsConfig.getBaseUrl())
                .defaultHeader("X-Hume-Api-Key", humeTtsConfig.getKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(strategies)
                .build();
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

    public Mono<byte[]> synthesizeAudioFile(ChatDto chatDto) {
        String textIa = this.processQuestion(chatDto);
        HumeTtsRequestDto request = new HumeTtsRequestDto(textIa);

        return webClient.post()
                .uri("/stream/file")
                .bodyValue(request)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .timeout(Duration.ofMillis(humeTtsConfig.getTimeout()));
    }

//    public Flux<DataBuffer> streamAudio(ChatDto chatDto) {
//        String textIa = this.processQuestion(chatDto);
//        HumeTtsRequestDto request = new HumeTtsRequestDto(textIa);
//
//        return webClient.post()
//                .uri("/stream/file")
//                .bodyValue(request)
//                .exchangeToFlux(response -> {
//                    if (response.statusCode().is2xxSuccessful()) {
//                        return response.bodyToFlux(DataBuffer.class);
//                    } else {
//                        return response.createException().flatMapMany(Flux::error);
//                    }
//                });
//    }
}
