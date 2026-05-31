package br.com.hermes.controller;

import br.com.hermes.dto.request.LoginRequest;
import br.com.hermes.dto.response.LoginResponse;
import br.com.hermes.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cliente/login")
    public ResponseEntity<LoginResponse> loginCliente(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginCliente(request));
    }

    @PostMapping("/transportador/login")
    public ResponseEntity<LoginResponse> loginTransportador(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginTransportador(request));
    }
}