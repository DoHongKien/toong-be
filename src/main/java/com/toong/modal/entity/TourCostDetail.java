package com.toong.modal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_cost_details")
public class TourCostDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    private Boolean isIncluded;

    private String content;

    @Column(name = "sort_order")
    private Integer sortOrder;
}