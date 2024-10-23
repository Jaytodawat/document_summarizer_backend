package com.jay.paper_summarizer.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.jay.paper_summarizer.models.PaperInfo;
import com.jay.paper_summarizer.repositories.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

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

    public String uploadFile(MultipartFile multipartFile) {
        try{
            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(getExtension(fileName));
            File file = convertToFile(multipartFile, fileName);
            String fileUrl = uploadFileToFirebase(file, fileName);

            PaperInfo paperInfo = PaperInfo.builder()
                    .title(fileName)
                    .filePath(fileUrl)
                    .build();
            paperRepository.save(paperInfo);
            file.delete();
            return fileUrl;


        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed";
        }
    }

    private void downloadFileFromFirebase(String fileName, String destination) throws IOException {
        InputStream inputStream = new FileInputStream("D:\\paper_summarizer\\src\\main\\resources\\paper-summarizer-b85db-firebase-adminsdk-4dpvh-74975a92e1.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of("paper-summarizer-b85db.appspot.com", fileName));
        blob.downloadTo(Paths.get(destination));

    }

    public String downloadFile(String fileUrl){
        String fileName = paperRepository.findByFilePath(fileUrl).getTitle();
        String destinationPath = "F:\\Downloads\\" + fileName;
        try {
            downloadFileFromFirebase(fileName, destinationPath);
            return "File Downloaded Successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "File Download Failed";
        }


    }
}
