package com.toong.repository;

import com.toong.modal.entity.PassOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassOrderRepository extends JpaRepository<PassOrder, Long> {

    PassOrder findByOrderCode(String orderCode);

}