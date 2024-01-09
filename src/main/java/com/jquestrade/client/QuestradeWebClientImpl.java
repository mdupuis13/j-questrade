package com.jquestrade.client;

import com.jquestrade.Account;
import com.jquestrade.AuthenticationToken;
import com.jquestrade.Position;
import com.jquestrade.client.config.WebClientProperties;
import com.jquestrade.exceptions.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
public class QuestradeWebClientImpl implements QuestradeWebClient {

    public static final String API_V1_TEMPLATE = "%s/v1/%s";


    private final RestClient authenticationClient;
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
        ResponseEntity<AccountResponse> response =
                callQuestrade(authToken, "accounts").toEntity(AccountResponse.class);

        return response.getBody() == null ? Collections.emptyList() : response.getBody().accounts();
    }

    @Override
    public List<Position> getPositions(AuthenticationToken authToken, Account account) {
        log.info("QuestradeWebClient: Calling Questrade API getPositions()");
        ResponseEntity<PositionsResponse> response =
                callQuestrade(authToken, "accounts/%s/positions".formatted(account.number())).toEntity(PositionsResponse.class);

        return response.getBody() == null ? Collections.emptyList() : response.getBody().positions();
    }

    private RestClient.ResponseSpec callQuestrade(AuthenticationToken authToken, String ressource) {

        return apiClient.get()
                        .uri(API_V1_TEMPLATE.formatted(authToken.api_server(), ressource))
                        .header("Authorization", "Bearer %s".formatted(authToken.access_token()))
                        .retrieve();
    }

    private AuthenticationToken createAuthenticationObject(ResponseEntity<Authorization> response) {
        log.info("QuestradeWebClient: Creating authentication object from API answer");
        Authorization clientAuth = response.getBody();

        if (clientAuth == null) throw new AuthenticationException("Cannot retrieve auth token");

        String dateHeader = response.getHeaders().getFirst("date");
        OffsetDateTime expiresAt = getExpirationDate(dateHeader, clientAuth);

        return new AuthenticationToken(clientAuth.access_token(), clientAuth.api_server(), expiresAt, clientAuth.refresh_token(), clientAuth.token_type());
    }

    private OffsetDateTime getExpirationDate(String dateHeader, Authorization clientAuth) {
        OffsetDateTime localDate = DateUtils.parseHeaderDateToLocalOffsetDateTime(dateHeader);

        return localDate.plusSeconds(clientAuth.expires_in());
    }
}
