package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PromotionRepository extends MongoRepository<Promotion, String> {
    List<Promotion> findBySegment(String segment);
}
