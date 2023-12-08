package com.jquestrade.client;

import com.jquestrade.client.Authorization;

public interface QuestradeWebClient {
    Authorization authenticate(String refreshToken, String accessToken, String apiServerURL);
}
