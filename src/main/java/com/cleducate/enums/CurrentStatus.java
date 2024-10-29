package com.cleducate.enums;

public enum CurrentStatus {
    ACTIVE,                // The user is active and can access the system
    INACTIVE,              // The user is inactive and cannot access the system
    SUSPENDED,             // The user is suspended due to policy violations
    PENDING_VERIFICATION,   // The user account is awaiting verification
    DELETED,               // The user account has been deleted
    BANNED,                // The user is banned from using the system
    GRADUATED              // The user has completed their course(s) and may have limited access
}
