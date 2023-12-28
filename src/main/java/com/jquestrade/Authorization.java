package com.jquestrade;

import java.time.OffsetDateTime;

/**
 * Represents the data that allows an application to make requests the Questrade API.
 *
 * @param access_token  The access token.
 *                      Only works with the associated API server, see {@link #api_server}
 * @param api_server    The URL of the API server assigned to the access token.
 * @param expires_at    Returns the date & time at which the access token will expire.
 *                      It appears to always be {@code 1800} seconds (30 minutes) from reception of said token.
 * @param refresh_token The new refresh token, which can be used to generate a new {@code Authorization}.
 * @param token_type    The access token type. Is always <b>Bearer</b>
 */
public record Authorization(String access_token,
                            String api_server,
                            OffsetDateTime expires_at,
                            String refresh_token,
                            String token_type) {

    /*
     * An authorization is valid if:
     *      * an access_token has been given, and is not blank or an empty string
     *      *
     */
    public Boolean isValid() {
        return !(access_token == null || access_token.isBlank() || access_token.isEmpty());
    }
}
