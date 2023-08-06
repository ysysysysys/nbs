package com.example.nbs.domain.auth;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {

    @Select("select * from nbs.user where username = #{username}")
    Optional<UserEntity> findByUsername(String username);

    @Select("select * from nbs.user")
    List<UserEntity> findAll();

    @Insert("insert into nbs.user (username,password,authority,fullname,address,created_datetime,updated_datetime) value (#{username}, #{password},#{authority},'seto','sapporo','2023-07-16 12:30:34','2023-07-16 12:30:34')")
    void insert(String username, String password, String authority);
}
