package ch.bms.beatmanager.exception;

/**
 * Thrown when user input fails validation (e.g., out-of-range BPM or blank strings).
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }

    // Constructor overloading to preserve the root cause
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}