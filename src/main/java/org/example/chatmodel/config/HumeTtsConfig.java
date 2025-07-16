package org.example.chatmodel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hume.tts")
@Data
public class HumeTtsConfig {
    private String key;
    private String baseUrl;
    private long timeout = 30000;
}
