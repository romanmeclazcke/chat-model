package org.example.chatmodel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HumeTtsResponseDto {
    @JsonProperty("request_id")
    private String requestId;
    private List<Generation> generations;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Generation {
        private List<Snippet> snippets;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Snippet {
        private String audio; // Base64 encoded audio
        private Double duration;
    }
}
