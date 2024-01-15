package com.jquestrade.client;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

class RequestPeriodTest {
    @Test
    void canCreate_RequestPeriod_StartBeforeEnd() {
        assertThatNoException().isThrownBy(() -> Instancio.create(RequestPeriod.class));
    }

    @Test
    void canCreate_RequestPeriod_StartEqualEnd() {
        OffsetDateTime testDate = Instancio.create(OffsetDateTime.class);

        assertThatNoException().isThrownBy(() -> new RequestPeriod(testDate, testDate));
    }

    @Test
    void cannotCreate_RequestPeriod_StartAfterEnd() {
        OffsetDateTime startDate = Instancio.create(OffsetDateTime.class);
        OffsetDateTime endDate = startDate.minusSeconds(30);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new RequestPeriod(startDate, endDate))
                                                                 .withMessageContaining("must be before or equal to End date");
    }


}