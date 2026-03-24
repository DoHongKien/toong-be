package com.toong.modal.dto;

import com.toong.modal.enums.MenuContext;
import com.toong.modal.enums.MenuType;
import lombok.Data;

@Data
public class MenuRequestDto {
    private Long parentId;
    private Long tourId;
    private String keyName;
    private String label;
    private String href;
    private MenuType type;
    private String megaAccentTitle;
    private String megaMainTitle;
    private String megaDescription;
    private String megaImage;
    private Integer orderIndex;
    private Boolean isActive;
    private MenuContext context;
    private String icon;
    private String requiredPermission;
}
