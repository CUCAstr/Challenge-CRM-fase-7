package br.com.savedra.challengebackend.service;

import br.com.savedra.challengebackend.model.AuditLog;
import br.com.savedra.challengebackend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(String action, String details) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AuditLog log = new AuditLog();
        log.setUserEmail(email);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(new Date());
        auditLogRepository.save(log);
    }
}
