package com.jquestrade.client;

import com.jquestrade.Account;
import com.jquestrade.AuthenticationToken;
import com.jquestrade.client.config.WebClientProperties;
import com.jquestrade.exceptions.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
public class QuestradeWebClientImpl implements QuestradeWebClient {

    private final RestClient authenticationClient;
    private final DateTimeFormatter DATE_HEADER_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    private final RestClient apiClient;

    public QuestradeWebClientImpl(WebClientProperties properties) {

        authenticationClient = RestClient.create(properties.getLoginUrl());
        apiClient = RestClient.create();
    }

    @Override
    public AuthenticationToken authenticate(String refreshToken) {
        log.info("QuestradeWebClient: Calling Questrade API with refresh token: {}", refreshToken);
        ResponseEntity<Authorization> response = authenticationClient.get()
                                                                     .uri(uriBuilder -> uriBuilder.path("/oauth2/token")
                                                                                                  .queryParam("grant_type", "refresh_token")
                                                                                                  .queryParam("refresh_token", refreshToken)
                                                                                                  .build())
                                                                     .retrieve()
                                                                     .toEntity(Authorization.class);

        return createAuthenticationObject(response);
    }

    @Override
    public List<Account> getAccounts(AuthenticationToken authToken) {
        log.info("QuestradeWebClient: Calling Questrade API getAccounts()");
        ResponseEntity<AccountResponse> response = apiClient.get()
                                                            .uri(authToken.api_server() + "/v1/accounts")
                                                            .header("Authorization", "Bearer %s".formatted(authToken.access_token()))
                                                            .retrieve()
                                                            .toEntity(AccountResponse.class);

        return response.getBody().accounts();
    }

    private AuthenticationToken createAuthenticationObject(ResponseEntity<Authorization> response) {
        log.info("QuestradeWebClient: Creating authentication object from API answer");
        Authorization clientAuth = response.getBody();

        if (clientAuth == null) throw new AuthenticationException("Cannot retrieve auth token");

        String dateHeader = response.getHeaders().getFirst("date");
        OffsetDateTime expiresAt = getExpirationDate(dateHeader, clientAuth);
        log.info("QuestradeWebClient: Date received from header: '{}'    local expiration date: {}", dateHeader, expiresAt);

        return new AuthenticationToken(clientAuth.access_token(), clientAuth.api_server(), expiresAt, clientAuth.refresh_token(), clientAuth.token_type());
    }

    private OffsetDateTime getExpirationDate(String dateHeader, Authorization clientAuth) {
        ZoneOffset currentZone = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now());

        ZonedDateTime dateParsed = ZonedDateTime.parse(dateHeader, DATE_HEADER_FORMAT);
        ZonedDateTime localDate = dateParsed.withZoneSameInstant(currentZone);

        return localDate.plusSeconds(clientAuth.expires_in())
                        .toOffsetDateTime();
    }
}
