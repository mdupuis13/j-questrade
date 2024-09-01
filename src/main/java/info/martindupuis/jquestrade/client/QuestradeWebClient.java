package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.*;

import java.util.Set;

public interface QuestradeWebClient {
    AuthenticationToken authenticate(String refreshToken);

    Set<Account> getAccounts(AuthenticationToken authToken);

    Set<Activity> getAccountActivities(AuthenticationToken authToken, Account account, RequestPeriod period);

    Set<Position> getPositions(AuthenticationToken authToken, Account account);

    Set<Candle> getPositionCandles(AuthenticationToken authToken, Position position, RequestPeriod period);
}
