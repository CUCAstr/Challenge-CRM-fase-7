package br.com.savedra.challengebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom {
    @Id
    private String id;
    private String operatorId;
    private String operatorName;
    private String userId;
    private String userName;
    private List<String> participants;
    private String lastMessageText;
    private Date lastMessageTimestamp;
}
