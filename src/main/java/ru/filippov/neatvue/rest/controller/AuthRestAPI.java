package ru.filippov.neatvue.rest.controller;

import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import ru.filippov.neatvue.config.jwt.JwtProvider;
import ru.filippov.neatvue.domain.Role;
import ru.filippov.neatvue.domain.User;
import ru.filippov.neatvue.dto.ProfileDto;
import ru.filippov.neatvue.dto.LoginDto;
import ru.filippov.neatvue.dto.SignUpDto;
import ru.filippov.neatvue.dto.TokenDto;
import ru.filippov.neatvue.repository.UserRepository;
import ru.filippov.utils.UtilBase64Image;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPI {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Value("${app.default.avatar}")
    private String defaultAvatar;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ProfileDto profile = ProfileDto.build(jwtProvider ,authentication);


        return ResponseEntity.ok(profile);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> isEmailExist(@RequestParam String email){

            return new ResponseEntity<Boolean>(userRepository.existsByEmail(email),
                    HttpStatus.OK);

    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpDto signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already used!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        /*User user = new User(signUpRequest.getEmail(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();*/

        /*strRoles.forEach(role -> {
        	switch(role) {
	    		case "admin":
	    			Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	    			roles.add(adminRole);
	    			
	    			break;
	    		case "pm":
	            	Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	            	roles.add(pmRole);
	            	
	    			break;
	    		default:
	        		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	        		roles.add(userRole);        			
        	}
        });*/

        User user = new User(
                signUpRequest.getEmail(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                encoder.encode(signUpRequest.getPassword()),
                        true,
                new HashSet<Role>(1) {{add(Role.USER);}},
                this.defaultAvatar
        );

        userRepository.save(user);

        return ResponseEntity.ok().body("User registered successfully!");
    }
}