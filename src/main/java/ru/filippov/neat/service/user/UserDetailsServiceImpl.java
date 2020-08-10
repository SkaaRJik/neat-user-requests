package ru.filippov.neat.service.user;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.util.StringUtil;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.filippov.neat.entity.Role;
import ru.filippov.neat.entity.User;
import ru.filippov.neat.dto.SignUpDto;
import ru.filippov.neat.exception.AvatarUploadException;
import ru.filippov.neat.exception.UserNotFoundException;
import ru.filippov.neat.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${path.avatars:./public/avatars}")
    private String avatarPath;

    private Path avatarFilePath;

    @PostConstruct
    public void initAvatarDirectory(){
        this.avatarFilePath = Paths.get(avatarPath).toAbsolutePath().normalize();


        try {
            Files.createDirectories(this.avatarFilePath);
        } catch (IOException e) {
            log.error("UserDetailsServiceImpl.initAvatarDirectory", e);
        }

    }

    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = null;
        if(username.indexOf('@') != -1){
            user = userRepository.findByEmail(username)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User Not Found with email : " + username)
                    );
        } else {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User Not Found with username : " + username)
                    );
        }

        return UserPrincipal.toUserPrinciple(user);
    }

    public Boolean existsByEmail(String email) {
        Boolean isUserExists = userRepository.existsByEmail(email);
        log.debug(String.format("User %s exists: %b", email, isUserExists));
        return isUserExists;
    }

    public Boolean existsByUsername(String username) {
        Boolean isUserExists = userRepository.existsByUsername(username);
        log.debug(String.format("User %s exists: %b", username, isUserExists));
        return isUserExists;
    }

    public void registrate(SignUpDto signUpRequest) throws PSQLException {
        String avatarName = null;
        try {
            avatarName = saveAvatar(signUpRequest.getUsername(), signUpRequest.getAvatar());
        } catch (AvatarUploadException e) {
            log.error("UserDetailsServiceImpl.registrate", e);
        }


        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .active(true)
                .password(encoder.encode(signUpRequest.getPassword()))
                .roles( new HashSet<Role>(1) {{add(Role.ROLE_USER);}})
                .avatar( avatarName )
                .creationDate(LocalDateTime.now())
                .lastPasswordUpdate(LocalDateTime.now())
                .build();

        userRepository.save(user);

    }

    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)) );
    }




    public String saveAvatar(String username, String base64Avatar) throws AvatarUploadException {
        final String userAvatarFilename = username + ".png";
        try {


            final String[] split = base64Avatar.split(",");

            BufferedImage image = null;
            byte[] imageByte;
            Base64.Decoder decoder = Base64.getDecoder();
            imageByte = decoder.decode(split[1]);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
            ImageIO.write(image, "png", this.avatarFilePath.resolve(userAvatarFilename).toFile());
        } catch (Exception ex) {
            throw new AvatarUploadException("Could not store file " + userAvatarFilename + ". Please try again!", ex);
        }
        return userAvatarFilename;

    }

    public void saveAvatar(Long userId, MultipartFile avatar) throws UserNotFoundException, AvatarUploadException {
        final User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", userId)));


        try {
            final String[] split = avatar.getOriginalFilename().split(".");
            String fileExtension = "";
            if(split != null) {
                if(split.length >= 2) {
                    fileExtension = "." + split[split.length-1].trim();
                }
            }

            if(StringUtils.isEmpty(fileExtension)){
                fileExtension = ".jpeg";
            }

            final String userAvatarFilename = user.getUsername() + fileExtension;

            Files.copy(avatar.getInputStream(), this.avatarFilePath.resolve(userAvatarFilename), StandardCopyOption.REPLACE_EXISTING);

            user.setAvatar(userAvatarFilename);
            userRepository.save(user);

        } catch (IOException ex) {
            throw new AvatarUploadException("Could not store file " + user.getUsername() + ". Please try again!", ex);
        }

    }

    public UrlResource getAvatar(String username) throws UserNotFoundException, MalformedURLException {

        final User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(String.format("User with username %d not found", username)));

        final String userAvatarFilename = user.getAvatar();

        return new UrlResource(this.avatarFilePath.resolve(userAvatarFilename).toUri());
    }
}
