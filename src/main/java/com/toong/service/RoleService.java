package com.toong.service;

import com.toong.modal.dto.AssignPermissionsDto;
import com.toong.modal.dto.PermissionResponseDto;
import com.toong.modal.dto.RoleRequestDto;
import com.toong.modal.dto.RoleResponseDto;

import java.util.List;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto createRole(RoleRequestDto request);
    RoleResponseDto updateRole(Long id, RoleRequestDto request);
    void deleteRole(Long id);
    List<PermissionResponseDto> getAllPermissions();
    List<PermissionResponseDto> getPermissionsByRole(Long roleId);
    RoleResponseDto assignPermissions(Long roleId, AssignPermissionsDto dto);
}
