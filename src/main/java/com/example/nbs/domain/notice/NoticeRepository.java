package com.example.nbs.domain.notice;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeRepository {

    @Select("select * from nbs.notice")
    List<NoticeEntity> findAll();

    @Insert("insert into nbs.notice (id,title,contents,request_for_reply,user_id,created_datetime,updated_datetime) value (#{id},#{title},#{contents},#{request_for_reply},1,#{dt},#{dt})")
    void insert(long id, String title, String contents, int request_for_reply, String dt);

    @Select("select max(id) from nbs.notice")
    int findByLastInsertNoticeId();

    @Select("select * from nbs.notice where id = #{noticeId}")
    NoticeEntity findByNoticeId(long noticeId);

    @Update("update nbs.notice set title = #{title},contents = #{contents},request_for_reply = #{request_for_reply},updated_datetime = #{dt} where id = #{id}")
    void update(long id, String title, String contents, int request_for_reply, String dt);

    @Delete("delete from nbs.notice where id = #{id}")
    void delete(long id);

}
