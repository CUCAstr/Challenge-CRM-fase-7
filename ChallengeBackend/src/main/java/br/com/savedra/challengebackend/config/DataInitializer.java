package br.com.savedra.challengebackend.config;

import br.com.savedra.challengebackend.model.User;
import br.com.savedra.challengebackend.model.Banner;
import br.com.savedra.challengebackend.model.Campaign;
import br.com.savedra.challengebackend.repository.UserRepository;
import br.com.savedra.challengebackend.repository.BannerRepository;
import br.com.savedra.challengebackend.repository.CampaignRepository;
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

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BannerRepository bannerRepository;
    private final CampaignRepository campaignRepository;
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 1. Limpeza e Saneamento
            mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("email", Sort.Direction.ASC).unique());

            // 2. Usuários de Teste (SPRINT 2 FINAL)
            ensureUser("Admin WTC", "admin@wtc.com", "OPERATOR", "Administração", "Ativo", "password123");
            ensureUser("Operador 2", "op2@wtc.com", "OPERATOR", "Suporte", "Ativo", "123");
            ensureUser("Cliente Finance", "finance@cliente.com", "CLIENT", "Finance", "Ativo", "password123");
            ensureUser("Cliente ESG", "esg@cliente.com", "CLIENT", "ESG", "Ativo", "password123");

            // 3. Seed de Conteúdo
            if (bannerRepository.count() == 0) {
                Banner b = new Banner();
                b.setTitle("Bem-vindo ao WTC");
                b.setDescription("Aproveite nossas ofertas exclusivas.");
                b.setSegment("Todos");
                bannerRepository.save(b);
            }
        } catch (Exception e) {
            log.error("Erro na inicialização: {}", e.getMessage());
        }
    }

    private void ensureUser(String name, String email, String role, String segment, String status, String password) {
        if (userRepository.findAll().stream().noneMatch(u -> email.equalsIgnoreCase(u.getEmail()))) {
            User user = new User();
            user.setName(name);
            user.setEmail(email.toLowerCase().trim());
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setStatus(status);
            user.setCompany("WTC");
            user.setSegment(segment);
            user.setNotes("");
            user.setMemberSince(new Date());
            userRepository.save(user);
            log.info("Usuário criado: {} com senha {}", email, password);
        }
    }
}
