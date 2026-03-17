package com.toong.repository;

import com.toong.modal.entity.ItineraryTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryTimelineRepository extends JpaRepository<ItineraryTimeline, Long> {

    List<ItineraryTimeline> findByItineraryId(Integer itineraryId);

}