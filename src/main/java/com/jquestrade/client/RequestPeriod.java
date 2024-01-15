package com.jquestrade.client;

import java.time.OffsetDateTime;

/**
 * Represent a request period
 *
 * @param periodStart   Start date of the period
 * @param periodEnd End date of the period, must happen after {@param periodStart}
 */
public record RequestPeriod (OffsetDateTime periodStart,
                             OffsetDateTime periodEnd) {
    public RequestPeriod {
        if (periodEnd.isBefore(periodStart))
            throw new IllegalArgumentException("Start date (%s) must be before or equal to End date (%s).".formatted(periodStart,periodEnd));
    }
}
