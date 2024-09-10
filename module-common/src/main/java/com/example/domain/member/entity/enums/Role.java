package com.example.domain.member.entity.enums;

public enum Role {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_MANAGER("MANAGER"),
    ROLE_SUPER_ADMIN("SUPER_ADMIN");

    // "USER", "ADMIN", "MANAGER"
    private String value;

    // Constructor
    Role(String value) {
        this.value = value;
    }

    // GetValue
    public String getValue() {
        return this.value;
    }
}
