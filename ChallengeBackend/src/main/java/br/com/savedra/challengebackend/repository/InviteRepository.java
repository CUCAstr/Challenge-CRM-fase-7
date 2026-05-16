package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.Invite;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InviteRepository extends MongoRepository<Invite, String> {
    List<Invite> findBySegment(String segment);
}
