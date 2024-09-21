package info.martindupuis.jquestrade;

/**
 * Represents information about a symbol/ticker
 *
 * @param symbol             Returns the stock symbol/ticker.
 * @param symbolId           Returns the internal unique symbol identifier.
 * @param prevDayClosePrice  Returns the closing trade price from the previous trading day.
 * @param highPrice52        Returns the 52-week-high price.
 * @param lowPrice52         Returns the 52-week-low price.
 * @param averageVol3Months  Returns the average trading volume over trailing 3 months.
 * @param averageVol20Days   Returns the average trading volume over trailing 20 days.
 * @param outstandingShares  Returns the total number of shares outstanding.
 * @param eps                Returns the trailing 12-month earnings per share.
 * @param pe                 Returns the trailing 12-month price to earnings ratio.
 * @param dividend           Returns the dividend amount per share.
 * @param symbolYield        Returns the dividend yield (dividend / prevDayClosePrice).
 * @param exDate             Returns the dividend ex-date.
 * @param marketCap          Returns the market capitalization (outstandingShares * prevDayClosePrice).
 * @param tradeUnit          Returns the number of shares of a particular security that is used as the acceptable quantity for trading
 * @param optionType         Returns the option duration type (e.g., "Weekly").
 * @param optionDurationType Returns the option type (e.g., "Call").
 * @param optionRoot         Returns the option root symbol (e.g., "MSFT").
 * @param optionExerciseType Returns the option exercise style (e.g., "American").
 * @param listingExchange    Returns the primary listing exchange of the symbol.
 * @param description        Returns the symbol description (e.g., "Microsoft Corp.").
 * @param securityType       Returns the symbol security type (Eg: "Stock")
 * @param optionExpiryDate   Returns the date on which the option expires.
 * @param dividendDate       Returns the dividend declaration date.
 * @param optionStrikePrice  Returns the option strike price
 * @param tradable           Returns whether a symbol is tradable on the platform.
 * @param quotable           Returns whether a symbol has live market data.
 * @param hasOptions         Returns whether the symbol is an underlying option.
 * @param currency           Returns the symbol currency (ISO format).
 * @param minTicks           Returns the min ticks.
 * @param industrySector     Returns the industry sector classification.
 * @param industryGroup      Returns the industry group classification.
 * @param industrySubgroup   Returns the industry subgroup classification.
 */
public record QuestradeSymbolInfo(String symbol,
                                  int symbolId,
                                  double prevDayClosePrice,
                                  double highPrice52,
                                  double lowPrice52,
                                  long averageVol3Months,
                                  long averageVol20Days,
                                  long outstandingShares,
                                  double eps,
                                  double pe,
                                  double dividend,
                                  double symbolYield,
                                  String exDate,
                                  long marketCap,
                                  int tradeUnit,
                                  String optionType,
                                  String optionDurationType,
                                  String optionRoot,
                                  String optionExerciseType,
                                  String listingExchange,
                                  String description,
                                  String securityType,
                                  String optionExpiryDate,
                                  String dividendDate,
                                  String optionStrikePrice,
                                  boolean tradable,
                                  boolean quotable,
                                  boolean hasOptions,
                                  String currency,
                                  MinTick[] minTicks,
                                  String industrySector,
                                  String industryGroup,
                                  String industrySubgroup) {

    /**
     * Represents a min tick.
     *
     * @param pivot             Returns the beginning of the interval for a given minimum price increment.
     * @param minPriceIncrement Returns the minimum price increment.
     */
    public record MinTick(double pivot,
                          double minPriceIncrement) {
    }

}