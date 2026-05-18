package br.com.savedra.challengebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "campaigns")
public class Campaign {
    @Id
    private String id;
    private String title;
    private String description;
    private String segment;
    private String startDate;
    private String endDate;
}
