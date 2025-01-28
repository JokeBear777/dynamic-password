package com.InfoSec.dynamic_password.domain.member.type;

public enum MemberRole {
    ADMIN,
    USER,
    GUEST,
    INACTIVE;


    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
