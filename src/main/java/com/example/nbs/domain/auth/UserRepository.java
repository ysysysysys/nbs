package com.example.nbs.domain.auth;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update nbs.user set password = #{password}, updated_datetime = #{dt} where id = #{id}")
    void updatePassword(long id, String password, String dt);

    @Update("update nbs.user set authority = #{authority}, updated_datetime = #{dt} where id = #{id}")
    void updateAuthority(long id, String authority, String dt);

}
