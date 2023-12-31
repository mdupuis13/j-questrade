package com.jquestrade;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;

class AuthenticationTokenTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void givenNoAccessToken_isValid_returnsFalse(String accessToken) {
        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                                           .set(field(AuthenticationToken::access_token), accessToken)
                                           .create();

        assertThat(sut.isValid()).isFalse();
    }

    @Test
    void givenAValidAccessToken_isValid_returnsTrue() {
        AuthenticationToken sut = Instancio.create(AuthenticationToken.class);

        assertThat(sut.isValid()).isTrue();
    }

    @Test
    void givenAuthValid_andTimeNotExpired_isValid_returnsTrue() {
        OffsetDateTime tokenDateTime = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).plusSeconds(1);

        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                                           .set(field(AuthenticationToken::expires_at), tokenDateTime)
                                           .create();

        assertThat(sut.isValid()).isTrue();
    }

    @Test
    void givenAuthValid_andTimeIsExpired_isValid_returnsFalse() {
        OffsetDateTime tokenDateTime = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).minusSeconds(1);

        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                                           .set(field(AuthenticationToken::expires_at), tokenDateTime)
                                           .create();

        assertThat(sut.isValid()).isFalse();
    }
}