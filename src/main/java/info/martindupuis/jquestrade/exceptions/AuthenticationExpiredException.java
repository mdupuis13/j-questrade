package info.martindupuis.jquestrade.exceptions;

/**
 * Thrown when the refresh token is not valid. Refresh tokens expire 7 days from when they were generated.
 */
public class AuthenticationExpiredException extends RuntimeException {
    public AuthenticationExpiredException(String reason) {
        super(reason);
    }
}