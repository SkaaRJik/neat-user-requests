package ru.filippov.neat.service.user;

import lombok.extern.log4j.Log4j2;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.filippov.neat.domain.Role;
import ru.filippov.neat.domain.User;
import ru.filippov.neat.dto.SignUpDto;
import ru.filippov.neat.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Value("${app.default.avatar}")
    private String DEFAULT_AVATAR;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
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

        return UserPrinciple.toUserPrinciple(user);
    }

    public Boolean existsByEmail(String email) {
        Boolean isUserExists = userRepository.existsByEmail(email);
        log.debug(String.format("User %s exists: %b", email, isUserExists));
        return isUserExists;
    }

    public Boolean existsByUsername(String username) {
        Boolean isUserExists = userRepository.existsByEmail(username);
        log.debug(String.format("User %s exists: %b", username, isUserExists));
        return isUserExists;
    }

    public void registrate(SignUpDto signUpRequest) throws PSQLException {

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .active(true)
                .password(encoder.encode(signUpRequest.getPassword()))
                .roles( new HashSet<Role>(1) {{add(Role.USER);}})
                .avatar( this.DEFAULT_AVATAR)
                .creationDate(LocalDateTime.now())
                .lastPasswordUpdate(LocalDateTime.now())
                .build();

        userRepository.save(user);

    }
}