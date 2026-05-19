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
        String trimmedId = chatRoomId.trim();
        System.out.println("[CHAT_DEBUG] BUSCANDO MENSAGENS: Sala=[" + trimmedId + "]");
        try {
            List<Message> messages = messageRepository.findByChatRoomIdOrderByTimestampAsc(trimmedId);
            System.out.println("[CHAT_DEBUG] SUCESSO: Encontradas " + messages.size() + " mensagens para a sala [" + trimmedId + "]");
            return messages;
        } catch (Exception e) {
            System.err.println("[CHAT_DEBUG] ERRO CRÍTICO ao buscar mensagens: " + e.getMessage());
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    public Message sendMessage(String chatRoomId, Message message) {
        String trimmedRoomId = chatRoomId.trim();
        System.out.println("[CHAT_DEBUG] SALVANDO MENSAGEM: Sala=[" + trimmedRoomId + "], Remetente=[" + message.getSenderId() + "]");
        
        try {
            message.setChatRoomId(trimmedRoomId);
            message.setTimestamp(new Date());
            message.setStatus("SENT");
            
            // GARANTIA ABSOLUTA DE NOVO REGISTRO
            message.setId(null); 
            Message savedMessage = messageRepository.save(message);
            System.out.println("[CHAT_DEBUG] MENSAGEM PERSISTIDA: Novo ID=[" + savedMessage.getId() + "]");

            // Atualização da Sala
            ChatRoom chatRoom = chatRoomRepository.findById(trimmedRoomId).orElseGet(() -> {
                System.out.println("[CHAT_DEBUG] SALA NÃO ENCONTRADA. Criando dinamicamente: " + trimmedRoomId);
                ChatRoom newRoom = new ChatRoom();
                newRoom.setId(trimmedRoomId);
                String[] parts = trimmedRoomId.split("_");
                if (parts.length == 2) {
                    newRoom.setParticipants(Arrays.asList(parts[0], parts[1]));
                    newRoom.setOperatorId(parts[0]); 
                    newRoom.setUserId(parts[1]);
                } else {
                    newRoom.setParticipants(Arrays.asList(trimmedRoomId));
                }
                return newRoom;
            });
            
            chatRoom.setLastMessageText(message.getText());
            chatRoom.setLastMessageTimestamp(message.getTimestamp());
            chatRoomRepository.save(chatRoom);
            System.out.println("[CHAT_DEBUG] SALA ATUALIZADA: " + trimmedRoomId);

            return savedMessage;
        } catch (Exception e) {
            System.err.println("[CHAT_DEBUG] ERRO AO SALVAR MENSAGEM: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public ChatRoom getOrCreateChatRoom(String operatorId, String operatorName, String userId, String userName) {
        String opId = operatorId.trim();
        String uId = userId.trim();
        String roomId = generateRoomId(opId, uId);
        
        System.out.println("[CHAT_DEBUG] GET_OR_CREATE_ROOM: Op=[" + opId + "], User=[" + uId + "] -> ID=[" + roomId + "]");
        
        Optional<ChatRoom> existingRoom = chatRoomRepository.findById(roomId);
        if (existingRoom.isPresent()) {
            System.out.println("[CHAT_DEBUG] SALA JÁ EXISTE.");
            return existingRoom.get();
        }

        System.out.println("[CHAT_DEBUG] CRIANDO NOVA SALA.");
        ChatRoom newRoom = new ChatRoom();
        newRoom.setId(roomId);
        newRoom.setOperatorId(opId);
        newRoom.setOperatorName(operatorName);
        newRoom.setUserId(uId);
        newRoom.setUserName(userName);
        newRoom.setParticipants(Arrays.asList(opId, uId));
        return chatRoomRepository.save(newRoom);
    }

    private String generateRoomId(String id1, String id2) {
        String s1 = id1.trim();
        String s2 = id2.trim();
        // CORREÇÃO: Ordem A-Z para bater com o Android
        if (s1.compareTo(s2) < 0) {
            return s1 + "_" + s2;
        } else {
            return s2 + "_" + s1;
        }
    }

    public void updateMessageStatus(String messageId, String status) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setStatus(status);
        messageRepository.save(message);
    }

    public void markMessagesAsRead(String chatRoomId, String readerId) {
        System.out.println("[CHAT_DEBUG] Marcando mensagens como LIDO para sala: " + chatRoomId);
        List<Message> messages = messageRepository.findByChatRoomIdOrderByTimestampAsc(chatRoomId);
        boolean updated = false;
        for (Message msg : messages) {
            if (!msg.getSenderId().equals(readerId) && !"READ".equals(msg.getStatus())) {
                msg.setStatus("READ");
                messageRepository.save(msg);
                updated = true;
            }
        }
        if (updated) {
            System.out.println("[CHAT_DEBUG] Status atualizado para READ.");
        }
    }
}
