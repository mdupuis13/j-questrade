package com.jquestrade.client;

import com.jquestrade.client.config.WebclientProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


public class QuestradeWebClientImpl implements QuestradeWebClient {

    private final WebClient webClient;
    private Authorization authInfo;

    public QuestradeWebClientImpl(WebclientProperties properties) {

        webClient = WebClient.builder()
                .baseUrl(properties.getLoginUrl())
                .build();
    }

    @Override
    public void authenticate(String refreshToken) {

        this.authInfo = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/oauth2/token")
                        .queryParam("grant_type", "refresh_token")
                        .queryParam("refresh_token", refreshToken)
                        .build())
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> handleErrorResponse(clientResponse.statusCode(), "Authorization"))
                .bodyToMono(Authorization.class)
                .block();
    }

    @Override
    public Boolean isAuthenticated() {
        return this.authInfo.isValid();
    }


    private Mono<? extends Throwable> handleErrorResponse(HttpStatusCode statusCode, String apiCalled) {

        // Handle non-success status codes here (e.g., logging or custom error handling)
        return Mono.error(new Exception("Failed to fetch '%s' from Questrade API. Status code: %s".formatted(apiCalled, statusCode)));
    }
}
