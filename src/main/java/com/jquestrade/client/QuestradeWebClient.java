package com.jquestrade.client;

import com.jquestrade.Account;
import com.jquestrade.AuthenticationToken;

import java.util.List;

public interface QuestradeWebClient {
    AuthenticationToken authenticate(String refreshToken);

    List<Account> getAccounts(AuthenticationToken authToken);
}
