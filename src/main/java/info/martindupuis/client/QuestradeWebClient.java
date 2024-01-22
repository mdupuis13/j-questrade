package info.martindupuis.client;

import info.martindupuis.*;

import java.util.List;

public interface QuestradeWebClient {
    AuthenticationToken authenticate(String refreshToken);

    List<Account> getAccounts(AuthenticationToken authToken);

    List<Position> getPositions(AuthenticationToken authToken, Account account);

    List<Candle> getCandles(AuthenticationToken authToken, Position position, RequestPeriod period);

    List<Activity> getAccountActivities(AuthenticationToken authToken, Account account, RequestPeriod period);
}
