package com.jquestrade;

import lombok.Setter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Represents an HTTPS request.
 */
class Request {

    @Setter
    private String accessToken;
    private int parameterCount = 0;
    @Setter
    private RequestMethod requestMethod = RequestMethod.GET;
    private String path;
    @Setter
    private String contentType;
    @Setter
    private String apiServer;

    Request(String path) {
        this.path = path;
    }


    void addParameter(String key, String value, String... values) {
        path += ((parameterCount == 0) ? "?" : "&") + key + "=" + value;

        for (String s : values) {
            path += "," + s;
        }

        parameterCount++;
    }

    void addParameter(String key, int value, int... values) {
        path += ((parameterCount == 0) ? "?" : "&") + key + "=" + value;

        for (int j : values) {
            path += "," + j;
        }

        parameterCount++;
    }

    HttpURLConnection getConnection() throws IOException {
        String URL;

        if (apiServer != null) {
            URL = apiServer + path;
        } else {
            URL = path;
        }

        HttpURLConnection connection =
                (HttpURLConnection) new URL(URL).openConnection();

        if (accessToken != null) {
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        }

        if (contentType != null) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", contentType);
        }

        connection.setRequestMethod(requestMethod.name());

        return connection;
    }

    @Override
    public String toString() {
        return requestMethod + " " + (apiServer != null ? apiServer : "") + path;
    }

    /**
     * Request methods for the HTTP request
     */
    enum RequestMethod {
        GET,
        POST
    }


}
