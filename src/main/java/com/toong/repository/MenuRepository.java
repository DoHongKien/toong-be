package com.toong.repository;

import com.toong.modal.entity.Menu;
import com.toong.modal.enums.MenuContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // Public: ROOT menu đang active theo context
    List<Menu> findByParentIsNullAndContextAndIsActiveTrueOrderByOrderIndexAsc(MenuContext context);

    // Admin: ROOT menu theo context (kể cả inactive)
    List<Menu> findByParentIsNullAndContextOrderByOrderIndexAsc(MenuContext context);
}
