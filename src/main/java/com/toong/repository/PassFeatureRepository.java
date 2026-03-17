package com.toong.repository;

import com.toong.modal.entity.PassFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassFeatureRepository extends JpaRepository<PassFeature, Long> {

    List<PassFeature> findByPassId(Integer passId);

}