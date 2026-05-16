package br.com.savedra.challengebackend.repository;

import br.com.savedra.challengebackend.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findByParticipantsContainingOrderByLastMessageTimestampDesc(String participantId);
}
