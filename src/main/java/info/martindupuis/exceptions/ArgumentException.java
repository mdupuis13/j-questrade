package info.martindupuis.exceptions;

/** Thrown if invalid or malformed argument, argument length exceeds imposed limit,
  *	or missing required argument.
  */
public class ArgumentException extends RuntimeException {
    public ArgumentException(String reason) {
        super(reason);
    }
}