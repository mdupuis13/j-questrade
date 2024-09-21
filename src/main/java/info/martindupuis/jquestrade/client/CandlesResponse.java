package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.QuestradeCandle;

import java.util.Set;

public record CandlesResponse(
        Set<QuestradeCandle> candles,
        int userId) {}
