package com.jquestrade;

import com.jquestrade.exceptions.ArgumentException;
import com.jquestrade.exceptions.RefreshTokenException;
import com.jquestrade.exceptions.StatusCodeException;

import java.time.ZonedDateTime;
import java.util.function.Consumer;

/**
 * The {@code Questrade} class represents a Questrade API client. It contains methods for easily accessing the Questrade API,
 * such as retrieving the balances, positions, orders, market data, etc. It will also automatically retrieve a new access token
 * if the current access token expires during runtime.
 *
 * @author Matei Marica
 * @see <a href="https://www.questrade.com/api">Questrade API documentation</a>
 */
public interface Questrade {
    /**
     * A string representation of this object's last HTTP request.
     *
     * @return A string representation of this object's last HTTP request.
     */
    String getLastRequest();

    /**
     * Starts up the API connection by exchanging the refresh token for an access token. You must call this method before
     * calling any methods that perform API requests.
     * This will create an {@link Authorization} for this object. Calling this method more than one time has no effect.
     *
     * @return A reference to the calling object, for optional method chaining.<br>
     * Example: {@code Questrade q = new Questrade(token).activate();}
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     */
    Questrade activate(String refreshToken) throws RefreshTokenException;

    /**
     * Consumes this object's refresh and access token, invalidating them
     * so that the user will have to go and generate a new refresh token from Questrade.
     * This method simply exchanges the refresh token for new tokens, but throws away the response,
     * essentially invalidating the old ones.
     */
    void revokeAuthorization();

    /**
     * Forcefully refreshes the authorization (which includes the access token) with the refresh token saved within the object.
     * Calling this function will save the resulting {@link Authorization} object to be relayed to <i>authorization relay function</i>
     * (if set using the {@link #setAuthRelay(Consumer)} method).<br><br>
     * For reference, an access token usually expires in 1800 seconds (30 minutes). This value can be retrieved by using
     * {@link #getAuthorization()} then the {@link Authorization#expires_in()} method.
     *
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     */
    void retrieveAccessToken() throws RefreshTokenException;

    /**
     * Returns an {@link Authorization} object which contains the access token, api server,
     * access token expiry time, new refresh token, and the access token type (which is always Bearer).<br><br>
     *
     * @return The current {@code Authorization} object. Will be {@code null} if {@link #activate(String)} has not been called yet.
     */
    Authorization getAuthorization();

    /**
     * Sets the authorization relay function, which is user-created method to which an {@link Authorization} object is relayed to.
     * This is useful for when you want to save the new refresh token instead of having to manually generate a new one on the Questrade website
     * every time you use your application. For example, you could relay the {@code Authorization} object to a method that saved the new refresh
     * token in a text file, so that the refresh token could be used when creating another {@code Questrade} object.<br><br>
     * <p>
     * Example usage:<br>
     * {@code Questrade q = new Questrade(token);}<br>
     * {@code q.setAuthRelay(auth -> saveToFile(auth));}<br>
     *
     * @param authRelayFunction Should be a {@code void} function to which the {@code Authorization} will be forwarded to.
     *                          This can be written as a lambda function.
     * @return A reference to the calling object, for optional method chaining.<br>
     * Example: {@code Questrade q = new Questrade(token).setAuthRelay(function);}
     */
    Questrade setAuthRelay(Consumer<Authorization> authRelayFunction);

    /**
     * Get the balances for the given account.
     *
     * @param accountNumber The account number to get the balances for. To get an account number, call
     *                      {@link #getAccounts()} to get a {@code Account[]}, then call {@link Account#number()} on some index.
     * @return A {@code Balances} object, from which the following {@code Balance} objects can be gotten:
     * perCurrencyBalances, combinedBalances, sodPerCurrencyBalances, sodCombinedBalances.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-balances">
     * The Questrade API <b>GET accounts/:id/balances</b> documentation</a>
     */
    Balances getBalances(String accountNumber) throws RefreshTokenException;

    /**
     * Get all the accounts for the associated Questrade account.
     *
     * @return An {@code Account[]} array, containing all the accounts.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API
     * @see <a href="https://questrade.com/api/documentation/rest-operations/account-calls/accounts">
     * The Questrade API <b>GET accounts</b> documentation</a>
     */
    Account[] getAccounts() throws RefreshTokenException;

    /**
     * Returns the current server time in ISO format and Eastern time zone (EST).
     *
     * @return A {@code ZonedDateTime} object representing the current server time.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/time">
     * The Questrade API <b>GET time</b> documentation</a>
     */
    ZonedDateTime getTime() throws RefreshTokenException;

    /**
     * Get all the activities of an account in a given time period. A maximum of 30 days of data can be requested at a time.
     *
     * @param accountNumber The account for which to get the activities for.
     * @param startTime     The beginning of the time period to get the activities for.
     * @param endTime       The end of the time period to get the activities for. This cannot be more than 31 days after the startTime argument.
     * @return An {@code Activity[]} array representing all the activities in the given time period.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws ArgumentException     If the request arguments are invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-activities">
     * The Questrade API <b>GET accounts/:id/activities</b> documentation</a>
     */
    Activity[] getActivities(String accountNumber, ZonedDateTime startTime, ZonedDateTime endTime) throws RefreshTokenException;

    /**
     * Get all the executions of an account in a given time period. A maximum of 30 days of data can be requested at a time.
     *
     * @param accountNumber The account for which to get the executions for.
     * @param startTime     The beginning of the time period to get the executions for.
     * @param endTime       The end of the time period to get the executions for.
     * @return An {@code Activity[]} array representing all the executions in the given time period.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws ArgumentException     If the request arguments are invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-executions">
     * The Questrade API <b>GET accounts/:id/executions</b> documentation</a>
     */
    Execution[] getExecutions(String accountNumber, ZonedDateTime startTime, ZonedDateTime endTime) throws RefreshTokenException;

