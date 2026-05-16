package br.com.savedra.challengebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "campaigns")
public class Campaign {
    @Id
    private String id;
    private String title;
    private String body;
    private String url;
    private String segment;
    private Date startDate;
    private Date endDate;
    private List<Map<String, String>> actions; // e.g., [{"action": "btn1", "title": "Inscrever-se"}]
    private Map<String, String> actionUrls; // e.g., {"btn1": "https://..."}
}
