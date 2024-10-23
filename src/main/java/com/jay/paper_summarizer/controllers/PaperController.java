package com.jay.paper_summarizer.controllers;

import com.jay.paper_summarizer.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/paper_summarizer")
public class PaperController {

    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.uploadFile(file);
        return ResponseEntity.ok("File uploaded successfully at: " + fileUrl);

    }

    @GetMapping("/download")
    public ResponseEntity<String> downloadFile(@RequestParam("filePath") String filePath) {
        System.out.println(filePath);
        String result = fileService.downloadFile(filePath);
        return ResponseEntity.ok(result);
    }
}
