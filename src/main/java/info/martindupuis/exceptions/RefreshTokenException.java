package info.martindupuis.exceptions;

/**
Thrown when the refresh token is not valid. Refresh tokens expire 7 days from when they were generated.
*/
public class RefreshTokenException extends Exception {
    public RefreshTokenException(String reason) {
        super(reason);
    }
}