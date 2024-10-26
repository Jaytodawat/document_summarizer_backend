package com.jay.paper_summarizer.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaperInfoDTO {
    String fileName;
    String fileUrl;
    Date uploadDate;
    long fileSize;
}
