package com.jquestrade;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpsEnabled = true, httpsPort = 8443)
class QuestradeWebClientImplTest {

    public static final String MY_API_SERVER = "MyApiServer";
    public static final String MY_ACCESS_TOKEN = "MyAccessToken";
    public static final String MY_REFRESH_TOKEN = "MyRefreshToken";
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
        String oldRefreshToken = "old_refresh_token";
        String oldAccessToken = "old_access_token";
        String oldAPIServerUrl = "old_server_url";



        Authorization auth = sut.authenticate(oldRefreshToken, oldAccessToken, oldAPIServerUrl);

        assertThat(auth.getAccess_token()).isEqualTo(oldAccessToken);
        assertThat(auth.getRefresh_token()).isEqualTo(oldRefreshToken);
        assertThat(auth.getApi_server()).isEqualTo(oldAPIServerUrl);
    }

}