package com.jquestrade.client;

import com.jquestrade.AuthenticationToken;

public interface QuestradeWebClient {
    AuthenticationToken authenticate(String refreshToken);
}
