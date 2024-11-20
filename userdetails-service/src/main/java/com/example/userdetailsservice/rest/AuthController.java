package com.example.userdetailsservice.rest;

import com.example.userdetailsservice.dto.AuthResponse;
import com.example.userdetailsservice.dto.SignUpRequest;
import com.example.userdetailsservice.model.User;
import com.example.userdetailsservice.security.TokenProvider;
import com.example.userdetailsservice.service.AuthService;
import com.example.userdetailsservice.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthService authService;

    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signupRequest){
        customUserDetailsService.saveUser(mapSignUpRequestToUser(signupRequest));

        String token = authService.authenticateAndGenerateToken(signupRequest.getUsername(), signupRequest.getPassword());
        return new AuthResponse(token);

    }

    private User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole("USER");
        return user;
    }


}
