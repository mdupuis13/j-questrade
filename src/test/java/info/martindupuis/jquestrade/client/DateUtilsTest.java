package info.martindupuis.jquestrade.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import info.martindupuis.UtilsForTests.MemoryAppender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Slf4j
class DateUtilsTest {
    private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "info.martindupuis.jquestrade.client.DateUtils";

    @BeforeAll
    static void setUpAll() {
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);

        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @BeforeEach
    void setUp() {
        memoryAppender.reset();
    }
    @Test
    void dateFromQuestradeHeader_whenTranslated_isAtLocalDateTime() {
        String dateReceived = ZonedDateTime.now(ZoneId.of("GMT")).format(DateUtils.DATE_HEADER_FORMAT);

        ZonedDateTime result = DateUtils.parseHeaderDateToLocalOffsetDateTime(dateReceived);

        log.info("dateReceived: %s    result: %s".formatted(dateReceived, result));

        assertThat(result).isCloseTo(ZonedDateTime.now(), within(5, ChronoUnit.SECONDS));
        // we should get one message logged, and only one
        assertThat(memoryAppender.list).hasSize(1);
        assertThat(memoryAppender.search("Date received from header")).hasSize(1);
    }
}