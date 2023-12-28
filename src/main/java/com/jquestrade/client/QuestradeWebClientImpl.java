package com.jquestrade.client;

import com.jquestrade.client.config.WebClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;


public class QuestradeWebClientImpl implements QuestradeWebClient {

    private final RestClient apiClient;

    public QuestradeWebClientImpl(WebClientProperties properties) {

        apiClient = RestClient.create(properties.getLoginUrl());
    }

    private static com.jquestrade.Authorization getAuthorization(ResponseEntity<Authorization> response) {
        Authorization clientAuth = response.getBody();

        String dateHeader = response.getHeaders().getFirst("date");
        OffsetDateTime expiresAt = getExpirationDate(dateHeader, clientAuth.expires_in());

        return new com.jquestrade.Authorization(clientAuth.access_token(), clientAuth.api_server(), expiresAt, clientAuth.refresh_token(), clientAuth.token_type());
    }

    private static OffsetDateTime getExpirationDate(String dateHeader, Authorization clientAuth) {
        return OffsetDateTime.parse(dateHeader)
                             .plusMinutes(clientAuth.expires_in());
    }

    @Override
    public com.jquestrade.Authorization authenticate(String refreshToken) {

        ResponseEntity<Authorization> response = apiClient.get()
                                                          .uri(uriBuilder -> uriBuilder.path("/oauth2/token")
                                                                                       .queryParam("grant_type", "refresh_token")
                                                                                       .queryParam("refresh_token", refreshToken)
                                                                                       .build())
                                                          .retrieve()
                                                          .toEntity(Authorization.class);

        return getAuthorization(response);
    }
}
