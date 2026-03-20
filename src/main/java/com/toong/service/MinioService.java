package com.toong.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {

    /**
     * Upload file lên MinIO và trả về tên object đã lưu.
     *
     * @param file MultipartFile từ request
     * @return objectName (key) trên MinIO
     */
    String uploadImage(MultipartFile file);

    /**
     * Download ảnh từ URL rồi upload lên MinIO với object path tuỳ chỉnh.
     * Dùng cho migration: download từ Unsplash/external URL → lưu vào folder cụ thể.
     *
     * @param imageUrl   URL ảnh cần download (http/https)
     * @param objectPath path đích trên MinIO, e.g. "tours/taynguyen/ta-nang-phan-dung/hero.jpg"
     * @return objectPath đã lưu thành công
     */
    String uploadImageFromUrl(String imageUrl, String objectPath);

    /**
     * Lấy InputStream của object từ MinIO để stream về client.
     *
     * @param objectName tên object trên MinIO
     * @return InputStream
     */
    InputStream getObject(String objectName);

    /**
     * Lấy content-type của object.
     *
     * @param objectName tên object trên MinIO
     * @return content-type string, e.g. "image/jpeg"
     */
    String getContentType(String objectName);

    /**
     * Sinh presigned URL cho phép truy cập trực tiếp vào object MinIO.
     * URL có hiệu lực trong 7 ngày.
     * Nếu objectName đã là URL đầy đủ (bắt đầu http) thì trả về luôn không xử lý.
     *
     * @param objectName tên object trên MinIO hoặc URL đầy đủ
     * @return presigned URL có thể dùng trực tiếp trong frontend
     */
    String getPresignedUrl(String objectName);
}
