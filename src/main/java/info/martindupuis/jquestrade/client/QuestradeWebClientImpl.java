package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.*;
import info.martindupuis.jquestrade.client.config.WebClientProperties;
import info.martindupuis.jquestrade.exceptions.AuthenticationException;
import info.martindupuis.jquestrade.exceptions.AuthenticationExpiredException;
import info.martindupuis.jquestrade.exceptions.TimeRangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
public class QuestradeWebClientImpl implements QuestradeWebClient {

    private static final String API_V1_TEMPLATE = "%sv1/%s";
    private static final DateTimeFormatter DATE_FORMATTER_FOR_URL = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter DATE_FORMATTER_FOR_LOG = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Date formatter object for converting <code>ZonedDateTime</code> objects to strings in the
     * ISO 8601 time format.
     */
    //private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final RestClient authenticationClient;
    private final RestClient apiClient;

    public QuestradeWebClientImpl(WebClientProperties properties) {

        authenticationClient = RestClient.create(properties.getLoginUrl());
        apiClient = RestClient.create();
    }

    @Override
    public AuthenticationToken authenticate(String refreshToken) {
        log.info("QuestradeWebClient: Calling Questrade API with refresh token: {}", refreshToken);

        ResponseEntity<AuthorizationResponse> response = authenticationClient.get()
                                                                             .uri(uriBuilder -> uriBuilder.path("/oauth2/token")
                                                                                                          .queryParam("grant_type", "refresh_token")
                                                                                                          .queryParam("refresh_token", refreshToken)
                                                                                                          .build())
                                                                             .retrieve()
                                                                             .toEntity(AuthorizationResponse.class);

        return createAuthenticationObject(response);
    }

    @Override
    public Set<QuestradeAccount> getAccounts(AuthenticationToken authToken) {
        log.info("QuestradeWebClient: entryPoint=getAccounts");

        ResponseEntity<AccountResponse> response =
                callQuestrade(authToken, "accounts").toEntity(AccountResponse.class);

        return response.getBody() == null ? Collections.emptySet() : response.getBody().accounts();
    }

    @Override
    public Set<QuestradePosition> getPositions(AuthenticationToken authToken, QuestradeAccount account) {
        log.info("QuestradeWebClient: entryPoint=getPositions account=*****");

        ResponseEntity<PositionsResponse> response =
                callQuestrade(authToken, "accounts/%s/positions".formatted(account.number())).toEntity(PositionsResponse.class);

        return response.getBody() == null ? Collections.emptySet() : response.getBody().positions();
    }

    @Override
    public Set<QuestradeCandle> getPositionCandles(AuthenticationToken authToken, QuestradePosition position, RequestPeriod period) {
        log.info("QuestradeWebClient: entryPoint=getCandles position={}, requestPeriod={}", position.symbol(), period);

        //  v1/markets/candles/38738?startTime=2014-10-01T00:00:00-05:00&endTime=2014-10-20T23:59:59-05:00&interval=OneDay
        String url = "markets/candles/%s?startTime=%s&endTime=%s&interval=OneDay".formatted(position.symbolId(),
                                                                                            period.periodStart().format(DATE_FORMATTER_FOR_URL),
                                                                                            period.periodEnd().format(DATE_FORMATTER_FOR_URL));

        ResponseEntity<CandlesResponse> response = callQuestrade(authToken, url).toEntity(CandlesResponse.class);

        return response.getBody() == null ? Collections.emptySet() : response.getBody().candles();
    }

    @Override
    public Set<QuestradeActivity> getAccountActivities(AuthenticationToken authToken, QuestradeAccount account, RequestPeriod period) {
        log.info("QuestradeWebClient: entryPoint=getAccountActivities (token, account=***** requestPeriod=%{}", period);

        if (period.numberDaysInBetween() < 1 || period.numberDaysInBetween() > 29)
            throw new TimeRangeException("Invalid period. Account activities are limited to 30 days. Start: %s  End: %s"
                                                 .formatted(period.periodEnd().format(DATE_FORMATTER_FOR_LOG),
                                                            period.periodEnd().format(DATE_FORMATTER_FOR_LOG)));

        String url = "accounts/%s/activities?startTime=%s&endTime=%s".formatted(account.number(),
                                                                                period.periodStart().format(DATE_FORMATTER_FOR_URL),
                                                                                period.periodEnd().format(DATE_FORMATTER_FOR_URL));
        ResponseEntity<AccountActivityResponse> response = callQuestrade(authToken, url).toEntity(AccountActivityResponse.class);

        return response.getBody() == null ? Collections.emptySet() : response.getBody().activities();
    }

    private RestClient.ResponseSpec callQuestrade(AuthenticationToken authToken, String resource) {
        if (authToken.isExpired())
            throw new AuthenticationExpiredException("Authentication has expired at %s".formatted(authToken.expires_at()));

        log.info("QuestradeWebClient: action=callQuestrade(..., {})", resource);

        String uri = API_V1_TEMPLATE.formatted(authToken.api_server(), resource);
        String authHeader = authToken.getAuthHeader();
        log.debug("QuestradeWebClient: uri={} header={}", uri, authHeader);

        return apiClient.get()
                        .uri(uri)
                        .header("Authorization", authHeader)
                        .retrieve();
    }

    private AuthenticationToken createAuthenticationObject(ResponseEntity<AuthorizationResponse> response) {
        log.info("QuestradeWebClient: action=createAuthenticationObjectFromAnswer");
        AuthorizationResponse clientAuth = response.getBody();

        if (clientAuth == null) throw new AuthenticationException("Cannot retrieve auth token");

        String dateHeader = response.getHeaders().getFirst("date");
        ZonedDateTime expiresAt = getExpirationDate(dateHeader, clientAuth);

        return new AuthenticationToken(clientAuth.access_token(), clientAuth.api_server(), expiresAt, clientAuth.refresh_token(), clientAuth.token_type());
    }

    private ZonedDateTime getExpirationDate(String dateHeader, AuthorizationResponse clientAuth) {
        ZonedDateTime localDate = DateUtils.parseHeaderDateToLocalOffsetDateTime(dateHeader);

        return localDate.plusSeconds(clientAuth.expires_in());
    }
}
