package com.example.nbs.domain.draft;

import com.example.nbs.domain.notice.NoticeEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DraftNoticeRepository {

    @Select("select * from nbs.draft_notice")
    List<NoticeEntity> findAll();

    @Insert("insert into nbs.draft_notice (id,title,contents,request_for_reply,created_user_id,updated_user_id,created_datetime,updated_datetime) value (#{id},#{title},#{contents},#{request_for_reply},#{loginId},#{loginId},#{dt},#{dt})")
    void insert(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    @Select("select * from nbs.draft_notice where id = #{noticeId}")
    NoticeEntity findByDraftId(long noticeId);

    @Update("update nbs.draft_notice set title = #{title},contents = #{contents},request_for_reply = #{request_for_reply},updated_user_id = #{loginId},updated_datetime = #{dt} where id = #{id}")
    void update(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    @Delete("delete from nbs.draft_notice where id = #{id}")
    void delete(long id);

}
