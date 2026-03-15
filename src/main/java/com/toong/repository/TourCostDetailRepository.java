package com.toong.repository;

import com.toong.modal.entity.TourCostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourCostDetailRepository extends JpaRepository<TourCostDetail, Integer> {

    List<TourCostDetail> findByTourId(Integer tourId);

}