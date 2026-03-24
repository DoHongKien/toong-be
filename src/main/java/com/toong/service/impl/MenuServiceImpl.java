package com.toong.service.impl;

import com.toong.modal.dto.*;
import com.toong.modal.entity.Menu;
import com.toong.modal.enums.MenuContext;
import com.toong.repository.MenuRepository;
import com.toong.repository.TourRepository;
import com.toong.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final TourRepository tourRepository;

    // ===================== PUBLIC =====================

    @Override
    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMainMenu() {
        // Public luôn lấy context = CLIENT
        List<Menu> rootMenus = menuRepository
                .findByParentIsNullAndContextAndIsActiveTrueOrderByOrderIndexAsc(MenuContext.CLIENT);
        return rootMenus.stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }

    // ===================== ADMIN =====================

    @Override
    @Transactional(readOnly = true)
    public List<MenuAdminResponseDto> getAllMenusForAdmin(MenuContext context) {
        MenuContext ctx = (context != null) ? context : MenuContext.CLIENT;
        List<Menu> rootMenus = menuRepository
                .findByParentIsNullAndContextOrderByOrderIndexAsc(ctx);

        // Khi context = CMS: lọc RBAC dựa trên quyền của user hiện tại
        if (ctx == MenuContext.CMS) {
            Set<String> userPerms = getCurrentUserPermissions();
            return rootMenus.stream()
                    .map(menu -> filterCmsMenuByPermission(menu, userPerms))
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
        }

        // CLIENT: trả về toàn bộ (admin CMS luôn thấy đủ menu website)
        return rootMenus.stream()
                .map(this::convertToAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MenuAdminResponseDto getMenuById(Long id) {
        return convertToAdminDto(findById(id));
    }

    @Override
    @Transactional
    public MenuAdminResponseDto createMenu(MenuRequestDto request) {
        Menu menu = new Menu();
        mapRequest(request, menu);
        return convertToAdminDto(menuRepository.save(menu));
    }

    @Override
    @Transactional
    public MenuAdminResponseDto updateMenu(Long id, MenuRequestDto request) {
        Menu menu = findById(id);
        mapRequest(request, menu);
        return convertToAdminDto(menuRepository.save(menu));
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new RuntimeException("Menu not found: " + id);
        }
        menuRepository.deleteById(id);
    }

    @Override
    @Transactional
    public MenuAdminResponseDto updateMenuStatus(Long id, MenuStatusDto dto) {
        Menu menu = findById(id);
        if (dto.getIsActive() != null) menu.setIsActive(dto.getIsActive());
        return convertToAdminDto(menuRepository.save(menu));
    }

    @Override
    @Transactional
    public MenuAdminResponseDto updateMenuOrder(Long id, MenuOrderDto dto) {
        Menu menu = findById(id);
        if (dto.getOrderIndex() != null) menu.setOrderIndex(dto.getOrderIndex());
        return convertToAdminDto(menuRepository.save(menu));
    }

    // ===================== RBAC HELPERS =====================

    /**
     * Lấy tập quyền của user hiện tại từ SecurityContext.
     * Authorities được lưu dạng "PERM_<code>" (VD: "PERM_TOUR_VIEW").
     */
    private Set<String> getCurrentUserPermissions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Set.of();
        return auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                // Strip prefix "PERM_" để so sánh với requiredPermission lưu trong DB
                .filter(a -> a.startsWith("PERM_"))
                .map(a -> a.substring(5))
                .collect(Collectors.toSet());
    }

    /**
     * Filter RBAC đệ quy cho menu CMS.
     * - Nếu menu là lá (ITEM): chỉ trả về nếu user có quyền hoặc requiredPermission = null.
     * - Nếu menu là nhóm (SIMPLE/MEGA_PARENT): sau khi lọc children, chỉ giữ lại nếu còn ít nhất 1 child.
     * Trả về null nếu menu bị ẩn hoàn toàn.
     */
    private MenuAdminResponseDto filterCmsMenuByPermission(Menu menu, Set<String> userPerms) {
        // Kiểm tra quyền của chính menu này
        String reqPerm = menu.getRequiredPermission();
        boolean hasAccess = (reqPerm == null) || userPerms.contains(reqPerm);
        if (!hasAccess) return null;

        MenuAdminResponseDto dto = convertToAdminDto(menu);

        // Xử lý đệ quy children
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            List<MenuAdminResponseDto> filteredChildren = menu.getChildren().stream()
                    .map(child -> filterCmsMenuByPermission(child, userPerms))
                    .filter(child -> child != null)
                    .collect(Collectors.toList());

            // Nếu menu là nhóm (không có href) và mọi children đều bị ẩn → ẩn cả nhóm
            if (filteredChildren.isEmpty() && menu.getHref() == null) {
                return null;
            }
            dto.setChildren(filteredChildren.isEmpty() ? null : filteredChildren);
        }

        return dto;
    }

    // ===================== PRIVATE HELPERS =====================

    private Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));
    }

    private void mapRequest(MenuRequestDto req, Menu menu) {
        if (req.getLabel() != null)               menu.setLabel(req.getLabel());
        if (req.getKeyName() != null)             menu.setKeyName(req.getKeyName());
        if (req.getHref() != null)                menu.setHref(req.getHref());
        if (req.getType() != null)                menu.setType(req.getType());
        if (req.getMegaAccentTitle() != null)     menu.setMegaAccentTitle(req.getMegaAccentTitle());
        if (req.getMegaMainTitle() != null)       menu.setMegaMainTitle(req.getMegaMainTitle());
        if (req.getMegaDescription() != null)     menu.setMegaDescription(req.getMegaDescription());
        if (req.getMegaImage() != null)           menu.setMegaImage(req.getMegaImage());
        if (req.getOrderIndex() != null)          menu.setOrderIndex(req.getOrderIndex());
        if (req.getIsActive() != null)            menu.setIsActive(req.getIsActive());
        if (req.getContext() != null)             menu.setContext(req.getContext());
        if (req.getIcon() != null)                menu.setIcon(req.getIcon());
        if (req.getRequiredPermission() != null)  menu.setRequiredPermission(req.getRequiredPermission());

        // Set parent
        if (req.getParentId() != null) {
            menu.setParent(menuRepository.findById(req.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found: " + req.getParentId())));
        } else {
            menu.setParent(null);
        }

        // Set tour (optional)
        if (req.getTourId() != null) {
            menu.setTour(tourRepository.findById(req.getTourId())
                    .orElseThrow(() -> new RuntimeException("Tour not found: " + req.getTourId())));
        } else {
            menu.setTour(null);
        }
    }

    private MenuResponseDto convertToPublicDto(Menu menu) {
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
                    .map(this::convertToPublicDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private MenuAdminResponseDto convertToAdminDto(Menu menu) {
        MenuAdminResponseDto dto = MenuAdminResponseDto.builder()
                .id(menu.getId())
                .parentId(menu.getParent() != null ? menu.getParent().getId() : null)
                .tourId(menu.getTour() != null ? menu.getTour().getId() : null)
                .keyName(menu.getKeyName())
                .label(menu.getLabel())
                .href(menu.getHref())
                .type(menu.getType())
                .megaAccentTitle(menu.getMegaAccentTitle())
                .megaMainTitle(menu.getMegaMainTitle())
                .megaDescription(menu.getMegaDescription())
                .megaImage(menu.getMegaImage())
                .orderIndex(menu.getOrderIndex())
                .isActive(menu.getIsActive())
                .context(menu.getContext())
                .icon(menu.getIcon())
                .requiredPermission(menu.getRequiredPermission())
                .build();

        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            dto.setChildren(menu.getChildren().stream()
                    .map(this::convertToAdminDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
