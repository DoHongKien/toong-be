package com.toong.repository;

import com.toong.modal.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByStatus(String status, Pageable pageable);
    Page<ContactMessage> findAll(Pageable pageable);
    long countByStatus(String status);
}
