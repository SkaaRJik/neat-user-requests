package ru.filippov.neat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.service.user.UserPrinciple;


@Data
@AllArgsConstructor
public class UserAuthDetailsDto {

    private final String firstname;
    private final String lastname;
    private final String username;
    private final TokenDto token;

    public static UserAuthDetailsDto build(TokenDto tokenInfo, UserPrinciple userPrincipal){


        return new UserAuthDetailsDto(
                userPrincipal.getFirstname(),
                userPrincipal.getLastname(),
                userPrincipal.getUsername(),
                tokenInfo
        );
    }

    //private final static String type = "Bearer";

}