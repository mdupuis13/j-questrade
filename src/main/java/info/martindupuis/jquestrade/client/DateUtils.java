package info.martindupuis.jquestrade.client;

import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
class DateUtils {
    static final DateTimeFormatter DATE_HEADER_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    public static ZonedDateTime parseHeaderDateToLocalOffsetDateTime(String dateReceived) {
        ZonedDateTime dateParsed = ZonedDateTime.parse(dateReceived, DATE_HEADER_FORMAT);
        ZonedDateTime localDate = dateParsed.withZoneSameInstant(ZoneId.systemDefault());

        log.info("QuestradeWebClient: Date received from header: '{}'    local expiration date: {}", dateReceived, localDate);
        return localDate;
    }
}
