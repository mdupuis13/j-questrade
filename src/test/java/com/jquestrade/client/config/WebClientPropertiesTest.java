package com.jquestrade.client.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = WebClientProperties.class)
@TestPropertySource("classpath:web-client-properties-test.yml")
class WebClientPropertiesTest {
    @Autowired
    private WebClientProperties webClientProperties;

    @Test
    void givenUserDefinedPOJO_whenBindingPropertiesFile_thenAllFieldsAreSet() {
        assertThat(webClientProperties.getLoginUrl()).isEqualTo("test login url");
    }
}