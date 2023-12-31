package com.jquestrade.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "info.martindupuis.jquestrade")
public class WebClientProperties {
    private String loginUrl;
}
