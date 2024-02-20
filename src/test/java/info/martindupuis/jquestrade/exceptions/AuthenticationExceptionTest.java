package info.martindupuis.jquestrade.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AuthenticationExceptionTest {
    @Test
    void canCreate() {
        String reason = "test reason";

        assertThatExceptionOfType(AuthenticationException.class)
                .isThrownBy(() -> {throw new AuthenticationException(reason);})
                .withMessage(reason);
    }


}