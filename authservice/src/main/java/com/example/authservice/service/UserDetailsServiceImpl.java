package com.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String id) {
        try {
            Long userId = Long.parseLong(id);
            return userService.findById(userId);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid user ID format: " + id);
        }
    }
}
