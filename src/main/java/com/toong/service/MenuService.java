package com.toong.service;

import com.toong.modal.dto.*;
import com.toong.modal.enums.MenuContext;

import java.util.List;

public interface MenuService {
    // Public — luôn dùng context=CLIENT
    List<MenuResponseDto> getMainMenu();

    // Admin
    List<MenuAdminResponseDto> getAllMenusForAdmin(MenuContext context);
    MenuAdminResponseDto getMenuById(Long id);
    MenuAdminResponseDto createMenu(MenuRequestDto request);
    MenuAdminResponseDto updateMenu(Long id, MenuRequestDto request);
    void deleteMenu(Long id);
    MenuAdminResponseDto updateMenuStatus(Long id, MenuStatusDto dto);
    MenuAdminResponseDto updateMenuOrder(Long id, MenuOrderDto dto);
}
