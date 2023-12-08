package com.jquestrade;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents an account activity. Could be a cash transactions, dividends, trades, etc.
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-activities">
 * The activity properties documentation</a>
 */
@Getter
public class Activity {
	
	private Activity() {}

	/** The trade date as a string in ISO 8601 format.  */
	private String tradeDate;

	/** The transaction date as a string in ISO 8601 format.  */
	 private String transactionDate;

	/** The settlement date as a string in ISO 8601 format.  */
	private String settlementDate;

	/** The action.<br>
	 * <u>Example types:</u><br>
	 * Buy (for orders)<br>
	 * Sell (for orders)<br>
	 * CON (for deposits)
	 */
	private String action;

	/** Returns the stock symbol involved in this activity (if applicable).<br>
	 * Returns an empty string ({@code ""}) if the activity isn't related to any particular security, like a deposit.
	 */
	private String symbol;

	/** Returns a numeric stock ID for the particular stock involved in this activity (if applicable).<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	private int symbolId;

	/** Returns the description of the activity. */
	private String description;

	/** Returns which currency was involved in the activity. */
	private String currency;

	/** Returns the quantity of shares involved in the activity (if applicable).<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	private double quantity;

	/** Returns the price of the shares involved in the activity (if the activity was an order).<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	private double price;

	/** Returns the gross amount.<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	private double grossAmount;

	/** Returns the net change for the commission paid for the activity.<br>
	 * For example, orders will often charge ECN fees, which are generally a few cents.
	 */
	private double commission;

	/** Returns the net amount for the account.<br>
	 * For example, deposits and sell orders will have a positive net amount
	 * and withdrawals and buy orders will have negative net amounts.
	 */
	private double netAmount;

	/** Returns the activity type. Possible values: Deposits, Dividends, Trades, etc. */
	public String type;

}