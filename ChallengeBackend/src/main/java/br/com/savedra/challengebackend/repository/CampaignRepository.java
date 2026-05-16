package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.Campaign;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CampaignRepository extends MongoRepository<Campaign, String> {
    List<Campaign> findBySegment(String segment);
}
