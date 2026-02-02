package com.LMS.service;

import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile file, String folder) {

        try {
            Bucket bucket = StorageClient.getInstance().bucket();

            String fileName = folder + "/"
                    + UUID.randomUUID()
                    + "-" + file.getOriginalFilename();

            Blob blob = bucket.create(
                    fileName,
                    file.getBytes(),
                    file.getContentType()
            );

            return "https://firebasestorage.googleapis.com/v0/b/"
                    + bucket.getName()
                    + "/o/"
                    + URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8)
                    + "?alt=media";

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}


