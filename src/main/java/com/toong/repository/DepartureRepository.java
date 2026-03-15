package com.toong.repository;

import com.toong.modal.entity.Departure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartureRepository extends JpaRepository<Departure, Integer> {

    List<Departure> findByTourId(Long tourId);
    List<Departure> findByTourSlug(String slug);

}