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
import org.junit.jupiter.api.Nested;
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
    static WireMockExtension wiremock = WireMockExtension.newInstance()
            .options(wireMockConfig().usingFilesUnderDirectory("wiremock")
                    .globalTemplating(true)
                    .notifier(new ConsoleNotifier(true)))
            .build();
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
    void givenValidCredentials_WhenITryToAuthenticate_IGetAnAccessToken() {
        String oldRefreshToken = Instancio.create(String.class);

        AuthenticationToken token = sut.authenticate(oldRefreshToken);
        log.info("Token is {}", token.toString());

        assertThat(token.isValid()).isTrue();
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
    
    @Nested
    class givenIAmAuthenticated {
        private AuthenticationToken validAuthToken;

        @BeforeEach
        void init() {
            validAuthToken = getValidTestAuthToken();
        }

        @Test
        void callingGetAccounts_returnsListOfAccounts() {
            List<Account> result = sut.getAccounts(validAuthToken).stream().toList();

            assertThat(result).hasSize(2);
            assertThatList(result).filteredOn(e -> e.number().equals("99912345")).first()
                    .hasFieldOrPropertyWithValue("type", "TFSA");
            assertThatList(result).filteredOn(e -> e.number().equals("99912346")).first()
                    .hasFieldOrPropertyWithValue("type", "RRSP");
        }

        @Test
        void givenAuthenticationHasExpired_callingGetAccounts_throwsAuthenticationExpiredException() {
            AuthenticationToken expiredAuthToken = getExpiredTestAuthToken();

            assertThatExceptionOfType(AuthenticationExpiredException.class)
                    .isThrownBy(() -> sut.getAccounts(expiredAuthToken))
                    .withMessageContaining("expired");
        }

        @Test
        void callingGetActivities_returnsListOfActivitiesForPeriod() {
            RequestPeriod aPeriod = getValidPeriod();
            Account anAccount = Instancio.create(Account.class);

            List<Activity> result = sut.getAccountActivities(validAuthToken, anAccount, aPeriod).stream().toList();

            assertThat(result).hasSize(1);
            assertThat(result.getFirst())
                    .hasFieldOrPropertyWithValue("type", "Interest");
        }

        @Test
        void callingGetActivitiesWithTooBigPeriod_throwsArgumentException() {
            RequestPeriod aPeriodOfMoreThan30Days = getInvalidPeriod_ForAccountActivities();
            Account anAccount = Instancio.create(Account.class);

            assertThatExceptionOfType(TimeRangeException.class)
                    .isThrownBy(() -> sut.getAccountActivities(validAuthToken, anAccount, aPeriodOfMoreThan30Days))
                    .withMessageContaining("Invalid period. Account activities are limited to 30 days.");
        }

        @Test
        void callingGetPositionsWithAnAccount_returnsListOfPositions() {
            Account anAccount = Instancio.create(Account.class);

            List<Position> result = sut.getPositions(validAuthToken, anAccount).stream().toList();

            assertThat(result).hasSize(1);
            assertThat(result.getFirst())
                    .hasFieldOrPropertyWithValue("symbol", "THI.TO")
                    .hasFieldOrPropertyWithValue("currentPrice", 60.17);
        }

        @Test
        void callingGetQuotes_returnsListOfPricesForPeriod() {
            RequestPeriod aPeriod = getValidPeriod();
            Position aPosition = Instancio.create(Position.class);

            List<Candle> result = sut.getPositionCandles(validAuthToken, aPosition, aPeriod).stream().toList();

            assertThat(result).hasSize(1);
            assertThat(result.getFirst())
                    .hasFieldOrPropertyWithValue("low", 70.3)
                    .hasFieldOrPropertyWithValue("high", 70.78)
                    .hasFieldOrPropertyWithValue("open", 70.68)
                    .hasFieldOrPropertyWithValue("close", 70.73)
                    .hasFieldOrPropertyWithValue("volume", 983609);
        }
    }
}