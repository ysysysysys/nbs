package com.example.nbs.domain.draft;

import com.example.nbs.domain.notice.NoticeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DraftNoticeRepositoryImpl implements DraftNoticeRepository {

    private final DraftNoticeRepository draftNoticeRepository;

    public List<NoticeEntity> findAll() {

        return draftNoticeRepository.findAll();

    }

    public NoticeEntity findByDraftId(long noticeId) {

        return draftNoticeRepository.findByDraftId(noticeId);

    }


    public void insert(long id, String title, String contents, int request_for_reply, long loginId, String dt) {

        draftNoticeRepository.insert(id, title, contents, request_for_reply, loginId, dt);

    }

    public void update(long id, String title, String contents, int request_for_reply, long loginId, String dt) {

        draftNoticeRepository.update(id, title, contents, request_for_reply, loginId, dt);

    }

    public void delete(long id) {

        draftNoticeRepository.delete(id);

    }

}
