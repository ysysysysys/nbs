package com.example.nbs.domain.draft;

import org.apache.ibatis.annotations.*;

@Mapper
public interface DraftFileRepository {

    @Insert("insert into nbs.draft_file (notice_id,name,created_datetime,updated_datetime) value (#{noticeLastId},#{filename},#{dt},#{dt})")
    void insert(long noticeLastId, String filename, String dt);

    @Delete("delete from nbs.draft_file where notice_id = #{id}")
    void delete(long id);

}
