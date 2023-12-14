package com.jquestrade.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(InstancioExtension.class)
@ActiveProfiles("test")
class QuestradeWebClientImplTest {

    @RegisterExtension
    static WireMockExtension wiremock =
            WireMockExtension.newInstance()
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

//    @Test
    void givenValidCredentials_WhenITryToAuthenticate_IGetAnAccessToken() {
        String oldRefreshToken = Instancio.create(String.class);

        sut.authenticate(oldRefreshToken);

        assertThat(sut.isAuthenticated()).isTrue();
    }

}