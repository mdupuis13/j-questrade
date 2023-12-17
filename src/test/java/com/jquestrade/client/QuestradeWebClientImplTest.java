package com.jquestrade.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jquestrade.client.config.WebclientProperties;
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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(InstancioExtension.class)
@ExtendWith(MockitoExtension.class)
class QuestradeWebClientImplTest {

    @RegisterExtension
    static WireMockExtension wiremock =
            WireMockExtension.newInstance()
                             .options(wireMockConfig().usingFilesUnderDirectory("wiremock")
                             ).build();


    @Mock
    WebclientProperties webclientProperties;

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

        sut.authenticate(oldRefreshToken);

        assertThat(sut.isAuthenticated()).isTrue();
    }

}