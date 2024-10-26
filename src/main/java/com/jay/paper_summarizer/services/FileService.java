package com.jay.paper_summarizer.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.jay.paper_summarizer.dto.PaperInfoDTO;
import com.jay.paper_summarizer.models.PaperInfo;
import com.jay.paper_summarizer.repositories.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    PaperRepository paperRepository;

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);
        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;

    }
    //
    private String uploadFileToFirebase(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("paper-summarizer-b85db.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        InputStream inputStream = new FileInputStream("D:\\paper_summarizer\\src\\main\\resources\\paper-summarizer-b85db-firebase-adminsdk-4dpvh-74975a92e1.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        String fileUrl = "https://storage.googleapis.com/paper-summarizer-b85db.appspot.com/o/%s?alt=media";
        return String.format(fileUrl, URLEncoder.encode(fileName, StandardCharsets.UTF_8));

    }
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Async
    public CompletableFuture<String> uploadFile(MultipartFile multipartFile) {
        try {
            // Generate a unique file name and convert the MultipartFile to a File
            String fileName = multipartFile.getOriginalFilename();

            String uniqueName = UUID.randomUUID().toString().concat(getExtension(fileName));
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            fileName = fileName.concat(uniqueName);
            File file = convertToFile(multipartFile, fileName);

            // Perform the asynchronous upload to Firebase
            String fileUrl = uploadFileToFirebase(file, fileName);

            // Save file details to the database
            PaperInfo paperInfo = PaperInfo.builder()
                    .title(fileName)
                    .filePath(fileUrl)
                    .uploadDate(new Date())
                    .fileSize(multipartFile.getSize())
                    .build();
            paperRepository.save(paperInfo);

            // Clean up temporary file
            file.delete();

            return CompletableFuture.completedFuture(fileUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("File upload failed");
        }
    }

    private byte[] downloadFileFromFirebase(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream("D:\\paper_summarizer\\src\\main\\resources\\paper-summarizer-b85db-firebase-adminsdk-4dpvh-74975a92e1.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        Blob blob = storage.get(BlobId.of("paper-summarizer-b85db.appspot.com", fileName));

        if (blob == null) {
            throw new FileNotFoundException("File not found in Firebase Storage.");
        }

        return blob.getContent();
    }

    public byte[] downloadFile(String fileName) {

        try {
            return downloadFileFromFirebase(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File Download Failed", e);
        }
    }

    public List<PaperInfoDTO> getAllUploadedFiles() {
        return paperRepository.findAll().stream()
                .map((paperInfo) -> PaperInfoDTO.builder()
                        .fileName(paperInfo.getTitle())
                        .fileUrl(paperInfo.getFilePath())
                        .uploadDate(paperInfo.getUploadDate())
                        .fileSize(paperInfo.getFileSize())
                        .build()).toList();
    }
}
