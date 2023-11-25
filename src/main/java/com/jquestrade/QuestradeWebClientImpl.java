package com.jquestrade;

public class QuestradeWebClientImpl implements QuestradeWebClient {

    private String refreshToken;
    private String accessToken;
    private String apiServerURL;

    private final String QUESTRADE_URL ="https://login.questrade.com";
    private final String AUTH_URI = QUESTRADE_URL + "/oauth2/token?grant_type=refresh_token&refresh_token=%s";

    public QuestradeWebClientImpl() {
    }

    @Override
    public Authorization authenticate(String refreshToken, String accessToken, String apiServerURL) {
        this.accessToken = accessToken;
        this.apiServerURL = apiServerURL;
        this.refreshToken = refreshToken;

        return new Authorization(refreshToken,accessToken,apiServerURL);
    }
}
