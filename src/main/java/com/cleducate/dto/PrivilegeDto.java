package com.cleducate.dto;

import com.cleducate.enums.Privileges;
import lombok.Data;

@Data
public class PrivilegeDto {
    private Long id;                // Privilege ID
    private Privileges name;
}
