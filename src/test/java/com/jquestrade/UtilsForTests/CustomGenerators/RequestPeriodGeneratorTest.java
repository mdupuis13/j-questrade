package com.jquestrade.UtilsForTests.CustomGenerators;

import com.jquestrade.client.RequestPeriod;
import org.instancio.Instancio;
import org.junit.jupiter.api.RepeatedTest;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.all;

class RequestPeriodGeneratorTest {
    RequestPeriodGenerator sut = new RequestPeriodGenerator();

    @RepeatedTest(30)
    void generate_RequestPeriod_End1To30DaysAfterStart() {
        CustomPeriod testPeriod = Instancio.of(CustomPeriod.class)
                                           .supply(all(RequestPeriod.class), sut)
                                           .create();

        RequestPeriod result = testPeriod.period;

        assertThat(result.periodStart()).isBefore(result.periodEnd());
        ZonedDateTime lastDayInRange = result.periodStart().plusDays(RequestPeriodGenerator.MAX_DAYS_IN_PERIOD);
        assertThat(result.periodEnd()).isBeforeOrEqualTo(lastDayInRange);
    }

    static class CustomPeriod {
        RequestPeriod period;
    }
}