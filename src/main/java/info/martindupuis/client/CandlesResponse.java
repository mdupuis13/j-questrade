package info.martindupuis.client;

import info.martindupuis.Candle;

import java.util.List;

public record CandlesResponse(List<Candle> candles,
                              int userId) {}
