package com.example.nbs.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEntity {

    private long id;
    private String username;
    private String password;
    private Authority authority;

    public enum Authority {
        ADMIN,
        USER
    }

    private String fullname;
    private String address;
    private String created_datetime;
    private String updated_datetime;

}
