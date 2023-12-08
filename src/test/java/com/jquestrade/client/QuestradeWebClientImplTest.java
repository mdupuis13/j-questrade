package com.jquestrade.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(InstancioExtension.class)
class QuestradeWebClientImplTest {

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
                                                         .options(wireMockConfig().dynamicPort()
                                                                                  .usingFilesUnderDirectory("wiremock")
                                                         ).build();


    QuestradeWebClient sut;

    @BeforeEach
    void setUp() {
        sut = new QuestradeWebClientImpl();
    }

    @Test
    void canInstantiate() {
        assertThat(sut).isNotNull();
    }

    @Test
    void givenValidCredentials_WhenITryToAuthenticate_IGetAnAccessToken() {
        String oldRefreshToken = Instancio.create(String.class);
        String oldAccessToken = Instancio.create(String.class);
        String oldAPIServerUrl = Instancio.create(String.class);

        Authorization auth = sut.authenticate(oldRefreshToken, oldAccessToken, oldAPIServerUrl);

        assertThat(auth.access_token()).isEqualTo(oldAccessToken);
        assertThat(auth.refresh_token()).isEqualTo(oldRefreshToken);
        assertThat(auth.api_server()).isEqualTo(oldAPIServerUrl);
    }

}