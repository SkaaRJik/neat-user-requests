package ru.filippov.neatvue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import ru.filippov.neatvue.config.jwt.JwtProvider;
import ru.filippov.neatvue.config.jwt.TokenProvider;
import ru.filippov.neatvue.service.user.UserPrinciple;
import ru.filippov.utils.UtilBase64Image;


@Data
@AllArgsConstructor
public class ProfileDto {
    private final TokenDto token;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final ImageDto avatar;

    public static ProfileDto build(TokenProvider jwtProvider, Authentication authentication){
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        return new ProfileDto(new TokenDto(jwt), userPrincipal.getFirstname(), userPrincipal.getLastname(), userPrincipal.getEmail(), new ImageDto(UtilBase64Image.encoder(userPrincipal.getAvatar())));
    }
    //private final static String type = "Bearer";

}