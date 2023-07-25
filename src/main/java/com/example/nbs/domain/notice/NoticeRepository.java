package com.example.nbs.domain.notice;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoticeRepository {

    @Select("select * from nbs.notice")
    List<NoticeEntity> findAllNotice();

    @Insert("insert into nbs.notice (title,contents,request_for_reply,household_id,created_datetime,updated_datetime) value (#{title},#{contents},#{request_for_reply},1,#{dt},#{dt})")
    void insertNotice(String title, String contents, int request_for_reply, String dt);

    @Insert("insert into nbs.file (notice_id,name,created_datetime,updated_datetime) value (#{noticeLastId},#{filename},#{dt},#{dt})")
    void insertFile(int noticeLastId, String filename, String dt);

    @Select("select max(id) from nbs.notice")
    int fineByLastInsertNoticeId();

    @Select("select * from nbs.notice where id = #{noticeId}")
    NoticeEntity findByNoticeId(int noticeId);

}
