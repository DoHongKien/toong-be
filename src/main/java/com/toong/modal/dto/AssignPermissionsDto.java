package com.toong.modal.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignPermissionsDto {
    private List<Long> permissionIds;
}
