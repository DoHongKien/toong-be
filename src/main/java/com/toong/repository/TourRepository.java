package com.toong.repository;

import com.toong.modal.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {
    Optional<Tour> findBySlug(String slug);

    @Query("select t from Tour t where (:name is null or lower(t.name) like lower(concat('%', :name, '%')))")
    Page<Tour> findTourByName(@Param("name") String name, Pageable pageable);
}
