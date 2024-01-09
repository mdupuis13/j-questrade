package com.jquestrade.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.jquestrade.client.DateUtils.DATE_HEADER_FORMAT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.jquestrade.UtilsForTests.MemoryAppender;

class DateUtilsTest {
    private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "com.jquestrade.client.DateUtils";

    @BeforeAll
    static void setUpAll() {
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);

        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @BeforeEach
    void setUp() {
        memoryAppender.reset();
    }
    @Test
    void dateFromQuestradeHeader_whenTranslated_isAtLocalDateTime() {
        String dateReceived = ZonedDateTime.now(ZoneId.of("GMT")).format(DATE_HEADER_FORMAT);

        OffsetDateTime result = DateUtils.parseHeaderDateToLocalOffsetDateTime(dateReceived);

        assertThat(result.toLocalDateTime()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
        // we should get one message logged, and only one
        assertThat(memoryAppender.list).hasSize(1);
        assertThat(memoryAppender.search("Date received from header")).hasSize(1);
    }
}