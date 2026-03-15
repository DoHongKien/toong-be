package com.toong.repository;

import com.toong.modal.entity.TourLuggage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourLuggageRepository extends JpaRepository<TourLuggage, Integer> {

    List<TourLuggage> findByTourId(Integer tourId);

}