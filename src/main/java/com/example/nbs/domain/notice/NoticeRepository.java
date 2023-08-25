package com.example.nbs.domain.notice;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeRepository {

    @Select("select * from nbs.notice")
    List<NoticeEntity> findAll();

    @Select("select * from nbs.notice where id = #{noticeId}")
    NoticeEntity findByNoticeId(long noticeId);

    @Insert("insert into nbs.notice (id,title,contents,request_for_reply,created_user_id,updated_user_id,created_datetime,updated_datetime) value (#{id},#{title},#{contents},#{request_for_reply},#{loginId},#{loginId},#{dt},#{dt})")
    void insert(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    @Update("update nbs.notice set title = #{title},contents = #{contents},request_for_reply = #{request_for_reply},updated_user_id = #{loginId},updated_datetime = #{dt} where id = #{id}")
    void update(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    @Delete("delete from nbs.notice where id = #{id}")
    void delete(long id);

}