    /**
     * Get all the orders of an account in a given time period, using one or more order IDs.
     * <p>
     * Example usages:<br>
     * {@code getOrders(accountNumber, 11111111)}<br>
     * {@code getOrders(accountNumber, 11111111, 22222222)}<br>
     * {@code getOrders(accountNumber, orderNum1, orderNum2, orderNum3)}<br>
     *
     * @param accountNumber The account to get the order information for.
     * @param orderId       The order ID to get the order info for (an {@link Order} object contain all of an order's information).
     * @param orderIds      Optional parameter for if you want to add more order IDs to the request.
     * @return An {@code Order[]} array containing all of the {@link Order} objects corresponding to the given order IDs.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws ArgumentException     If the request arguments are invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-orders">
     * The Questrade API <b>GET accounts/:id/orders[/:orderId]</b> documentation</a>
     */
    Order[] getOrders(String accountNumber, int orderId, int... orderIds) throws RefreshTokenException;
    /**
     * Get all the orders of an account in a given time period. A maximum of 30 days of data can be requested at a time.
     * This will get all orders in the time period, whether still open or closed.
     *
     * @param accountNumber The account for which to get the orders for.
     * @param startTime     The beginning of the time period to get the orders for.
     * @param endTime       The end of the time period to get the orders for.
     * @return An {@code Order[]} array containing all of the {@link Order}s created in the time period.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-orders">
     * The Questrade API <b>GET accounts/:id/orders[/:orderId]</b> documentation</a>
     */
    Order[] getOrders(String accountNumber, ZonedDateTime startTime, ZonedDateTime endTime) throws RefreshTokenException;

    /**
     * Get all the current positions for a given account.
     *
     * @param accountNumber The account to get the positions for.
     * @return A {@code Position[]} array containing all the corresponding {@link Position} objects.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws ArgumentException     If the request arguments are invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-orders">
     * The Questrade API <b>GET accounts/:id/orders[/:orderId]</b> documentation</a>
     */
    Position[] getPositions(String accountNumber) throws RefreshTokenException;

    /**
     * Returns historical market data in the form of OHLC candlesticks for a specified symbol.
     * This call is limited to returning 2,000 candlesticks in a single response.
     *
     * @param symbolId  The internal symbol identifier.
     * @param startTime The beginning of the time period to get the candles for.
     * @param endTime   The end of the time period to get the candles for.
     * @param interval  The time between the candles.
     * @return An {@code Candle[]} array containing all of the {@link Candle}s within in the given time period.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws ArgumentException     If the request arguments are invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/markets-candles-id">
     * The Questrade API <b>GET markets/candles/:id</b> documentation</a>
     */
    Candle[] getCandles(int symbolId, ZonedDateTime startTime, ZonedDateTime endTime, Candle.Interval interval) throws RefreshTokenException;

    /**
     * Retrieves information about supported markets.
     *
     * @return An {@code Market[]} array containing all the available {@link Market}s.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/markets-candles-id">
     * The Questrade API <b>GET markets</b> documentation</a>
     */
    Market[] getMarkets() throws RefreshTokenException;

    /**
     * Returns a search for a symbol containing basic information. This method is the same as calling
     * {@code searchSymbol(String, 0)}<br><br>
     * Example: If the {@code prefix} is {@code "BMO"}, the result set will contain basic information for
     * {@code "BMO"}, {@code "BMO.PRJ.TO"}, etc. (anything with {@code "BMO"} in it).
     *
     * @param prefix The prefix of a symbol or any word in the description. Example: "AAPL" is a valid parameter.
     * @return A {code Symbol[]} object containing basic information about the symbol(s).
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/symbols-search">
     * The Questrade API <b>GET symbols/search</b> documentation</a>
     */
    Symbol[] searchSymbol(String prefix) throws RefreshTokenException;

    /**
     * Returns detailed information for one or more specific symbols using their internal unique identifiers.
     *
     * @param id  The internal unique identifier for a symbol.
     * @param ids Optional parameter for if you want to get information for multiple symbols in the same request.
     * @return A {@code SymbolInfo[]} object containing information about the symbol(s).
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/symbols-id">
     * The Questrade API <b>GET symbols/:id</b> documentation</a>
     */
    SymbolInfo[] getSymbol(int id, int... ids) throws RefreshTokenException;

    /**
     * Returns detailed information for one or more specific symbols using their symbol names.
     *
     * @param name  The name of the symbol. (Eg: "MSFT")
     * @param names Optional parameter for if you want to get information for multiple symbols in the same request.
     * @return A {@code SymbolInfo[]} object containing information about the symbol(s).
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/symbols-id">
     * The Questrade API <b>GET symbols/:id</b> documentation</a>
     */
    SymbolInfo[] getSymbol(String name, String... names) throws RefreshTokenException;

    /**
     * Retrieves a single Level 1 market data quote for one or more symbols. <br><br>
     * <b>IMPORTANT NOTE:</b> The Questrade user needs to be subscribed to a real-time data package,
     * to receive market quotes in real-time, otherwise call to get quote is considered snap quote and
     * limit per market can be quickly reached. Without real-time data package, once limit is reached, the response
     * will return delayed data. <b>(Please check "delay" parameter in response always)</b>
     *
     * @param id  The internal identifer of a symbol.
     * @param ids Optional parameter for adding more symbols to the same request.
     * @return A {@code Quote[]} array, each index containing the quote for each requested symbol.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/markets-quotes-id">
     * The Questrade API <b>GET markets/quotes/:id</b> documentation</a>
     */
    Quote[] getQuote(int id, int... ids) throws RefreshTokenException;
}
