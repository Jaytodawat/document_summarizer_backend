package com.jay.paper_summarizer.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String password;
}
