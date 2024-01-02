package com.jquestrade.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
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
import org.junitpioneer.jupiter.DefaultTimeZone;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(InstancioExtension.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
class QuestradeWebClientImplTest {

    @RegisterExtension
    static WireMockExtension wiremock =
            WireMockExtension.newInstance()
                             .options(wireMockConfig().usingFilesUnderDirectory("wiremock")
                             ).build();


    @Mock
    WebClientProperties webclientProperties;

    QuestradeWebClient sut;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        Mockito.when(webclientProperties.getLoginUrl()).thenReturn("http://localhost:" + wiremock.getPort());

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

        assertThat(token.isValid()).isTrue();
    }

    @Test
    @DefaultTimeZone("PST")
    void givenIAmAuthenticated_WhenReadTheExpirationDate_ItIsInMyTimeZone() {
        // change JVM timezone for this specific test to Pacific Time
        // the date should be between 2 and 3 hours later than the wiremock
        String oldRefreshToken = Instancio.create(String.class);

        AuthenticationToken token = sut.authenticate(oldRefreshToken);

        assertThat(token.expires_at()).isAfterOrEqualTo(OffsetDateTime.now());
    }
}