package com.toong.modal.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    private LocalDateTime createdTime;

    private String createdBy;

    private LocalDateTime modifiedTime;

    private String modifiedBy;
}
