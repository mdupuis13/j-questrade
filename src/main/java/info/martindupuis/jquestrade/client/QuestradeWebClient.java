package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.*;

import java.util.Set;

public interface QuestradeWebClient {
    AuthenticationToken authenticate(String refreshToken);

    Set<Account> getAccounts(AuthenticationToken authToken);

    Set<Position> getPositions(AuthenticationToken authToken, Account account);

    Set<Candle> getCandles(AuthenticationToken authToken, Position position, RequestPeriod period);

    Set<Activity> getAccountActivities(AuthenticationToken authToken, Account account, RequestPeriod period);
}
