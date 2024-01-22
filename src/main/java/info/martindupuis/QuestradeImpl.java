package info.martindupuis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import info.martindupuis.exceptions.ArgumentException;
import info.martindupuis.exceptions.RefreshTokenException;
import info.martindupuis.exceptions.StatusCodeException;
import info.martindupuis.exceptions.TimeRangeException;

import java.io.*;
import java.net.HttpURLConnection;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class QuestradeImpl implements Questrade {

    /**
     * Date formatter object for converting <code>ZonedDateTime</code> objects to strings in the
     * ISO 8601 time format.
     */
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    /**
     * A string representation of this object's last HTTP request.
     */
    private String lastRequest;
    /**
     * Authorization object that is created with information retrieved when consuming refresh token.
     */
    private AuthenticationToken authenticationToken;

    /**
     * Represents a {@code void} function that relays the {@code Authorization} object to a given function.
     * Set in {@code retrieveAccessToken()}.
     */
    private Consumer<AuthenticationToken> authRelayFunction = null;


    /**
     * <b>Temporary storage variable.</b> Authorization that is created in {@link #QuestradeImpl(String, String, String)}.
     * Meant to use cached data to save doing an API request.
     * This is {@code null} after {@link #activate(String)} is called when the {@link #QuestradeImpl(String, String, String)} constructor is used.
     */
    private AuthenticationToken startingAuthenticationToken;

    /**
     * Creates an instance of the {@code Questrade} wrapper, whose methods can be used to access the Questrade API. To use the object to access the Questrade API,
     * you must call the {@link #activate(String)} method on the object, otherwise API methods will not work. This constructor is intended for using cached data to create a
     * {@code Questrade} object, to save time and bandwidth since an access token can be used for 30 minutes after generation. Calling this constructor on its own
     * will <b>not</b> make an API request to Questrade.
     *
     * @param refreshToken The refresh token that is used to gain access to the Questrade API.
     * @param accessToken  The previously-generated access token. This will be used until it expires, after which the refreshToken will
     *                     be used to get a new authorization.
     * @param apiServer    The API server address associated with the accessToken.
     * @see <a href="https://www.questrade.com/api/documentation/getting-started">
     * Instructions for getting a refresh token for your Questrade account.</a>
     */
    public QuestradeImpl(String refreshToken, String accessToken, String apiServer) {
//        this.startingAuthorization = new Authorization(refreshToken);
    }

    @Override
    public String getLastRequest() {
        return lastRequest;
    }

    @Override
    public Questrade activate(String refreshToken) throws RefreshTokenException {
        if (refreshToken != null) {
            retrieveAccessToken(refreshToken);
        } else if (startingAuthorization != null) {
            authorization = startingAuthorization;
            startingAuthorization = null;
        }

        return this;
    }

    @Override
    public void revokeAuthorization() {
        String URL = "https://login.questrade.com/oauth2/token?grant_type=refresh_token&refresh_token=%s".formatted(authenticationToken.refresh_token());

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);

        try {
            sendRequest(request);
        } catch (RefreshTokenException ignored) {
        }
    }

    /**
     * Manually refresh the authorization (which includes the access token) with a given refresh token. Calling this function will save the resulting
     * {@link AuthenticationToken} object to be relayed to <i>authorization relay function</i>
     * (if set using the {@link #setAuthRelay(Consumer)} method).<br><br>
     * For reference, an access token usually expires in 1800 seconds (30 minutes). This value can be retrieved by using
     * {@link #getAuthorization()} then the {@link AuthenticationToken#expires_at()} method.
     *
     * @param refreshToken The refresh token to be used to refresh the authorization.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     */
    private void retrieveAccessToken(String refreshToken) throws RefreshTokenException {
        String URL = "https://login.questrade.com/oauth2/token?grant_type=refresh_token&refresh_token=%s".formatted(refreshToken);

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);

        String responseJSON = sendRequest(request);

        authenticationToken = new Gson().fromJson(responseJSON, AuthenticationToken.class);

        if (authRelayFunction != null) {
            authRelayFunction.accept(authorization);
        }
    }

    @Override
    public void retrieveAccessToken() throws RefreshTokenException {
        retrieveAccessToken(authenticationToken.refresh_token());
    }

    @Override
    public AuthenticationToken getAuthorization() {
        return authenticationToken;
    }

    @Override
    public Questrade setAuthRelay(Consumer<AuthenticationToken> authRelayFunction) {
        this.authRelayFunction = authRelayFunction;
        return this;
    }

    @Override
    public Balances getBalances(String accountNumber) throws RefreshTokenException {
        String URL = "v1/accounts/%s/balances".formatted(accountNumber);

        Request request = new Request(URL);
        request.setAccessToken(authenticationToken.access_token());
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());

        String balancesJSON = sendRequest(request);

        return new Gson().fromJson(balancesJSON, Balances.class);
    }

    @Override
    public Account[] getAccounts() throws RefreshTokenException {
        String URL = "v1/accounts/";

        Request request = new Request(URL);
        request.setAccessToken(authenticationToken.access_token());
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());

        String accountsJSON = sendRequest(request);

        Accounts accounts = new Gson().fromJson(accountsJSON, Accounts.class);

        return accounts.accounts;
    }

    @Override
    public ZonedDateTime getTime() throws RefreshTokenException {
        String URL = "v1/time";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());

        String timeJSON = sendRequest(request);

        String timeISO = new Gson().fromJson(timeJSON, JsonObject.class).get("time").getAsString();
        return ZonedDateTime.parse(timeISO);
    }

    @Override
    public Activity[] getActivities(String accountNumber, ZonedDateTime startTime, ZonedDateTime endTime) throws RefreshTokenException {
        if (startTime.isAfter(endTime)) {
            throw new TimeRangeException("The startTime must be earlier than the endTime.");
        }

        String URL = "v1/accounts/" + accountNumber + "/activities";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("startTime", startTime.format(ISO_FORMATTER));
        request.addParameter("endTime", endTime.format(ISO_FORMATTER));

        String activitiesJSON = sendRequest(request);

        Activities activities = new Gson().fromJson(activitiesJSON, Activities.class);

        return activities.activities;
    }

    @Override
    public Execution[] getExecutions(String accountNumber, ZonedDateTime startTime, ZonedDateTime endTime) throws RefreshTokenException {
        if (startTime.isAfter(endTime)) {
            throw new TimeRangeException("The startTime must be earlier than the endTime.");
        }

        String URL = "v1/accounts/" + accountNumber + "/executions";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setAccessToken(authenticationToken.access_token());
        request.setApiServer(authenticationToken.api_server());
        request.addParameter("startTime", startTime.format(ISO_FORMATTER));
        request.addParameter("endTime", endTime.format(ISO_FORMATTER));

        String executionsJSON = sendRequest(request);

        Executions executions = new Gson().fromJson(executionsJSON, Executions.class);

        return executions.executions;
    }

    @Override
    public Order[] getOrders(String accountNumber, int orderId, int... orderIds) throws RefreshTokenException {
        String URL = "v1/accounts/" + accountNumber + "/orders";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("ids", orderId, orderIds);

        return finishGetOrders(request);
    }

    @Override
    public Order[] getOrders(String accountNumber, ZonedDateTime startTime, ZonedDateTime endTime) throws RefreshTokenException {
        if (startTime.isAfter(endTime)) {
            throw new TimeRangeException("The startTime must be earlier than the endTime.");
        }

        String URL = "v1/accounts/" + accountNumber + "/orders";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("startTime", startTime.format(ISO_FORMATTER));
        request.addParameter("endTime", endTime.format(ISO_FORMATTER));
        request.setApiServer(authenticationToken.api_server());

        return finishGetOrders(request);
    }

    /**
     * A private method that all {@code getOrders} methods eventually funnel in to. Method exists only to not have to repeat code.
     *
     * @param request The API request created in some {@code getOrders} method. Contains the URL, parameters, request method, etc.
     * @return An {@code Order[]} array containing all the corresponding {@link Order} objects.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/account-calls/accounts-id-orders">
     * The Questrade API <b>GET accounts/:id/orders[/:orderId]</b> documentation</a>
     */

    private Order[] finishGetOrders(Request request) throws RefreshTokenException {
        String ordersJSON = sendRequest(request);
        Orders orders = new Gson().fromJson(ordersJSON, Orders.class);
        return orders.orders;
    }

    @Override
    public Position[] getPositions(String accountNumber) throws RefreshTokenException {
        String URL = "v1/accounts/" + accountNumber + "/positions";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());

        String positionsJSON = sendRequest(request);

        Positions positions = new Gson().fromJson(positionsJSON, Positions.class);

        return positions.positions;
    }

    @Override
    public Candle[] getCandles(int symbolId, ZonedDateTime startTime, ZonedDateTime endTime, Interval interval) throws RefreshTokenException {
        if (startTime.isAfter(endTime)) {
            throw new TimeRangeException("The startTime must be earlier than the endTime.");
        }

        String URL = "v1/markets/candles/" + symbolId;

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("startTime", startTime.format(ISO_FORMATTER));
        request.addParameter("endTime", endTime.format(ISO_FORMATTER));
        request.addParameter("interval", interval.name());

        String candlesJSON = sendRequest(request);

        Candles candles = new Gson().fromJson(candlesJSON, Candles.class);

        return candles.candles;
    }

    @Override
    public Market[] getMarkets() throws RefreshTokenException {
        String URL = "v1/markets";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());

        String marketsJSON = sendRequest(request);

        Markets markets = new Gson().fromJson(marketsJSON, Markets.class);

        return markets.markets;
    }

    @Override
    public Symbol[] searchSymbol(String prefix) throws RefreshTokenException {
        return searchSymbol(prefix, 0);
    }

    /**
     * Returns a search for a symbol containing basic information.<br><br>
     * * Example: If the {@code prefix} is {@code "BMO"}, the result set will contain basic information for
     * {@code "BMO"}, {@code "BMO.PRJ.TO"}, etc. (anything with {@code "BMO"} in it).
     *
     * @param prefix The prefix of a symbol or any word in the description. Example: "AAPL" is a valid parameter.
     * @param offset Offset in number of records from the beginning of a result set.
     *               Example: If there would normally be 5 results in the resulting array, an offset of {@code 1}
     *               would return 4 results (the 2nd to the 5th).
     * @return A {code Symbol[]} object containing basic information about the symbol(s).
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     * @see <a href="https://www.questrade.com/api/documentation/rest-operations/market-calls/symbols-search">
     * The Questrade API <b>GET symbols/search</b> documentation</a>
     */
    private Symbol[] searchSymbol(String prefix, int offset) throws RefreshTokenException {
        if (offset < 0) {
            throw new ArgumentException("offset argument cannot be less than 0");
        }

        String URL = "v1/symbols/search";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("prefix", prefix);
        if (offset > 0) {
            request.addParameter("offset", offset + "");
        }

        String symbolsJSON = sendRequest(request);

        Symbols symbols = new Gson().fromJson(symbolsJSON, Symbols.class);

        return symbols.symbols;
    }

    @Override
    public SymbolInfo[] getSymbol(int id, int... ids) throws RefreshTokenException {
        String URL = "v1/symbols";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("ids", id, ids);

        return finishGetSymbol(request);
    }

    @Override
    public SymbolInfo[] getSymbol(String name, String... names) throws RefreshTokenException {
        String URL = "v1/symbols";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("names", name, names);

        return finishGetSymbol(request);
    }

    /**
     * Helper method to cut down on code. All getSymbol() methods funnel into here.
     */

    private SymbolInfo[] finishGetSymbol(Request request) throws RefreshTokenException {
        String symbolInfosJSON = sendRequest(request);
        SymbolInfos symbolsInfos = new Gson().fromJson(symbolInfosJSON, SymbolInfos.class);
        return symbolsInfos.symbols;
    }

    @Override
    public Quote[] getQuote(int id, int... ids) throws RefreshTokenException {
        String URL = "v1/markets/quotes";

        Request request = new Request(URL);
        request.setRequestMethod(Request.RequestMethod.GET);
        request.setApiServer(authenticationToken.api_server());
        request.setAccessToken(authenticationToken.access_token());
        request.addParameter("ids", id, ids);

        String quotesJSON = sendRequest(request);

        Quotes quotes = new Gson().fromJson(quotesJSON, Quotes.class);
        return quotes.quotes;
    }

    /**
     * Sends the given request. If the access token expires during execution, it will automatically use the cached refresh token
     * to get a new access token and retry the request.
     *
     * @param request The API request that contain contains the URL, parameters, request method, etc.
     * @return Usually a string in JSON format.
     * @throws RefreshTokenException If the refresh token is invalid.
     * @throws ArgumentException     If the request arguments are invalid.
     * @throws StatusCodeException   If an error occurs when contacting the Questrade API.
     */
    private String sendRequest(Request request) throws RefreshTokenException {

        try {
            lastRequest = request.toString();

            HttpURLConnection connection = request.getConnection();

            int statusCode = connection.getResponseCode();

            // This exception is thrown when there's no internet (I'm guessing)
            //java.net.UnknownHostException

            // Response codes in the 200s are "successful"
            if (statusCode > 299 || statusCode < 200) {

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String responseJSON = in.readLine();
                connection.disconnect();

                //Extract error from response JSON
                Error error;
                try {
                    error = new Gson().fromJson(responseJSON, Error.class);
                } catch (JsonSyntaxException e) {
                    throw new RefreshTokenException("Error code " + statusCode + " was returned. Assuming refresh token is invalid.");
                }

                // Error code 1017 means access token is invalid or expired
                if (error.code == 1017) {

                    retrieveAccessToken(authenticationToken.refresh_token()); // get new access token
                    request.setAccessToken(authenticationToken.access_token()); // set new access token
                    request.setApiServer(authenticationToken.api_server()); // set new api server
                    return sendRequest(request); // resend fixed-up request
                } else if (error.code == 1002 || error.code == 1003 || error.code == 1004) {
                    throw new ArgumentException(error.message);
                }

                throw new StatusCodeException("A bad status code was returned: " + statusCode + ". Reason: " + error.message, statusCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseJSON = in.readLine();
            connection.disconnect();

            return responseJSON;

        } catch (IOException e) {
//            e.printStackTrace();
        }

        return null;
    }

    /**
     * Class used for GSON parsing, only in {@link QuestradeImpl#getAccounts()}
     */
    private static class Accounts {
        private Account[] accounts;
        private int userId;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#getActivities(String, ZonedDateTime, ZonedDateTime)}
     */
    private static class Activities {
        private Activity[] activities;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#getExecutions(String, ZonedDateTime, ZonedDateTime)}
     */
    private static class Executions {
        private Execution[] executions;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#finishGetOrders(Request)}
     */
    private static class Orders {
        private Order[] orders;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#getPositions(String)}
     */
    private static class Positions {
        private Position[] positions;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#getCandles(int, ZonedDateTime, ZonedDateTime, Interval)}
     */
    private static class Candles {
        private Candle[] candles;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#getMarkets()}
     */
    private static class Markets {
        private Market[] markets;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#searchSymbol(String, int)}
     */
    private static class Symbols {
        private Symbol[] symbols;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#finishGetOrders(Request)}
     */
    private static class SymbolInfos {
        private SymbolInfo[] symbols;
    }

    /**
     * Private class used for GSON parsing, only in {@link QuestradeImpl#getQuote(int, int...)}
     */
    private static class Quotes {
        private Quote[] quotes;
    }

    /**
     * Represents an error response returned by the Questrade API servers.
     */
    private static class Error {
        private int code;
        private String message;
    }
}
