package info.martindupuis.jquestrade.client;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a request period
 *
 * @param periodStart Start date of the period
 * @param periodEnd   End date of the period, must happen after {@param periodStart}
 */
public record RequestPeriod(ZonedDateTime periodStart,
                            ZonedDateTime periodEnd) {
    public RequestPeriod {
        if (periodEnd.isBefore(periodStart))
            throw new IllegalArgumentException("Start date (%s) must be before or equal to End date (%s).".formatted(periodStart, periodEnd));
    }

    public Long numberDaysInBetween() {
        return ChronoUnit.DAYS.between(periodStart, periodEnd);
    }


    public List<RequestPeriod> splitIntoPeriodsOfXDays(int xDays) {
        if (this.numberDaysInBetween() <= xDays)
            return List.of(this);

        int nbPeriods = (int) (this.numberDaysInBetween() / xDays);
        // add a period if the actual period is not a multiple of xDays
        nbPeriods += this.numberDaysInBetween() % xDays == 0 ? 0 : 1;


        List<RequestPeriod> periods = new ArrayList<>();

        for (int periodX = 0; periodX < nbPeriods; periodX++) {
            ZonedDateTime newPeriodStart = periodStart.plusDays((long) periodX * xDays);
            ZonedDateTime newPeriodEnd = newPeriodStart.plusDays(xDays);

            if (newPeriodEnd.isAfter(periodEnd))
                newPeriodEnd = periodEnd;

            periods.add(new RequestPeriod(newPeriodStart, newPeriodEnd));
        }

        return periods;
    }

}
