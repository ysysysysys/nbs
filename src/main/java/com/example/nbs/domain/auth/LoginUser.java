package com.example.nbs.domain.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUser {

    private long loginId;
    private String loginUsername;
    private UserEntity.Authority loginAuthority;

    public LoginUser() {

    }

    public enum Authority {
        ADMIN,
        USER
    }

}
