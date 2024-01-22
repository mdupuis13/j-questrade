package info.martindupuis.UtilsForTests;

import info.martindupuis.UtilsForTests.CustomGenerators.RequestPeriodGenerator;
import info.martindupuis.client.RequestPeriod;
import org.instancio.Instancio;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.instancio.Select.all;

public class RequestPeriodUtils {
    /**
     * Hacky way to use a generator for the RequestPeriod
     *
     * @return A valid {@link RequestPeriod}
     */
    public static RequestPeriod getValidPeriod() {
        class TempPeriod {
            public RequestPeriod period;
        }

        TempPeriod temp = Instancio.of(TempPeriod.class)
                                   .supply(all(RequestPeriod.class), new RequestPeriodGenerator())
                                   .create();

        return temp.period;
    }

    public static RequestPeriod getInvalidPeriod_ForAccountActivities() {
        return new RequestPeriod(ZonedDateTime.now()
                                              .withZoneSameLocal(ZoneId.systemDefault())
                                              .minusDays(40),
                                 ZonedDateTime.now()
                                              .withZoneSameLocal(ZoneId.systemDefault()));
    }
}
