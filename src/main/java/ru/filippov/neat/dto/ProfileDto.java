package ru.filippov.neat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import ru.filippov.neat.service.user.UserPrinciple;
import ru.filippov.utils.UtilBase64Image;


@Data
@AllArgsConstructor
public class ProfileDto {

    private final String firstname;
    private final String lastname;
    private final String email;
    private final TokenDto token;

    public static ProfileDto build(TokenDto tokenInfo, Authentication authentication){

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        return new ProfileDto(
                userPrincipal.getFirstname(),
                userPrincipal.getLastname(),
                userPrincipal.getUsername(),
                tokenInfo
        );
    }
    //private final static String type = "Bearer";

}