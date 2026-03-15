package com.toong.service;

import com.toong.modal.dto.MenuResponseDto;
import java.util.List;

public interface MenuService {
    List<MenuResponseDto> getMainMenu();
}
