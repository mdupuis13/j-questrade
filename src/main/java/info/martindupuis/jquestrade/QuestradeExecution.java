package info.martindupuis.jquestrade;

/**
 * Represents executions for a specific account.
 *
 * @param symbol                   The symbol of the security involved in the execution.
 * @param symbolId                 The internal symbol identifier
 * @param quantity                 The number of shares in the execution.
 * @param side                     The client side of the order to which the execution belongs.
 *                                 Example values: Buy, Sell, Short, BTO, etc...
 * @param price                    The execution price.
 * @param id                       The internal identifier of the execution.
 * @param orderId                  The internal identifier of the order to which the execution belongs.
 * @param orderChainId             The internal identifier of the order chain to which the execution belongs.
 * @param exchangeExecId           The identifier of the execution at the market where it originated.
 * @param timestamp                The execution timestamp.
 * @param notes                    Manual notes for the execution that may have been entered by Trade Desk staff.
 *                                 This is usually blank.
 * @param venue                    The trading venue where execution originated.
 * @param totalCost                The execution cost (price x quantity).
 * @param orderPlacementCommission The Questrade commission for orders placed with Trade Desk.
 * @param commission               The Questrade commission for the execution.
 * @param executionFee             The liquidity fee charged by execution venue.
 * @param secFee                   The SEC fee charged on all sales of US securities.
 * @param canadianExecutionFee     Additional execution fee charged by TSX (if applicable).
 * @param parentId                 The internal identifier of the parent order.
 * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-executions">
 * The executions properties documentation</a>
 */
public record QuestradeExecution(String symbol,
                                 int symbolId,
                                 int quantity,
                                 String side,
                                 double price,
                                 double id,
                                 int orderId,
                                 int orderChainId,
                                 String exchangeExecId,
                                 String timestamp,
                                 String notes,
                                 String venue,
                                 double totalCost,
                                 double orderPlacementCommission,
                                 double commission,
                                 double executionFee,
                                 double secFee,
                                 int canadianExecutionFee,
                                 int parentId) {

}
