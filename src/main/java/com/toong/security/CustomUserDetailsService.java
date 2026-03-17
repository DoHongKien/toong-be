package com.toong.security;

import com.toong.modal.entity.Employee;
import com.toong.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if ("locked".equals(employee.getStatus())) {
            throw new UsernameNotFoundException("Account is locked: " + username);
        }

        // Build authorities từ permissions của role
        List<SimpleGrantedAuthority> authorities = List.of();
        if (employee.getRole() != null && employee.getRole().getPermissions() != null) {
            authorities = employee.getRole().getPermissions().stream()
                    .map(p -> new SimpleGrantedAuthority("PERM_" + p.getCode()))
                    .collect(Collectors.toList());
        }

        return new User(employee.getUsername(), employee.getPasswordHash(), authorities);
    }
}
