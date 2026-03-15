package com.toong.modal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toong.modal.enums.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuResponseDto {
    private Long id;
    private String keyName;
    private String label;
    private String href;
    private MenuType type;
    
    // Mega Menu fields
    private String megaAccentTitle;
    private String megaMainTitle;
    private String megaDescription;
    private String megaImage;
    
    private Integer orderIndex;
    private List<MenuResponseDto> children;
}
