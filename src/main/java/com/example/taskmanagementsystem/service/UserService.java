package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User getByUsername(String username) {
        return Optional.ofNullable(username)
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public User currentUser() {
        var username = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getByUsername(username.getUsername());
    }
}
