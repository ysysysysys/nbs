package com.example.nbs.domain.draft;

import com.example.nbs.domain.notice.NoticeEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DraftNoticeRepository {

    @Select("select * from nbs.draft_notice")
    List<NoticeEntity> findAll();

    @Insert("insert into nbs.draft_notice (id,title,contents,request_for_reply,household_id,created_datetime,updated_datetime) value (#{id},#{title},#{contents},#{request_for_reply},1,#{dt},#{dt})")
    void insert(long id, String title, String contents, int request_for_reply, String dt);

    @Select("select * from nbs.draft_notice where id = #{noticeId}")
    NoticeEntity findByDraftId(long noticeId);

    @Update("update nbs.draft_notice set title = #{title},contents = #{contents},request_for_reply = #{request_for_reply},updated_datetime = #{dt} where id = #{id}")
    void update(long id, String title, String contents, int request_for_reply, String dt);

    @Delete("delete from nbs.draft_notice where id = #{id}")
    void delete(long id);

}
