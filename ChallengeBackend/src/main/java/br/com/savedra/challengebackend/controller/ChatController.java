package br.com.savedra.challengebackend.controller;

import br.com.savedra.challengebackend.model.ChatRoom;
import br.com.savedra.challengebackend.model.Message;
import br.com.savedra.challengebackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void processMessage(@Payload Message message) {
        Message saved = chatService.sendMessage(message.getChatRoomId(), message);
        messagingTemplate.convertAndSendToUser(
                message.getChatRoomId(), "/queue/messages",
                saved
        );
    }

    @GetMapping("/rooms/{userId}")
    public ResponseEntity<List<ChatRoom>> getUserChatRooms(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getUserChatRooms(userId));
    }

    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable String chatRoomId) {
        System.out.println("Loading messages for room: " + chatRoomId);
        List<Message> messages = chatService.getChatMessages(chatRoomId);
        System.out.println("Found " + messages.size() + " messages");
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/messages/{chatRoomId}")
    public ResponseEntity<Message> sendMessage(@PathVariable String chatRoomId, @RequestBody Message message) {
        System.out.println("Sending message to room: " + chatRoomId + " from sender: " + message.getSenderId());
        return ResponseEntity.ok(chatService.sendMessage(chatRoomId, message));
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> getOrCreateChatRoom(
            @RequestParam String operatorId,
            @RequestParam String operatorName,
            @RequestParam String userId,
            @RequestParam String userName
    ) {
        return ResponseEntity.ok(chatService.getOrCreateChatRoom(operatorId, operatorName, userId, userName));
    }

    @PatchMapping("/messages/{messageId}/status")
    public ResponseEntity<Void> updateMessageStatus(
            @PathVariable String messageId,
            @RequestParam String status
    ) {
        chatService.updateMessageStatus(messageId, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/rooms/{chatRoomId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable String chatRoomId,
            @RequestParam String userId
    ) {
        chatService.markMessagesAsRead(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }
}
