package info.martindupuis.jquestrade.client;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import info.martindupuis.jquestrade.*;
import info.martindupuis.jquestrade.client.config.WebClientProperties;
import info.martindupuis.jquestrade.exceptions.AuthenticationExpiredException;
import info.martindupuis.jquestrade.exceptions.TimeRangeException;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static info.martindupuis.UtilsForTests.RequestPeriodUtils.getInvalidPeriod_ForAccountActivities;
import static info.martindupuis.UtilsForTests.RequestPeriodUtils.getValidPeriod;
import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;

@ExtendWith(InstancioExtension.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
class QuestradeWebClientImplTest {

    private static final String ACCESS_TOKEN = "my-access-token-123";
    private static final String TEST_URL_TEMPLATE = "http://localhost:%d/";

    @RegisterExtension
    static WireMockExtension wiremock =
            WireMockExtension.newInstance()
                    .options(wireMockConfig().usingFilesUnderDirectory("wiremock")
                            .globalTemplating(true)
                            .notifier(new ConsoleNotifier(true))
                    ).build();
    QuestradeWebClient sut;

    @Mock
    WebClientProperties webclientProperties;
    private String testServerUrl;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        testServerUrl = TEST_URL_TEMPLATE.formatted(wiremock.getPort());

        Mockito.when(webclientProperties.getLoginUrl()).thenReturn(testServerUrl);

        sut = new QuestradeWebClientImpl(webclientProperties);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void canInstantiate() {
        assertThat(sut).isNotNull();
    }

    @Test
    void givenValidCredentials_WhenITryToAuthenticate_IGetAnAccessToken() {
        String oldRefreshToken = Instancio.create(String.class);

        AuthenticationToken token = sut.authenticate(oldRefreshToken);
        log.info(token.toString());

        assertThat(token.isValid()).isTrue();
    }

    @Test
    void givenIAmAuthenticated_callingGetAccounts_returnsListOfAccounts() {
        AuthenticationToken authToken = getValidTestAuthToken();

        List<Account> result = sut.getAccounts(authToken);

        assertThat(result).hasSize(2);
        assertThatList(result).first()
                .hasFieldOrPropertyWithValue("type", "TFSA")
                .hasFieldOrPropertyWithValue("number", "99912345");
        assertThatList(result).last()
                .hasFieldOrPropertyWithValue("type", "RRSP")
                .hasFieldOrPropertyWithValue("number", "99912346");
    }

    @Test
    void givenAuthenticationHasExpired_callingGetAccounts_throwsAuthenticationExpiredException() {
        AuthenticationToken authToken = getExpiredTestAuthToken();

        assertThatExceptionOfType(AuthenticationExpiredException.class).isThrownBy(() -> sut.getAccounts(authToken))
                .withMessageContaining("expired");
    }

    @Test
    void givenIAmAuthenticated_callingGetPositionsWithAnAccount_returnsListOfPositions() {
        AuthenticationToken authToken = getValidTestAuthToken();

        Account anAccount = Instancio.create(Account.class);

        List<Position> result = sut.getPositions(authToken, anAccount);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst())
                .hasFieldOrPropertyWithValue("symbol", "THI.TO")
                .hasFieldOrPropertyWithValue("currentPrice", 60.17);
    }

    @Test
    void givenIAmAuthenticated_callingGetQuotes_returnsListOfPricesForPeriod() {
        AuthenticationToken authToken = getValidTestAuthToken();
        RequestPeriod aPeriod = getValidPeriod();
        Position aPosition = Instancio.create(Position.class);

        List<Candle> result = sut.getCandles(authToken, aPosition, aPeriod);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst())
                .hasFieldOrPropertyWithValue("low", 70.3)
                .hasFieldOrPropertyWithValue("high", 70.78)
                .hasFieldOrPropertyWithValue("open", 70.68)
                .hasFieldOrPropertyWithValue("close", 70.73)
                .hasFieldOrPropertyWithValue("volume", 983609);
    }

    @Test
    void givenIAmAuthenticated_callingGetActivities_returnsListOfActivitiesForPeriod() {
        AuthenticationToken authToken = getValidTestAuthToken();
        RequestPeriod aPeriod = getValidPeriod();
        Account anAccount = Instancio.create(Account.class);

        List<Activity> result = sut.getAccountActivities(authToken, anAccount, aPeriod);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst())
                .hasFieldOrPropertyWithValue("type", "Interest");
    }

    @Test
    void givenIAmAuthenticated_callingGetActivitiesWithToBigPeriod_throwsArgumentException() {
        AuthenticationToken authToken = getValidTestAuthToken();
        RequestPeriod aPeriod = getInvalidPeriod_ForAccountActivities();
        Account anAccount = Instancio.create(Account.class);

        assertThatExceptionOfType(TimeRangeException.class).isThrownBy(() -> sut.getAccountActivities(authToken, anAccount, aPeriod))
                .withMessageContaining("Invalid period. Account activities are limited to 30 days.");
    }

    private AuthenticationToken getValidTestAuthToken() {
        return Instancio.of(AuthenticationToken.class)
                .set(field(AuthenticationToken::api_server), testServerUrl)
                .set(field(AuthenticationToken::access_token), ACCESS_TOKEN)
                .generate(field(AuthenticationToken::expires_at),
                        generators -> generators.temporal().zonedDateTime().future())
                .create();
    }

    private AuthenticationToken getExpiredTestAuthToken() {
        return Instancio.of(AuthenticationToken.class)
                .set(field(AuthenticationToken::api_server), testServerUrl)
                .set(field(AuthenticationToken::access_token), ACCESS_TOKEN)
                .generate(field(AuthenticationToken::expires_at), gen -> gen.temporal().zonedDateTime().past())
                .create();
    }
}