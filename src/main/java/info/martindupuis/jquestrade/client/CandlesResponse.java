package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Candle;

import java.util.Set;

public record CandlesResponse(
        Set<Candle> candles,
        int userId) {}
