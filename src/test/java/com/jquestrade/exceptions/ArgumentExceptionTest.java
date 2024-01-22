package com.jquestrade.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class ArgumentExceptionTest {
    @Test
    void canCreate() {
        String reason = "test reason";

        assertThatExceptionOfType(ArgumentException.class)
                .isThrownBy(() -> {throw new ArgumentException(reason);})
                .withMessage(reason);
    }
}