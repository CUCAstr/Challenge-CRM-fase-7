package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.Segment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SegmentRepository extends MongoRepository<Segment, String> {
}
