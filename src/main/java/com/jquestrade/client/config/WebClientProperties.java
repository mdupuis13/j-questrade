package com.jquestrade.client.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "info.martindupuis.jquestrade")
public class WebClientProperties {
    private final String loginUrl;

    public WebClientProperties(@Value("${imfo.martindupuis.jquestrade.login-url}") final String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
