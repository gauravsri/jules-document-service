package com.dms.backend.dto.auth;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String password;
    // In a real app, you might include email, tenant info, etc.
}
