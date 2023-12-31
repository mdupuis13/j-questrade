package com.jquestrade.client;

import com.jquestrade.AuthenticationToken;
import com.jquestrade.client.config.WebClientProperties;
import com.jquestrade.exceptions.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class QuestradeWebClientImpl implements QuestradeWebClient {

    private final RestClient apiClient;

    private final DateTimeFormatter DATE_HEADER_FORMAT_NY = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    private final ZoneId ZONE_NY = ZoneId.of("America/New_York");

    public QuestradeWebClientImpl(WebClientProperties properties) {

        apiClient = RestClient.create(properties.getLoginUrl());
    }

    @Override
    public AuthenticationToken authenticate(String refreshToken) {

        ResponseEntity<Authorization> response = apiClient.get()
                                                          .uri(uriBuilder -> uriBuilder.path("/oauth2/token")
                                                                                       .queryParam("grant_type", "refresh_token")
                                                                                       .queryParam("refresh_token", refreshToken)
                                                                                       .build())
                                                          .retrieve()
                                                          .toEntity(Authorization.class);

        return createAuthenticationObject(response);
    }

    private AuthenticationToken createAuthenticationObject(ResponseEntity<Authorization> response) {
        Authorization clientAuth = response.getBody();

        if (clientAuth == null) throw new AuthenticationException("Cannot retrieve auth token");

        String dateHeader = response.getHeaders().getFirst("date");
        OffsetDateTime expiresAt = getExpirationDate(dateHeader, clientAuth);

        return new AuthenticationToken(clientAuth.access_token(), clientAuth.api_server(), expiresAt, clientAuth.refresh_token(), clientAuth.token_type());
    }

    private OffsetDateTime getExpirationDate(String dateHeader, Authorization clientAuth) {
        ZoneOffset currentZone = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now());

        OffsetDateTime dateParsed = LocalDateTime.parse(dateHeader, DATE_HEADER_FORMAT_NY)
                                                 .atZone(ZONE_NY)
                                                 .toOffsetDateTime();

        return dateParsed.plusSeconds(clientAuth.expires_in())
                         .withOffsetSameLocal(currentZone);
    }


}
