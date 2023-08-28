package com.example.nbs.domain.auth;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface UserRepository {

    List<UserEntity> findAll();

    UserEntity findByUserId(long id);

    Optional<UserEntity> findByUsername(String username);

    void insert(String username, String password, String authority, String fullname, String address, String dt);

    void updateAuthority(long id, String authority, String dt);

    void updateBasicInfo(long id, String fullname, String address, String dt);

    void updateUsername(long id, String username, String dt);

    void updatePassword(long id, String password, String dt);

    void delete(long id);

}

