package com.toong.service.impl;

import com.toong.modal.dto.MenuResponseDto;
import com.toong.modal.entity.Menu;
import com.toong.repository.MenuRepository;
import com.toong.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMainMenu() {
        // 1. Lấy tất cả menu cấp ROOT (không có cha) và đang active
        List<Menu> rootMenus = menuRepository.findByParentIsNullAndIsActiveTrueOrderByOrderIndexAsc();

        // 2. Chuyển đổi và tự động đệ quy lấy menu con
        return rootMenus.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MenuResponseDto convertToDto(Menu menu) {
        MenuResponseDto dto = MenuResponseDto.builder()
                .id(menu.getId())
                .keyName(menu.getKeyName())
                .label(menu.getLabel())
                .href(menu.getHref())
                .type(menu.getType())
                .megaAccentTitle(menu.getMegaAccentTitle())
                .megaMainTitle(menu.getMegaMainTitle())
                .megaDescription(menu.getMegaDescription())
                .megaImage(menu.getMegaImage())
                .orderIndex(menu.getOrderIndex())
                .build();

        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            dto.setChildren(menu.getChildren().stream()
                    .filter(child -> child.getIsActive() != null && child.getIsActive())
                    .map(this::convertToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
