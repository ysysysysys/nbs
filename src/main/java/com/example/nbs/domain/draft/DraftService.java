package com.example.nbs.domain.draft;


import com.example.nbs.domain.notice.NoticeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DraftService {

    private final DraftNoticeRepositoryImpl draftNoticeRepositoryImpl;

    private final DraftFileRepositoryImpl draftFileRepositoryImpl;

    public List<NoticeEntity> findAll() {

        return draftNoticeRepositoryImpl.findAll();

    }

    @Transactional
    public void save(long id, String title, String contents, List<Path> filename, int request_for_reply, long loginId, String dt) {

        if (draftNoticeRepositoryImpl.findByDraftId(id) == null) {

            // 登録
            draftNoticeRepositoryImpl.insert(id, title, contents, request_for_reply, loginId, dt);

            for (int i = 0; i < filename.size(); i++) {
                draftFileRepositoryImpl.insert(id, filename.get(i).getFileName().toString(), dt);
            }

        } else {

            // 更新
            draftNoticeRepositoryImpl.update(id, title, contents, request_for_reply, loginId, dt);

            draftFileRepositoryImpl.delete(id);

            for (int i = 0; i < filename.size(); i++) {
                draftFileRepositoryImpl.insert(id, filename.get(i).getFileName().toString(), dt);
            }

        }

    }

    public NoticeEntity findById(long id) {

        return draftNoticeRepositoryImpl.findByDraftId(id);

    }

    @Transactional
    public void delete(long id) {

        draftNoticeRepositoryImpl.delete(id);

        draftFileRepositoryImpl.delete(id);

    }

}
