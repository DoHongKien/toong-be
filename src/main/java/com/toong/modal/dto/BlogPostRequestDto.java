package com.toong.modal.dto;

import lombok.Data;

@Data
public class BlogPostRequestDto {
    private String title;
    private String slug;
    private String content;
    private String thumbnail;
    private String status;
}
