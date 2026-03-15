package com.toong.repository;

import com.toong.modal.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // Chỉ lấy những menu cấp cao nhất (không có cha)
    List<Menu> findByParentIsNullAndIsActiveTrueOrderByOrderIndexAsc();
}
