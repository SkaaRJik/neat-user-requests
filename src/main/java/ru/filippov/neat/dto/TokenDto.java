package ru.filippov.neat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenDto {
    private final String accessToken;
    private final String refreshToken;
    private final long accessTokenExpiredAfterMilliseconds;
    private final long refreshTokenExpiredAfterMilliseconds;
}
