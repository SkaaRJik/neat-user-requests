package ru.filippov.neat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.filippov.neat.entity.Role;
import ru.filippov.neat.service.user.UserPrincipal;


@Data
@AllArgsConstructor
public class UserAuthDetailsResponse {
    private final Long id;
    private final String firstname;
    private final String lastname;
    private final String[] roles;
    private final String username;
    private final TokenDto tokens;

    public static UserAuthDetailsResponse build(TokenDto tokens, UserPrincipal userPrincipal){

        String[] roles = userPrincipal.getAuthorities()
                .stream()
                .map(authority -> Role.valueOf(authority.getAuthority()).name())
                .toArray(String[]::new);

        return new UserAuthDetailsResponse(
                userPrincipal.getId(),
                userPrincipal.getFirstname(),
                userPrincipal.getLastname(),
                roles,
                userPrincipal.getUsername(),
                tokens
        );
    }

    //private final static String type = "Bearer";

}
