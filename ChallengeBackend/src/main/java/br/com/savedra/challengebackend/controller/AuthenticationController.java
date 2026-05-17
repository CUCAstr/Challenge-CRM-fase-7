package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.dto.AuthenticationRequest;
import br.com.savedra.challengebackend.dto.AuthenticationResponse;
import br.com.savedra.challengebackend.dto.RegisterRequest;
import br.com.savedra.challengebackend.model.User;
import br.com.savedra.challengebackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        System.out.println("Receiving registration request for: " + request.getEmail());
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        System.out.println("Receiving authentication request for: " + request.getEmail());
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMe() {
        System.out.println("Receiving /me request");
        return ResponseEntity.ok(service.getMe());
    }
}
