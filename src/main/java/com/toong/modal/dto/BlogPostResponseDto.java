package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostResponseDto {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String thumbnail;
    private String status;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private String author;
}
