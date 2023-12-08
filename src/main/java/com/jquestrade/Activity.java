package com.jquestrade;

import lombok.Getter;

/**
 * Represents an account activity. Could be a cash transactions, dividends, trades, etc.
 *
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-activities">
 * The activity properties documentation</a>
 */
@Getter
public class Activity {
	
	/** The transaction date as a string in ISO 8601 format.  */
	public String transactionDate;
	/** The trade date as a string in ISO 8601 format.  */
	public String tradeDate;
	/** The settlement date as a string in ISO 8601 format.  */
	public String settlementDate;
	/** The action.<br>
	 * <u>Example types:</u><br>
	 * Buy (for orders)<br>
	 * Sell (for orders)<br>
	 * CON (for deposits)
	 */
	public String action;
	/**
	 * Returns the stock symbol involved in this activity (if applicable).<br>
	 * Returns an empty string ({@code ""}) if the activity isn't related to any particular security, like a deposit.
	 */
	public String symbol;
	/**
	 * Returns a numeric stock ID for the particular stock involved in this activity (if applicable).<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	public int symbolId;
	/** Returns the description of the activity. */
	public String description;
	/** Returns which currency was involved in the activity. */
	public String currency;
	/** Returns the quantity of shares involved in the activity (if applicable).<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	public double quantity;
	/** Returns the price of the shares involved in the activity (if the activity was an order).<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	public double price;
	/** Returns the gross amount.<br>
	 * Returns {@code 0} if the activity isn't related to any particular security, like a deposit.
	 */
	public double grossAmount;
	/** Returns the net change for the commission paid for the activity.<br>
	 * For example, orders will often charge ECN fees, which are generally a few cents.
	 */
	public double commission;
	/** Returns the net amount for the account.<br>
	 * For example, deposits and sell orders will have a positive net amount
	 * and withdrawals and buy orders will have negative net amounts.
	 */
	public double netAmount;

	private Activity() {
	}

	/** Returns the activity type. Possible values: Deposits, Dividends, Trades, etc. */
	public String type;

}