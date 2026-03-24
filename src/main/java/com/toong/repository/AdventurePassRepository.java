package com.toong.repository;

import com.toong.modal.entity.AdventurePass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdventurePassRepository extends JpaRepository<AdventurePass, Long> {

    List<AdventurePass> findAllByOrderByPriceAsc();
}