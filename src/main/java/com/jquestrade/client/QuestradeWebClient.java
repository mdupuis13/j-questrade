package com.jquestrade.client;

public interface QuestradeWebClient {
    Authorization authenticate(String refreshToken, String accessToken, String apiServerURL);
}
