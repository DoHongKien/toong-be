package com.toong.repository;

import com.toong.modal.entity.TourFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourFaqRepository extends JpaRepository<TourFaq, Integer> {

    List<TourFaq> findByTourId(Integer tourId);

}