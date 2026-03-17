package com.toong.modal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general_faqs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralFaq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Builder.Default
    private Integer sortOrder = 0;
}
