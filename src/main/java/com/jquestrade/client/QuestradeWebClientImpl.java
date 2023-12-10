package com.jquestrade.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class QuestradeWebClientImpl implements QuestradeWebClient {

    @Value("${com.jquestrade.questrade.login-url}")
    private final String QUESTRADE_URL = "";
    private final WebClient webClient;
    private Authorization authInfo;

    public QuestradeWebClientImpl() {

        webClient = WebClient.builder()
                .baseUrl(QUESTRADE_URL)
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
