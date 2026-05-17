package br.com.savedra.challengebackend.service;

import br.com.savedra.challengebackend.dto.AuthenticationRequest;
import br.com.savedra.challengebackend.dto.AuthenticationResponse;
import br.com.savedra.challengebackend.dto.RegisterRequest;
import br.com.savedra.challengebackend.model.User;
import br.com.savedra.challengebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j // Adiciona suporte a logs profissionais
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Iniciando processo de registro para o email: {}", request.getEmail());
        try {
            var user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setCompany(request.getCompany());

            user.setSegment(request.getSegment() != null ? request.getSegment() : "Geral");
            user.setGender(request.getGender() != null ? request.getGender() : "Não informado");
            user.setPhone(request.getPhone() != null ? request.getPhone() : "");
            user.setNotes("");

            user.setMemberSince(new java.util.Date());
            user.setStatus("Ativo");
            user.setScore(0);

            User savedUser = repository.save(user);
            log.info("Usuário registrado com sucesso. ID: {}, Role: {}", savedUser.getId(), savedUser.getRole());

            var userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );

            var jwtToken = jwtService.generateToken(userDetails);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (Exception e) {
            log.error("FALHA NO REGISTRO: {}", e.getMessage());
            throw e;
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Tentativa de login recebida para: {}", request.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            log.info("Autenticação bem-sucedida para: {}", request.getEmail());

            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        log.error("Usuário autenticado mas não encontrado no banco: {}", request.getEmail());
                        return new RuntimeException("Usuário não encontrado");
                    });

            var userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );

            var jwtToken = jwtService.generateToken(userDetails);
            log.info("Token JWT gerado para: {}", request.getEmail());
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (Exception e) {
            log.error("FALHA NA AUTENTICAÇÃO para {}: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }
    public User getMe() {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByEmail(email).orElseThrow();
    }
}
