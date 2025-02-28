package com.example.taskmanagementsystem.mapper;

import com.example.taskmanagementsystem.dto.response.AuthResponse;
import com.example.taskmanagementsystem.jwt.JwtService;
import com.example.taskmanagementsystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMapper {
    private final JwtService jwtService;

    public AuthResponse tokenBuilder(User user) {
        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
