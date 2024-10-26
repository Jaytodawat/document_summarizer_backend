package com.jay.paper_summarizer.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "paper_info")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaperInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    String title;
    String filePath;
    Date uploadDate;
    long fileSize;

}
