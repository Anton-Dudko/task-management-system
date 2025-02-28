package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.request.AuthRequest;
import com.example.taskmanagementsystem.dto.request.RegisterRequest;
import com.example.taskmanagementsystem.dto.response.AuthResponse;
import com.example.taskmanagementsystem.exceptions.UserAlreadyExistsException;
import com.example.taskmanagementsystem.mapper.AuthMapper;
import com.example.taskmanagementsystem.mapper.UserMapper;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registerUser(RegisterRequest registerRequest) {

        return Optional.ofNullable(registerRequest)
                .map(userMapper::buildUser)
                .flatMap(user -> {
                    try {
                        return Optional.of(userService.create(user));
                    } catch (Exception e) {
                        throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
                    }
                })
                .map(authMapper::tokenBuilder)
                .orElseThrow();
    }

    public AuthResponse authUser(AuthRequest authRequest) {
        return Optional.ofNullable(authRequest)
                .map(a -> authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                a.getEmail(),
                                a.getPassword())))
                .map(a -> (User) a.getPrincipal())
                .map(authMapper::tokenBuilder)
                .orElseThrow(() -> new RuntimeException("Error occurred while authentication user"));
    }
}
