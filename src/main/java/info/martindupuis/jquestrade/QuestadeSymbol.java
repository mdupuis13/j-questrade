package info.martindupuis.jquestrade;

/**
 * Represents a symbol/ticker.
 *
 * @param theSymbol       Returns the stock symbol/ticker.
 * @param symbolId        Returns the internal unique symbol identifier.
 * @param description     Returns the symbol description (e.g., "Microsoft Corp.").
 * @param securityType    Returns the symbol security type (Eg: "Stock")
 * @param listingExchange Returns the primary listing exchange of the symbol.
 * @param tradable        Returns whether a theSymbol is tradable on the platform.
 * @param quotable        Returns whether a theSymbol has live market data.
 * @param currency        Returns the symbol currency (ISO format).
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/symbols-search">
 * The symbol search documentation</a>
 */
public record QuestadeSymbol(String theSymbol,
                             int symbolId,
                             String description,
                             String securityType,
                             String listingExchange,
                             boolean tradable,
                             boolean quotable,
                             String currency) {
}
