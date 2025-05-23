package com.example.authservice.service;

import com.example.authservice.domain.User;
import com.example.authservice.domain.UserRole;
import com.example.authservice.dto.request.LoginRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.response.AuthResponse;
import com.example.authservice.exceptions.UserAlreadyExistsException;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.repository.UserRepository;
import com.example.common.event.UserRegisteredEvent;
import com.example.common.kafka.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public AuthResponse register(RegisterRequest registerRequest) {
        validateIfUserExists(registerRequest.getEmail());

        User savedUser = createAndSaveUser(registerRequest);
        publishUserRegisteredEvent(savedUser, registerRequest);

        String token = jwtService.generateToken(savedUser);
        return buildAuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticateUser(loginRequest);

        User user = findUserByEmail(loginRequest.getEmail());
        String token = jwtService.generateToken(user);

        return buildAuthResponse(token);
    }

    private void validateIfUserExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use");
        }
    }

    private User createAndSaveUser(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .roles(List.of(UserRole.USER))
                .build();
        return userRepository.save(user);
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

        kafkaTemplate.send(KafkaTopic.USER_REGISTERED.getTopicName(), event);
    }

    private void authenticateUser(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private AuthResponse buildAuthResponse(String token) {
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}

