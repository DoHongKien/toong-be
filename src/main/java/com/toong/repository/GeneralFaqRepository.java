package com.toong.repository;

import com.toong.modal.entity.GeneralFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralFaqRepository extends JpaRepository<GeneralFaq, Long> {
    List<GeneralFaq> findAllByOrderBySortOrderAsc();
}
