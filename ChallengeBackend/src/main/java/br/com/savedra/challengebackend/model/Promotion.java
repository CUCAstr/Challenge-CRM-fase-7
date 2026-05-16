package br.com.savedra.challengebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "promotions")
public class Promotion {
    @Id
    private String id;
    private String title;
    private String description;
    private String originalValue;
    private String promotionValue;
    private String dateExpiresIn;
    private String hoursExpiresIn;
    private String segment;
}
