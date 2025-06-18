package com.example.authservice.service;

import com.example.authservice.domain.User;
import com.example.authservice.dto.request.LoginRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.response.AuthResponse;
import com.example.authservice.kafka.producer.UserEventPublisher;
import com.example.common.event.user.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserEventPublisher userEventPublisher;

    public AuthResponse register(RegisterRequest registerRequest) {
        userService.validateIfUserExists(registerRequest.getEmail());

        User user = userService.createUser(registerRequest);
        User savedUser = userService.saveUser(user);

        publishUserRegisteredEvent(savedUser, registerRequest);

        String token = jwtService.generateToken(savedUser);
        return buildAuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userService.findUserByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        authenticateUser(loginRequest, user.getId());
        String token = jwtService.generateToken(user);

        return buildAuthResponse(token);
    }

    private void publishUserRegisteredEvent(User user, RegisterRequest request) {
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .birthDate(request.getBirthDate())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        userEventPublisher.publishUserRegistered(event);
    }

    private void authenticateUser(LoginRequest request, Long userId) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private AuthResponse buildAuthResponse(String token) {
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}

