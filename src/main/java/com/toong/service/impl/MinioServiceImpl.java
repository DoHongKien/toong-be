package com.toong.service.impl;

import com.toong.service.MinioService;
import io.minio.*;
import io.minio.http.Method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    // ----------------------------------------------------------------
    // Upload
    // ----------------------------------------------------------------

    @Override
    public String uploadImage(MultipartFile file) {
        ensureBucketExists();

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String objectName = UUID.randomUUID() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("Uploaded object '{}' to bucket '{}'", objectName, bucketName);
            return objectName;
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tải ảnh lên MinIO: " + e.getMessage(), e);
        }
    }

    // ----------------------------------------------------------------
    // Upload from URL (migration helper)
    // ----------------------------------------------------------------

    @Override
    public String uploadImageFromUrl(String imageUrl, String objectPath) {
        ensureBucketExists();
        try {
            // Mở kết nối HTTP đến URL ngoài
            HttpURLConnection conn = (HttpURLConnection) new URL(imageUrl).openConnection();
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            conn.setConnectTimeout(15_000);
            conn.setReadTimeout(30_000);
            conn.connect();

            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("HTTP " + status + " khi download: " + imageUrl);
            }

            // Xác định content type
            String contentType = conn.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = "image/jpeg";
            }
            // Trim params phiếu như "; charset=..."
            if (contentType.contains(";")) {
                contentType = contentType.substring(0, contentType.indexOf(";")).trim();
            }

            // Đọc toàn bộ bytes
            byte[] imageBytes = conn.getInputStream().readAllBytes();
            conn.disconnect();

            // Upload lên MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(imageBytes), imageBytes.length, -1)
                            .contentType(contentType)
                            .build()
            );
            log.info("Migrated '{}' → MinIO object '{}'", imageUrl, objectPath);
            return objectPath;
        } catch (Exception e) {
            log.error("Failed to migrate image from URL '{}': {}", imageUrl, e.getMessage(), e);
            throw new RuntimeException("Không thể upload ảnh từ URL: " + e.getMessage(), e);
        }
    }

    // ----------------------------------------------------------------
    // Get object stream
    // ----------------------------------------------------------------

    @Override
    public InputStream getObject(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to get object '{}' from MinIO: {}", objectName, e.getMessage(), e);
            throw new RuntimeException("Không tìm thấy ảnh: " + objectName, e);
        }
    }

    // ----------------------------------------------------------------
    // Get content type from object stat
    // ----------------------------------------------------------------

    @Override
    public String getContentType(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return stat.contentType();
        } catch (Exception e) {
            log.warn("Could not determine content type for '{}', defaulting to image/jpeg", objectName);
            return "image/jpeg";
        }
    }

    // ----------------------------------------------------------------
    // Presigned URL
    // ----------------------------------------------------------------

    @Override
    public String getPresignedUrl(String objectName) {
        if (objectName == null || objectName.isBlank()) return null;
        // Nếu đã là URL đầy đủ (Unsplash, http...) thì pass-through không xử lý
        if (objectName.startsWith("http")) return objectName;
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, java.util.concurrent.TimeUnit.DAYS)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for '{}': {}", objectName, e.getMessage());
            return objectName; // fallback: trả về object path thô
        }
    }

    // ----------------------------------------------------------------
    // Internal helpers
    // ----------------------------------------------------------------

    private void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created MinIO bucket '{}'", bucketName);
            }
        } catch (Exception e) {
            log.error("Failed to ensure bucket '{}' exists: {}", bucketName, e.getMessage(), e);
            throw new RuntimeException("Không thể kiểm tra/tạo bucket MinIO: " + e.getMessage(), e);
        }
    }
}
