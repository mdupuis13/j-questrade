package com.jquestrade.client;

public interface QuestradeWebClient {
    void authenticate(String refreshToken);
    Boolean isAuthenticated();
}
