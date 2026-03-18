package com.toong.security;

import com.toong.modal.entity.Employee;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final Employee employee;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Employee employee) {
        this.employee = employee;

        if (employee.getRole() != null && employee.getRole().getPermissions() != null) {
            this.authorities = employee.getRole().getPermissions().stream()
                    .map(p -> new SimpleGrantedAuthority("PERM_" + p.getCode()))
                    .collect(Collectors.toList());
        } else {
            this.authorities = List.of();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return employee.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"locked".equals(employee.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "active".equals(employee.getStatus());
    }
}
