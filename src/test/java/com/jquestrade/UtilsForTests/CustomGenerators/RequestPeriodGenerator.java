package com.jquestrade.UtilsForTests.CustomGenerators;

import com.jquestrade.client.RequestPeriod;
import org.instancio.Instancio;
import org.instancio.Random;
import org.instancio.generator.Generator;

import java.time.ZonedDateTime;

public class RequestPeriodGenerator implements Generator<RequestPeriod> {
    public static final int MAX_DAYS_IN_PERIOD = 30;

    @Override
    public RequestPeriod generate(Random random) {
        int nbDaysInFuture = random.intRange(1, MAX_DAYS_IN_PERIOD);

        ZonedDateTime start = Instancio.create(ZonedDateTime.class);
        ZonedDateTime end = start.plusDays(nbDaysInFuture);

        return new RequestPeriod(start, end);
    }
}
