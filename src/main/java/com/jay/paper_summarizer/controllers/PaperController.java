package com.jay.paper_summarizer.controllers;

import com.jay.paper_summarizer.dto.PaperInfoDTO;
import com.jay.paper_summarizer.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/paper_summarizer")
public class PaperController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<CompletableFuture<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("Hello");
        try {
            CompletableFuture<String> fileUrl = fileService.uploadFile(file);
            System.out.println(fileUrl);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(CompletableFuture.completedFuture("File upload failed"));
        }
    }


    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filePath") String fileName) {
        try {
            byte[] fileData = fileService.downloadFile(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(fileData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<PaperInfoDTO>> getAllUploadedFiles() {
        System.out.println("Hello");
        List<PaperInfoDTO> fileUrls = fileService.getAllUploadedFiles();
        return ResponseEntity.ok(fileUrls);
    }
}
