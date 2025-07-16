package org.example.chatmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HumeTtsRequestDto {

    private List<Utterance> utterances;
    private Format format = new Format("mp3");

    public HumeTtsRequestDto(String text) {
        this.utterances = List.of(new Utterance(text));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Utterance {
        private String text;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Format {
        private String type;
    }
}
