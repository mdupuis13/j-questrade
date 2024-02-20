package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Candle;

import java.util.List;

public record CandlesResponse(List<Candle> candles,
                              int userId) {}
