package com.example.nbs.domain.notice;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeRepository {

    List<NoticeEntity> findAll();

    NoticeEntity findByNoticeId(long noticeId);

    void insert(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    void update(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    void delete(long id);

}
