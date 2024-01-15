package com.jquestrade.UtilsForTests.CustomGenerators;

import com.jquestrade.client.RequestPeriod;
import org.instancio.Instancio;
import org.instancio.Random;
import org.instancio.generator.Generator;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class RequestPeriodGenerator implements Generator<RequestPeriod> {
    public static final int MAX_DAYS_IN_PERIOD = 30;
    public static final int NB_LEGAL_YEARS_TO_KEEP = 7;
    private static final LocalDateTime LAST_POSSIBLE_DATE = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime FIRST_POSSIBLE_DATE = LAST_POSSIBLE_DATE.minusYears(NB_LEGAL_YEARS_TO_KEEP);

    @Override
    public RequestPeriod generate(Random random) {
        int nbDaysInFuture = random.intRange(1, MAX_DAYS_IN_PERIOD);

        OffsetDateTime start = Instancio.create(OffsetDateTime.class)
                                        .withOffsetSameLocal(ZoneOffset.ofHours(-5));
        OffsetDateTime end = start.plusDays(nbDaysInFuture);

        return new RequestPeriod(start, end);
    }
}
