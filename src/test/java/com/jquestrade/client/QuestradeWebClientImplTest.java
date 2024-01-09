package com.jquestrade.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jquestrade.Account;
import com.jquestrade.AuthenticationToken;
import com.jquestrade.client.config.WebClientProperties;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.instancio.Select.field;

@ExtendWith(InstancioExtension.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
class QuestradeWebClientImplTest {

    // It appears that Questrade always expires the token in 1800 seconds (30 minutes)
    // add a buffer for test execution time between wiremock response and code
    private static final short DEFAULT_TIMEOUT_SECONDS = 1801;

    // This has to match the wiremock stub response
    private static final short NB_ACCOUNTS = 2;
    private static final String ACCESS_TOKEN = "my-access-token-123";
    private static final String TEST_URL_TEMPLATE = "http://localhost:%d";

    @RegisterExtension
    static WireMockExtension wiremock =
            WireMockExtension.newInstance()
                             .options(wireMockConfig().usingFilesUnderDirectory("wiremock")
                             ).build();
    QuestradeWebClient sut;

    // Use this URL to test a real call (use sparingly, it's rate limited)
//    private static final String TEST_URL = "https://login.questrade.com";

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
        AuthenticationToken authToken = Instancio.of(AuthenticationToken.class)
                                                 .set(field(AuthenticationToken::api_server), testServerUrl)
                                                 .set(field(AuthenticationToken::access_token), ACCESS_TOKEN)
                                                 .create();

        List<Account> result = sut.getAccounts(authToken);

        assertThat(result).hasSize(NB_ACCOUNTS);
        assertThatList(result).first()
                              .hasFieldOrPropertyWithValue("type", "TFSA")
                              .hasFieldOrPropertyWithValue("number", "99912345");
        assertThatList(result).last()
                              .hasFieldOrPropertyWithValue("type", "RRSP")
                              .hasFieldOrPropertyWithValue("number", "99912346");
    }
}