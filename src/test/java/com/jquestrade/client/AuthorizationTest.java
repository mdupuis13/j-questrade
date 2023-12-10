package com.jquestrade.client;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;

class AuthorizationTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void givenNoAccessToken_isValid_isFalse(String accessToken) {
        Authorization sut = Instancio.of(Authorization.class)
                                     .set(field(Authorization::access_token), accessToken)
                                     .create();

        assertThat(sut.isValid()).isFalse();
    }

    @Test
    void givenSutHasAValidAccessToken_isValid_isTrue() {
        Authorization sut = Instancio.of(Authorization.class)
                                     .create();

        assertThat(sut.isValid()).isTrue();
    }

}