package com.jquestrade.client;

public class QuestradeWebClientImpl implements QuestradeWebClient {

    private Authorization auth_info;
    private String accessToken;
    private String apiServerURL;

    private final String QUESTRADE_URL ="https://login.questrade.com";
    private final String AUTH_URI = QUESTRADE_URL + "/oauth2/token?grant_type=refresh_token&refresh_token=%s";

    public QuestradeWebClientImpl() {
    }

    @Override
    public Authorization authenticate(String refreshToken, String accessToken, String apiServerURL) {
        this.auth_info = new Authorization(accessToken,apiServerURL,refreshToken);

        return this.auth_info;
    }

    private <T> T makeCall(String url) {
        return null;
    }
}
