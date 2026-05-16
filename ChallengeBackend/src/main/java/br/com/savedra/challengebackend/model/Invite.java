package br.com.savedra.challengebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invites")
public class Invite {
    @Id
    private String id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String location;
    private String segment;
}
