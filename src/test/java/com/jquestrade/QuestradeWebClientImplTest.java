package com.jquestrade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestradeWebClientImplTest {

    public static final String MY_API_SERVER = "MyApiServer";
    public static final String MY_ACCESS_TOKEN = "MyAccessToken";
    public static final String MY_REFRESH_TOKEN = "MyRefreshToken";
    QuestradeWebClient sut;

    @BeforeEach
    void setUp() {
        sut = new QuestradeWebClientImpl(MY_REFRESH_TOKEN, MY_ACCESS_TOKEN, MY_API_SERVER);
    }

    @Test
    void canInstantiate() {
        assertThat(sut).isNotNull();
    }

/*
    @Test
    void getAuthorization() {
        Authorization auth = sut.getAuthorization();

        assertThat(auth.getAccess_token()).isEqualTo(MY_ACCESS_TOKEN);
        assertThat(auth.getRefresh_token()).isEqualTo(MY_REFRESH_TOKEN);
        assertThat(auth.getApi_server()).isEqualTo(MY_API_SERVER);
    }
*/
}