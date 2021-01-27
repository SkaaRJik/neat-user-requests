package ru.filippov.neat.config.jwt;

public enum JwtStatus {
    EXPIRED_TOKEN, CHANGED_TOKEN;

    public final int value = 9000 + ordinal();
}
