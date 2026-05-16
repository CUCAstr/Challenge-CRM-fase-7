package br.com.savedra.challengebackend.service;

import br.com.savedra.challengebackend.model.ChatRoom;
import br.com.savedra.challengebackend.model.Message;
import br.com.savedra.challengebackend.repository.ChatRoomRepository;
import br.com.savedra.challengebackend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public List<ChatRoom> getUserChatRooms(String userId) {
        return chatRoomRepository.findByParticipantsContainingOrderByLastMessageTimestampDesc(userId);
    }

    public List<Message> getChatMessages(String chatRoomId) {
        return messageRepository.findByChatRoomIdOrderByTimestampAsc(chatRoomId);
    }

    public Message sendMessage(String chatRoomId, Message message) {
        message.setChatRoomId(chatRoomId);
        message.setTimestamp(new Date());
        message.setStatus("SENT");
        Message savedMessage = messageRepository.save(message);

        // Update ChatRoom last message
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        chatRoom.setLastMessageText(message.getText());
        chatRoom.setLastMessageTimestamp(message.getTimestamp());
        chatRoomRepository.save(chatRoom);

        return savedMessage;
    }

    public ChatRoom getOrCreateChatRoom(String operatorId, String operatorName, String userId, String userName) {
        String roomId = generateRoomId(operatorId, userId);
        Optional<ChatRoom> existingRoom = chatRoomRepository.findById(roomId);
        
        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        ChatRoom newRoom = new ChatRoom();
        newRoom.setId(roomId);
        newRoom.setOperatorId(operatorId);
        newRoom.setOperatorName(operatorName);
        newRoom.setUserId(userId);
        newRoom.setUserName(userName);
        newRoom.setParticipants(Arrays.asList(operatorId, userId));
        return chatRoomRepository.save(newRoom);
    }

    private String generateRoomId(String id1, String id2) {
        if (id1.compareTo(id2) > 0) {
            return id1 + "_" + id2;
        } else {
            return id2 + "_" + id1;
        }
    }

    public void updateMessageStatus(String messageId, String status) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setStatus(status);
        messageRepository.save(message);
    }
}
