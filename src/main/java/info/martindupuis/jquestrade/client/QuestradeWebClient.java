package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.*;

import java.util.Set;

public interface QuestradeWebClient {
    AuthenticationToken authenticate(String refreshToken);

    Set<QuestradeAccount> getAccounts(AuthenticationToken authToken);

    Set<QuestradeActivity> getAccountActivities(AuthenticationToken authToken, QuestradeAccount account, RequestPeriod period);

    Set<QuestradePosition> getPositions(AuthenticationToken authToken, QuestradeAccount account);

    Set<QuestradeCandle> getPositionCandles(AuthenticationToken authToken, QuestradePosition position, RequestPeriod period);
}
