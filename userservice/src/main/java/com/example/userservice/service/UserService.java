package com.example.userservice.service;

import com.example.common.event.user.UserChangedEmailEvent;
import com.example.common.event.user.UserDeletedEvent;
import com.example.userservice.domain.User;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.exceptions.forbidden.ForbiddenException;
import com.example.userservice.exceptions.notfound.UserNotFoundException;
import com.example.userservice.kafka.producer.UserEventPublisher;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);

        UserDeletedEvent event = UserDeletedEvent.builder()
                .userId(id)
                .build();
        userEventPublisher.publishUserDeleted(event);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequest userRequest) {
        User user = getUserById(id);

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        if (!user.getEmail().equals(userRequest.getEmail())) {
            user.setEmail(userRequest.getEmail());
            UserChangedEmailEvent event = UserChangedEmailEvent.builder()
                    .userId(id)
                    .email(userRequest.getEmail())
                    .build();
            userEventPublisher.publishUserEmailChanged(event);
        }

        return saveUser(user);
    }

    public void authorizeUser(Long jwtUserId, Long targetUserId) {
        if (jwtUserId == null || !jwtUserId.equals(targetUserId)) {
            throw new ForbiddenException("User not authorized to perform this action");
        }
    }
}
