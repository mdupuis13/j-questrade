package com.jquestrade.client;

import java.time.OffsetDateTime;

public record RequestPeriod (OffsetDateTime periodStart,
                             OffsetDateTime periodEnd) {
    public RequestPeriod {
        if (periodStart.isAfter(periodEnd))
            throw new IllegalArgumentException("Start date (%s) must be before or equal to End date (%s).".formatted(periodStart,periodEnd));
    }
}
