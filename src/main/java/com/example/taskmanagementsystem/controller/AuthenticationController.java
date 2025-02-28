package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.request.AuthRequest;
import com.example.taskmanagementsystem.dto.request.RegisterRequest;
import com.example.taskmanagementsystem.dto.response.AuthResponse;
import com.example.taskmanagementsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/registration")
    @Operation(summary = "Регистрация пользователя",
            description = "Создает нового пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public AuthResponse register(@RequestBody @Valid RegisterRequest registerRequest){
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Аутентификация пользователя",
            description = "Аутентифицирует пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно аутентифицирован"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest authRequest){
        return authService.authUser(authRequest);
    }
}
