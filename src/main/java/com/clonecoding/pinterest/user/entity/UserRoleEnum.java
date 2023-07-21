package com.clonecoding.pinterest.user.entity;

public enum UserRoleEnum {
    USER(Authority.user),
    ADMIN(Authority.master);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String user = "ROLE_USER";
        public static final String master = "ROLE_MASTER";
    }
}
