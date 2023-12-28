package com.jquestrade.client;

import com.jquestrade.Authorization;

public interface QuestradeWebClient {
    Authorization authenticate(String refreshToken);
}
