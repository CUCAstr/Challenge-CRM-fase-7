package br.com.savedra.challengebackend.config;

import br.com.savedra.challengebackend.model.User;
import br.com.savedra.challengebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Inicializador de Dados Profissional.
 * 
 * ESTRATÉGIA:
 * 1. Limpa duplicatas de emails que impedem o funcionamento da autenticação.
 * 2. Cria o índice único manualmente após a limpeza para evitar o erro 11000.
 * 3. Garante o usuário admin padrão.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando fase de saneamento de dados...");
        
        try {
            // 1. Limpeza de Emails Duplicados
            List<User> allUsers = userRepository.findAll();
            Set<String> seenEmails = new HashSet<>();
            int removedCount = 0;
            
            for (User user : allUsers) {
                if (user.getEmail() == null) {
                    userRepository.delete(user);
                    continue;
                }
                String email = user.getEmail().toLowerCase().trim();
                if (seenEmails.contains(email)) {
                    log.warn("Removendo duplicata para o email: {}", email);
                    userRepository.delete(user);
                    removedCount++;
                } else {
                    seenEmails.add(email);
                }
            }
            log.info("Saneamento concluído. Duplicatas removidas: {}", removedCount);

            // 2. Criação Manual do Índice Único
            // Isso garante que novas duplicatas não sejam criadas.
            mongoTemplate.indexOps(User.class).ensureIndex(
                new Index().on("email", Sort.Direction.ASC).unique()
            );
            log.info("Índice de email único garantido no MongoDB.");

            // 3. Usuário Admin
            ensureAdmin();

        } catch (Exception e) {
            log.error("FALHA NA FASE DE INICIALIZAÇÃO: {}", e.getMessage());
        }
    }

    private void ensureAdmin() {
        if (userRepository.findAll().stream().noneMatch(u -> "admin@wtc.com".equals(u.getEmail()))) {
            User admin = new User();
            admin.setName("Admin WTC");
            admin.setEmail("admin@wtc.com");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setRole("OPERATOR");
            admin.setStatus("Ativo");
            admin.setCompany("WTC");
            admin.setSegment("Administração");
            admin.setNotes("");
            admin.setMemberSince(new Date());
            userRepository.save(admin);
            log.info("Admin padrão criado: admin@wtc.com / password123");
        } else {
            log.info("Admin já existe e está pronto.");
        }
    }
}
