package com.toong.modal.dto;

import lombok.Data;

@Data
public class BannerRequestDto {
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Boolean isActive;
}
