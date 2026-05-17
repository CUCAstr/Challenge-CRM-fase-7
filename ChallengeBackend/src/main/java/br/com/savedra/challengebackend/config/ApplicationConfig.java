package br.com.savedra.challengebackend.config;

import br.com.savedra.challengebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            log.info("Procurando usuário no banco para autenticação: {}", username);
            
            // --- CORREÇÃO: ROBUSTEZ ---
            // Usamos findAll().stream() em vez de findByEmail(). 
            // Isso evita que o erro 'non unique result' derrube o processo de login
            // caso a limpeza do DataInitializer ainda não tenha refletido no banco.
            return repository.findAll().stream()
                .filter(u -> username.equalsIgnoreCase(u.getEmail()))
                .findFirst() // Pega apenas o primeiro caso existam duplicatas
                .map(user -> {
                    log.info("Usuário autorizado: {}, Role: {}", user.getEmail(), user.getRole());
                    return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                    );
                })
                .orElseThrow(() -> {
                    log.warn("Tentativa de login falhou: Usuário {} não encontrado.", username);
                    return new UsernameNotFoundException("User not found");
                });
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
