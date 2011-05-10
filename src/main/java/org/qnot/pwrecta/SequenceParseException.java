package org.qnot.pwrecta;

public class SequenceParseException extends Exception {

    private static final long serialVersionUID = -8532492297318885298L;

    public SequenceParseException() {
    }

    public SequenceParseException(String message) {
        super(message);
    }

    public SequenceParseException(Throwable cause) {
        super(cause);
    }

    public SequenceParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
