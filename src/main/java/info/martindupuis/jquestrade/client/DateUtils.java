package info.martindupuis.jquestrade.client;

import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
class DateUtils {
    static final DateTimeFormatter DATE_HEADER_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz",
            Locale.US);

    public static ZonedDateTime parseHeaderDateToLocalOffsetDateTime(String dateReceived) {
        ZonedDateTime dateParsed = ZonedDateTime.parse(dateReceived, DATE_HEADER_FORMAT);
        ZonedDateTime localDate = dateParsed.withZoneSameInstant(ZoneId.systemDefault());

        log.debug(
                "QuestradeWebClient: ZoneID is {}   Date received from header: '{}'  date parsed: {}  local expiration date: {}",
                ZoneId.systemDefault(),
                dateReceived,
                dateParsed,
                localDate);
        return localDate;
    }
}
