package info.martindupuis.jquestrade;

/** Represents a symbol/ticker. For more information on the ticker, use
 * {@link info.martindupuis.jquestrade.client.QuestradeWebClient#getSymbol(int, int...)} to get a {@link SymbolInfo},
 * which is this class + more info.
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/symbols-search">
 * The symbol search documentation</a>
 */
public class Symbol {
    private String theSymbol;
    private int symbolId;
    private String description;
    private String securityType;
    private String listingExchange;
    private boolean isTradable;
    private boolean isQuotable;
    private String currency;

    public Symbol() {
    }

    /**
     * Returns the stock symbol/ticker.
     *
     * @return The stock symbol/ticker.
     */
    public String getSymbol() {
        return theSymbol;
    }

    /**
     * Returns the internal unique symbol identifier.
     *
     * @return The internal unique symbol identifier.
     */
    public int getSymbolId() {
        return symbolId;
    }

    /**
     * Returns the symbol description (e.g., "Microsoft Corp.").
     *
     * @return The symbol description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the symbol security type (Eg: "Stock")
     *
     * @return The symbol security type
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/enumerations/enumerations#security-type">
     * The Security Type section</a>
     * for all possible values.
     */
    public String getSecurityType() {
        return securityType;
    }

    /**
     * Returns the primary listing exchange of the symbol.
     *
     * @return The primary listing exchange.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/enumerations/enumerations#listing-exchange">
     * The Listing Exchange section</a>
     * for all possible values.
     */
    public String getListingExchange() {
        return listingExchange;
    }

    /**
     * Returns whether a theSymbol is tradable on the platform.
     *
     * @return Whether a theSymbol is tradable on the platform.
     */
    public boolean isTradable() {
        return isTradable;
    }

    /**
     * Returns whether a theSymbol has live market data.
     *
     * @return Whether a theSymbol has live market data.
     */
    public boolean isQuotable() {
        return isQuotable;
    }

    /**
     * Returns the symbol currency (ISO format).
     *
     * @return The symbol currency.
     */
    public String getCurrency() {
        return currency;
    }

}
