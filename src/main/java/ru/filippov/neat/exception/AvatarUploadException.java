package ru.filippov.neat.exception;

public class AvatarUploadException extends Exception {
    public AvatarUploadException() {
        super();
    }

    public AvatarUploadException(String message) {
        super(message);
    }

    public AvatarUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public AvatarUploadException(Throwable cause) {
        super(cause);
    }

    protected AvatarUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
