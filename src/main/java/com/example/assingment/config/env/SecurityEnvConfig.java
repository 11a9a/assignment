package com.example.assingment.config.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Data
@ConfigurationProperties(value = "security")
public class SecurityEnvConfig {
    private Map<String, UserProperties> users;

    @Data
    public static class UserProperties {
        private String username;
        private String password;
        private String role;
    }
}
