package com.cleducate.dto;

import com.cleducate.enums.Roles;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDto {
    private Long id;                // Role ID
    private Roles roleName;         // Role name (e.g., SUPER_ADMIN, ADMIN, etc.)
    private Set<PrivilegeDto> privileges;
}
