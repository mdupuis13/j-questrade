package info.martindupuis.jquestrade.client;

import info.martindupuis.UtilsForTests.RequestPeriodUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.*;

class RequestPeriodTest {
    @Test
    void canCreate_RequestPeriod_StartBeforeEnd() {
        assertThatNoException().isThrownBy(() -> Instancio.create(RequestPeriod.class));
    }

    @Test
    void canCreate_RequestPeriod_StartEqualEnd() {
        ZonedDateTime testDate = Instancio.create(ZonedDateTime.class);

        assertThatNoException().isThrownBy(() -> new RequestPeriod(testDate, testDate));
    }

    @Test
    void cannotCreate_RequestPeriod_StartAfterEnd() {
        ZonedDateTime startDate = Instancio.create(ZonedDateTime.class);
        ZonedDateTime endDate = startDate.minusSeconds(30);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new RequestPeriod(startDate, endDate))
                                                                 .withMessageContaining("must be before or equal to End date");
    }

    @Test
    void givenValidPeriod_callingGetInterval_returnsTheIntervalBetweenStartAndEnd() {
        RequestPeriod period = RequestPeriodUtils.getValidPeriod();

        Long result = period.numberDaysInBetween();

        assertThat(result).isNotZero()
                          .isPositive();
    }
}