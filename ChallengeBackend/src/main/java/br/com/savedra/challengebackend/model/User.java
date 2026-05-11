package br.com.savedra.challengebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String company;
    private String email;
    private String password;
    private String role; // OPERATOR or CLIENT
    private String segment;
    private Integer score;
    private String status;
    private Date memberSince;
    private String notes;
    private String gender;
    private String phone;
    private String category;
}
