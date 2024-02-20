package info.martindupuis.jquestrade;

/**
 * Represents historical market data in the form of OHLC candlesticks for a specified symbol.
 *
 * @param start  Returns the candlestick start timestamp (in ISO format).
 * @param end    Returns the candlestick end timestamp (in ISO format).
 * @param low    Returns the lowest price during this candle.
 * @param high   Returns the highest price during this candle.
 * @param open   Returns the opening price for this candle.
 * @param close  Returns the closing price for this candle.
 * @param volume Returns the trading volume for this candle.
 * @param VWAP   Returns the Volume Weighted Average Price.
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/markets-candles-id">
 * Candles API documentation</a> for more insight.
 */
public record Candle(
        String start,
        String end,
        double low,
        double high,
        double open,
        double close,
        int volume,
        double VWAP) {

    /**
     * Represents an interval for a set of candles.
     *
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/enumerations/enumerations#historical-data-granularity">
     * The Historical Data Granularity (candle interval) documentation</a>
     */
    public enum Interval {
        OneMinute,
        TwoMinutes,
        ThreeMinutes,
        FourMinutes,
        FiveMinutes,
        TenMinutes,
        FifteenMinutes,
        TwentyMinutes,
        HalfHour,
        OneHour,
        TwoHours,
        FourHours,
        OneDay,
        OneWeek,
        OneMonth,
        OneYear
    }
}
