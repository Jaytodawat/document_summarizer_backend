package com.jay.paper_summarizer.controllers;

import com.jay.paper_summarizer.dto.PaperInfoDTO;
import com.jay.paper_summarizer.services.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/paper_summarizer")
@Slf4j
@AllArgsConstructor
public class PaperController {


    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<CompletableFuture<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());
        try {
            CompletableFuture<String> fileUrl = fileService.uploadFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e){
            log.error("Error uploading file: {}", e.getMessage());
            return ResponseEntity.status(500).body(CompletableFuture.completedFuture("File upload failed"));
        }
    }


    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filePath") String fileName) {
        log.info("Downloading file: {}", fileName);
        try {
            byte[] fileData = fileService.downloadFile(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(fileData);
        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<PaperInfoDTO>> getAllUploadedFiles() {
        log.info("Getting all uploaded files");
        List<PaperInfoDTO> fileUrls = fileService.getAllUploadedFiles();
        return ResponseEntity.ok(fileUrls);
    }
}
