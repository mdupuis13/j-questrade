package com.jquestrade.client;

import com.jquestrade.Candle;

import java.util.List;

public record CandlesResponse(List<Candle> candles,
                              int userId) {}
