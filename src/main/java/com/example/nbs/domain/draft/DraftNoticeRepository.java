package com.example.nbs.domain.draft;

import com.example.nbs.domain.notice.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DraftNoticeRepository {

    List<NoticeEntity> findAll();

    NoticeEntity findByDraftId(long noticeId);

    void insert(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    void update(long id, String title, String contents, int request_for_reply, long loginId, String dt);

    void delete(long id);

}
