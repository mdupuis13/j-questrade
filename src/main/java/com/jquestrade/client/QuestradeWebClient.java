package com.jquestrade.client;

import com.jquestrade.Account;
import com.jquestrade.AuthenticationToken;
import com.jquestrade.Position;

import java.util.List;

public interface QuestradeWebClient {
    AuthenticationToken authenticate(String refreshToken);

    List<Account> getAccounts(AuthenticationToken authToken);

    List<Position> getPositions(AuthenticationToken authToken, Account account);
}
