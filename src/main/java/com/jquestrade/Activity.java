package com.jquestrade;

/**
 * Represents an account activity. Could be a cash transactions, dividends, trades, etc.
 *
 * @param tradeDate       The trade date as a string in ISO 8601 format.
 * @param transactionDate The transaction date as a string in ISO 8601 format.
 * @param settlementDate  The settlement date as a string in ISO 8601 format.
 * @param action          The action.<br>
 *                        <u>Example types:</u><br>
 *                        Buy (for orders)<br>
 *                        Sell (for orders)<br>
 *                        CON (for deposits)
 * @param symbol          Returns the stock symbol involved in this activity (if applicable).<br>
 *                        Returns an empty string ({@code ""}) if the activity isn't related to any particular security, like a deposit.
 * @param symbolId        Returns a numeric stock ID for the particular stock involved in this activity (if applicable).<br>
 *                        Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
 * @param description     Returns the description of the activity.
 * @param currency        Returns which currency was involved in the activity.
 * @param quantity        Returns the quantity of shares involved in the activity (if applicable).<br>
 *                        Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
 * @param price           Returns the price of the shares involved in the activity (if the activity was an order).<br>
 *                        Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
 * @param grossAmount     Returns the gross amount.<br>
 *                        Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
 * @param commission      Returns the net change for the commission paid for the activity.<br>
 *                        For example, orders will often charge ECN fees, which are generally a few cents.
 * @param netAmount       Returns the net amount for the account.<br>
 *                        For example, deposits and sell orders will have a positive net amount
 *                        and withdrawals and buy orders will have negative net amounts.
 * @param type            Returns the activity type. Possible values: Deposits, Dividends, Trades, etc.
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-activities">
 * The activity properties documentation</a>
 */

public record Activity(String tradeDate,
                       String transactionDate,
                       String settlementDate,
                       String action,
                       String symbol,
                       int symbolId,
                       String description,
                       String currency,
                       double quantity,
                       double price,
                       double grossAmount,
                       double commission,
                       double netAmount,
                       String type) {}