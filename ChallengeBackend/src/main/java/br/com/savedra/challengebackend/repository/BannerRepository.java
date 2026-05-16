package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BannerRepository extends MongoRepository<Banner, String> {
    List<Banner> findBySegment(String segment);
}
