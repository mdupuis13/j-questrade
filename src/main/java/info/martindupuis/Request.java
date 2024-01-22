package info.martindupuis;

import lombok.Setter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

/**
 * Represents an HTTPS request.
 */
class Request {

    @Setter
    private String accessToken;
    private int parameterCount = 0;
    @Setter
    private RequestMethod requestMethod = RequestMethod.GET;
    private final StringBuilder path;
    @Setter
    private String contentType;
    @Setter
    private String apiServer;

    Request(String path) {
        this.path = new StringBuilder(path);
    }


    void addParameter(String key, String value, String... values) {
        this.path.append(parameterCount == 0 ? "?" : "&")
                 .append(key)
                 .append("=")
                 .append(value);

        for (String otherValue : values) {
            path.append(",")
                .append(otherValue);
        }

        parameterCount++;
    }

    void addParameter(String key, int value, int... values) {
        String[] valuesAsString = Arrays.stream(values)
                                        .mapToObj(Integer::toString)
                                        .toArray(String[]::new);

        addParameter(key, "%d".formatted(value), valuesAsString);
    }

    HttpURLConnection getConnection() throws IOException {
        String URL = "";

        if (apiServer != null) {
            URL = apiServer + path;
        }

        URL += path.toString();

        URL url = URI.create(URL).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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
