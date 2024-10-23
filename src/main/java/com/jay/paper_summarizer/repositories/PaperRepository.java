package com.jay.paper_summarizer.repositories;

import com.jay.paper_summarizer.models.PaperInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaperRepository extends JpaRepository<PaperInfo, Integer> {

    @Query("SELECT p FROM PaperInfo p WHERE p.filePath = ?1")
    public PaperInfo findByFilePath(String filePath);


}
