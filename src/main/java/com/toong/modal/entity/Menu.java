package com.toong.modal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toong.modal.enums.MenuType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    @ToString.Exclude
    private Menu parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    @JsonIgnore
    @ToString.Exclude
    private Tour tour;

    @Column(unique = true)
    private String keyName;

    @Column(nullable = false)
    private String label;

    private String href;

    @Enumerated(EnumType.STRING)
    private MenuType type;

    // Fields for Mega Menu
    private String megaAccentTitle;
    private String megaMainTitle;
    
    @Column(columnDefinition = "TEXT")
    private String megaDescription;
    
    private String megaImage;

    private Integer orderIndex;
    
    private Boolean isActive;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    @ToString.Exclude
    private List<Menu> children;
}
