package info.martindupuis.jquestrade.client;

import info.martindupuis.UtilsForTests.RequestPeriodUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

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

    @Test
    void givenASmallPeriod_callingSplitIntoMoreDays_returnAListOfOne_containingTheSamePeriod() {
        RequestPeriod bigPeriod = RequestPeriodUtils.getValidPeriod();
//        bigPeriod = new RequestPeriod(bigPeriod.periodStart(), bigPeriod.periodStart().plusYears(1));

        int periodLength = bigPeriod.numberDaysInBetween().intValue();
        List<RequestPeriod> result = bigPeriod.splitIntoPeriodsOfXDays( periodLength);

        assertThat(result).size().isEqualTo(1);
        RequestPeriod resultPeriod = Objects.requireNonNull(result).getFirst();
        assertThat(resultPeriod.numberDaysInBetween()).isEqualTo(periodLength);
    }

    @Test
    void givenAOneYearPeriod_callingSplitIntoLessDays_returnAListOf13Periods() {
        ZonedDateTime periodStart = ZonedDateTime.of(LocalDateTime.of(2001,1,1,0,0), ZoneId.systemDefault());
        RequestPeriod bigPeriod = new RequestPeriod(periodStart, periodStart.plusYears(1));

        List<RequestPeriod> result = bigPeriod.splitIntoPeriodsOfXDays( 30);

        assertThat(result).size().isEqualTo(13);

        RequestPeriod firstPeriod = Objects.requireNonNull(result).getFirst();
        assertThat(firstPeriod.numberDaysInBetween()).isEqualTo(30);

        RequestPeriod lastPeriod = Objects.requireNonNull(result.getLast());
        assertThat(lastPeriod.numberDaysInBetween()).isEqualTo(5);
    }

    @Test
    void givenAPeriodOfTwoSplits_callingSplitIntoLessDays_returnAListOfTwoEvenPeriods() {
        ZonedDateTime periodStart = ZonedDateTime.of(LocalDateTime.of(2001,1,1,0,0), ZoneId.systemDefault());
        RequestPeriod bigPeriod = new RequestPeriod(periodStart, periodStart.plusDays(60));

        List<RequestPeriod> result = bigPeriod.splitIntoPeriodsOfXDays( 30);

        assertThat(result).size().isEqualTo(2);

        RequestPeriod firstPeriod = Objects.requireNonNull(result).getFirst();
        assertThat(firstPeriod.numberDaysInBetween()).isEqualTo(30);

        RequestPeriod lastPeriod = Objects.requireNonNull(result.getLast());
        assertThat(lastPeriod.numberDaysInBetween()).isEqualTo(30);
    }}