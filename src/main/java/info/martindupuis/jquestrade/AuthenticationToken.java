package info.martindupuis.jquestrade;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Represents the data that allows an application to make requests the Questrade
 * API.
 *
 * @param access_token  The access token.
 *                      Only works with the associated API server, see
 *                      {@link #api_server}
 * @param api_server    The URL of the API server assigned to the access token.
 * @param expires_at    Returns the date & time at which the access token will
 *                      expire.
 *                      It appears to always be {@code 1800} seconds (30
 *                      minutes) from reception of said token.
 * @param refresh_token The new refresh token, which can be used to generate a
 *                      new {@code Authorization}.
 * @param token_type    The access token type. Is always <b>Bearer</b>
 */
@Slf4j
public record AuthenticationToken(String access_token,
        String api_server,
        ZonedDateTime expires_at,
        String refresh_token,
        String token_type) {

    /*
     * An authorization is valid if:
     * - an access_token has been given, and is not blank or an empty string
     * - the expiration date-time has not passed
     */
    public Boolean isValid() {
        boolean accessTokenIsValid = !(access_token == null || access_token.isBlank() || access_token.isEmpty());

        return !isExpired() && accessTokenIsValid;
    }

    public boolean isExpired() {
        LocalDateTime localNow = LocalDateTime.now();
        LocalDateTime localExpiration = expires_at.toLocalDateTime();

        log.debug(
                "QuestradeWebClient: class=AuthenticationToken action=isExpired() local now ({}) is after local expiration ({}) ?",
                localNow, 
                localExpiration);

        return localNow.isAfter(localExpiration);
    }

    public String getAuthHeader() {
        return String.format("Bearer %s",access_token);
    }
}
