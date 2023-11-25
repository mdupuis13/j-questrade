package com.jquestrade;

public interface QuestradeWebClient {
    Authorization authenticate(String refreshToken, String accessToken, String apiServerURL);
}
