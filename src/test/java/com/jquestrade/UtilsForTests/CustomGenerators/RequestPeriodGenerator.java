package com.jquestrade.UtilsForTests.CustomGenerators;

import com.jquestrade.client.RequestPeriod;
import org.instancio.Instancio;
import org.instancio.Random;
import org.instancio.generator.Generator;

import java.time.OffsetDateTime;

public class RequestPeriodGenerator implements Generator<RequestPeriod> {

    @Override
    public RequestPeriod generate(Random random) {
        OffsetDateTime start = Instancio.create(OffsetDateTime.class);
        OffsetDateTime end = start.plusDays(30);

        return new RequestPeriod(start, end);
    }
}
