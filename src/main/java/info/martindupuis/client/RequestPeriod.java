package info.martindupuis.client;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

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

    public Long getDaysInBetween() {
        return ChronoUnit.DAYS.between(periodStart, periodEnd);
    }
}
