package com.example.userservice.controller;

import com.example.userservice.domain.User;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        Long id = jwt.getClaim("id");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody @Valid UserRequest userRequest,
                                           @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        userService.authorizeUser(userId, id);

        User user = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        userService.authorizeUser(userId, id);

        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
