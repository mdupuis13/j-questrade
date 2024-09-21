package info.martindupuis.jquestrade;

/**
 * Represents a per-currency or combined balances for a specified account.
 *
 * @param currency          Returns this balance's currency.
 * @param cash              Returns how much cash (non-invested money) is in this balance.
 * @param marketValue       Returns the market value of all securities in the account in this balance's currency.
 * @param totalEquity       Returns the total equity for this balance (cash + market value).
 * @param buyingPower       Returns the buying power for that particular currency side of the account.
 * @param maintenanceExcess Returns the maintenance excess for that particular currency side of the account.
 * @param realTime          Whether real-time data was used to calculate this balance's values.
 */
public record QuestradeBalance(String currency,
                               double cash,
                               double marketValue,
                               double totalEquity,
                               double buyingPower,
                               double maintenanceExcess,
                               boolean realTime) {

}
