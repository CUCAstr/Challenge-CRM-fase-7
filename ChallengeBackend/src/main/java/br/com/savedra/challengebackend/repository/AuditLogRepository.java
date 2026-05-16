package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
}
