package com.example.nbs.domain.notice;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileRepository {

    @Insert("insert into nbs.file (notice_id,name,created_datetime,updated_datetime) value (#{noticeLastId},#{filename},#{dt},#{dt})")
    void insert(long noticeLastId, String filename, String dt);

    @Delete("delete from nbs.file where notice_id = #{id}")
    void delete(long id);

}
