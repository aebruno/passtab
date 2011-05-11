package org.qnot.pwrecta;

public class AlphabetParseException extends PasswordRectaException {
    private static final long serialVersionUID = 1146546954615040647L;

    public AlphabetParseException() {
    }

    public AlphabetParseException(String message) {
        super(message);
    }

    public AlphabetParseException(Throwable cause) {
        super(cause);
    }

    public AlphabetParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
