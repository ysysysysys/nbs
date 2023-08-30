package com.example.nbs.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepository {

    private final NoticeRepository noticeRepository;

    public List<NoticeEntity> findAll() {

        return noticeRepository.findAll();

    }

    public NoticeEntity findByNoticeId(long noticeId) {

        return noticeRepository.findByNoticeId(noticeId);

    }

    public void insert(long id, String title, String contents, int request_for_reply, long loginId, String dt) {

        noticeRepository.insert(id, title, contents, request_for_reply, loginId, dt);

    }

    public void update(long id, String title, String contents, int request_for_reply, long loginId, String dt) {

        noticeRepository.update(id, title, contents, request_for_reply, loginId, dt);

    }

    public void delete(long id) {

        noticeRepository.delete(id);

    }

}
