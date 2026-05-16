package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatRoomIdOrderByTimestampAsc(String chatRoomId);
}
