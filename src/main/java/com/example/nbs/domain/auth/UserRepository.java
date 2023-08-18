package com.example.nbs.domain.auth;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {

    @Select("select * from nbs.user where username = #{username}")
    Optional<UserEntity> findByUsername(String username);

    @Select("select * from nbs.user")
    List<UserEntity> findAll();

    @Insert("insert into nbs.user (username,password,authority,fullname,address,created_datetime,updated_datetime) value (#{username}, #{password},#{authority},#{fullname},#{address},#{dt},#{dt})")
    void insert(String username, String password, String authority, String fullname, String address, String dt);

    @Select("select * from nbs.user where id = #{id}")
    UserEntity findByUserId(long id);

    @Update("update nbs.user set authority = #{authority}, updated_datetime = #{dt} where id = #{id}")
    void updateAuthority(long id, String authority, String dt);

    @Update("update nbs.user set fullname = #{fullname}, address = #{address}, updated_datetime = #{dt} where id = #{id}")
    void updateBasicInfo(long id, String fullname, String address, String dt);

    @Update("update nbs.user set username = #{username}, updated_datetime = #{dt} where id = #{id}")
    void updateUsername(long id, String username, String dt);

    @Update("update nbs.user set password = #{password}, updated_datetime = #{dt} where id = #{id}")
    void updatePassword(long id, String password, String dt);

    @Delete("delete from nbs.user where id = #{id}")
    void delete(long id);

}

