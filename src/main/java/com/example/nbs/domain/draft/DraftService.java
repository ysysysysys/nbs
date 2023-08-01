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

    private final DraftNoticeRepository draftNoticeRepository;

    private final DraftFileRepository draftFileRepository;

    public List<NoticeEntity> findAll() {

        return draftNoticeRepository.findAll();

    }

    @Transactional
    public void save(long id, String title, String contents, List<Path> filename, int request_for_reply, String dt) {

        if (draftNoticeRepository.findByDraftId(id) == null) {

            // 登録
            draftNoticeRepository.insert(id, title, contents, request_for_reply, dt);

            for (int i = 0; i < filename.size(); i++) {
                draftFileRepository.insert(id, filename.get(i).getFileName().toString(), dt);
            }

        } else {

            // 更新
            draftNoticeRepository.update(id, title, contents, request_for_reply, dt);

            draftFileRepository.delete(id);

            for (int i = 0; i < filename.size(); i++) {
                draftFileRepository.insert(id, filename.get(i).getFileName().toString(), dt);
            }

        }

    }

    public NoticeEntity findById(long id) {

        return draftNoticeRepository.findByDraftId(id);

    }

    @Transactional
    public void delete(long id) {

        draftNoticeRepository.delete(id);

        draftFileRepository.delete(id);

    }

}
