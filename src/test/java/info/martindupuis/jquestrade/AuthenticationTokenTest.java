package info.martindupuis.jquestrade;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                                           .generate(field("expires_at"), gen -> gen.temporal().zonedDateTime().future())
                                           .create();

        assertThat(sut.isValid()).isTrue();
    }

    @Test
    void givenAuthValid_andTimeNotExpired_isValid_returnsTrue() {
        ZonedDateTime tokenDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).plusSeconds(1);

        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                                           .set(field(AuthenticationToken::expires_at), tokenDateTime)
                                           .create();

        assertThat(sut.isValid()).isTrue();
    }

    @Test
    void givenAuthValid_andTimeIsExpired_isValid_returnsFalse() {
        ZonedDateTime tokenDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).minusSeconds(1);

        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                                           .set(field(AuthenticationToken::expires_at), tokenDateTime)
                                           .create();

        assertThat(sut.isValid()).isFalse();
    }

    @Test
    void givenAuthValid_andTimeNotExpired_isExpired_returnsfalse() {
        ZonedDateTime tokenDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).plusSeconds(1);

        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                .set(field(AuthenticationToken::expires_at), tokenDateTime)
                .create();

        assertThat(sut.isExpired()).isFalse();
    }

    @Test
    void givenAuthValid_andTimeIsExpired_isExpired_returnsTue() {
        ZonedDateTime tokenDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC).minusSeconds(1);

        AuthenticationToken sut = Instancio.of(AuthenticationToken.class)
                .set(field(AuthenticationToken::expires_at), tokenDateTime)
                .create();

        assertThat(sut.isExpired()).isTrue();
    }

}