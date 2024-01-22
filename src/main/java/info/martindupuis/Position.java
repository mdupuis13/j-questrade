package info.martindupuis;

/**
 * Represents a position in your Questrade account.
 *
 * @param symbol             Returns the symbol of the position.
 * @param symbolId           Returns the internal symbol identifier.
 * @param openQuantity       Returns the position quantity remaining open.
 * @param closedQuantity     Returns the portion of the position that was closed today.
 * @param currentMarketValue Returns the current market value of the position (quantity x price).
 * @param currentPrice       Returns the current price of the position symbol.
 * @param averageEntryPrice  Returns the average price paid for all executions constituting the position.
 * @param closedPnl          Return The realized profit/loss on this position.
 * @param openPnl            Return the total unrealized profit/loss on this position.
 * @param totalCost          Returns the total cost of the position.
 * @param isRealTime         Returns whether real-time quote was used to compute the profit-and-losses.
 * @param isUnderReorg       Whether the symbol is currently undergoing a reorg.
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-positions">
 * The positions documentation</a>
 */
public record Position(
        String symbol,
        int symbolId,
        int openQuantity,
        int closedQuantity,
        double currentMarketValue,
        double currentPrice,
        double averageEntryPrice,
        double closedPnl,
        double openPnl,
        double totalCost,
        boolean isRealTime,
        boolean isUnderReorg) {}
