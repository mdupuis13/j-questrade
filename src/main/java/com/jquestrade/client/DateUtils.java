package com.jquestrade.client;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
class DateUtils {
    static final DateTimeFormatter DATE_HEADER_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    public static OffsetDateTime parseHeaderDateToLocalOffsetDateTime(String dateReceived) {

        ZoneOffset currentZone = ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now());

        ZonedDateTime dateParsed = ZonedDateTime.parse(dateReceived, DATE_HEADER_FORMAT);
        OffsetDateTime localDate = dateParsed.withZoneSameInstant(currentZone).toOffsetDateTime();

        log.info("QuestradeWebClient: Date received from header: '{}'    local expiration date: {}", dateReceived, localDate);
        return localDate;
    }
}
