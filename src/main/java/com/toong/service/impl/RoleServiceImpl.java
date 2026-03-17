package com.toong.service.impl;

import com.toong.modal.dto.AssignPermissionsDto;
import com.toong.modal.dto.PermissionResponseDto;
import com.toong.modal.dto.RoleRequestDto;
import com.toong.modal.dto.RoleResponseDto;
import com.toong.modal.entity.Permission;
import com.toong.modal.entity.Role;
import com.toong.repository.PermissionRepository;
import com.toong.repository.RoleRepository;
import com.toong.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto request) {
        Role role = Role.builder().name(request.getName()).code(request.getCode()).build();
        return toDto(roleRepository.save(role));
    }

    @Override
    public RoleResponseDto updateRole(Long id, RoleRequestDto request) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(request.getName());
        role.setCode(request.getCode());
        return toDto(roleRepository.save(role));
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) throw new RuntimeException("Role not found");
        roleRepository.deleteById(id);
    }

    @Override
    public List<PermissionResponseDto> getAllPermissions() {
        return permissionRepository.findAll().stream().map(this::toPermDto).collect(Collectors.toList());
    }

    @Override
    public List<PermissionResponseDto> getPermissionsByRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        return role.getPermissions().stream().map(this::toPermDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleResponseDto assignPermissions(Long roleId, AssignPermissionsDto dto) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(dto.getPermissionIds()));
        role.setPermissions(permissions);
        return toDto(roleRepository.save(role));
    }

    private RoleResponseDto toDto(Role r) {
        List<PermissionResponseDto> perms = r.getPermissions() == null ? List.of() :
                r.getPermissions().stream().map(this::toPermDto).collect(Collectors.toList());
        return RoleResponseDto.builder().id(r.getId()).name(r.getName()).code(r.getCode()).permissions(perms).build();
    }

    private PermissionResponseDto toPermDto(Permission p) {
        return PermissionResponseDto.builder()
                .id(p.getId()).name(p.getName()).code(p.getCode()).module(p.getModule()).build();
    }
}
