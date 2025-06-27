package com.example.userservice.controller;

import com.example.userservice.domain.User;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.dto.response.UserWithRatingResponse;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UserWithRatingResponse> getCurrentUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userMapper.toUserWithRating(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWithRatingResponse> findById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toUserWithRating(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWithRatingResponse> updateUser(@PathVariable Long id,
                                           @RequestBody @Valid UserRequest userRequest,
                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        userService.authorizeUser(userId, id);

        User user = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userMapper.toUserWithRating(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        userService.authorizeUser(userId, id);

        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
