package ru.filippov.neat.exception;

public class NeatConfigurationNotFoundException extends Exception {
    public NeatConfigurationNotFoundException() {
    }

    public NeatConfigurationNotFoundException(String message) {
        super(message);
    }

    public NeatConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeatConfigurationNotFoundException(Throwable cause) {
        super(cause);
    }
}
