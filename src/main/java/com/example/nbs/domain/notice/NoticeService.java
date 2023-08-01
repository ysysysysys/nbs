package com.example.nbs.domain.notice;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final FileRepository fileRepository;

    public List<NoticeEntity> findAll() {

        return noticeRepository.findAll();

    }

    @Transactional
    public void create(long id, String title, String contents, List<Path> filename, int request_for_reply, String dt) {

        noticeRepository.insert(id, title, contents, request_for_reply, dt);

        for (int i = 0; i < filename.size(); i++) {
            fileRepository.insert(id, filename.get(i).getFileName().toString(), dt);
        }

    }

    public long findByLastInsertId() {

        return noticeRepository.findByLastInsertNoticeId();

    }

    public NoticeEntity findById(long id) {

        return noticeRepository.findByNoticeId(id);

    }

    @Transactional
    public void update(long id, String title, String contents, List<Path> filename, int request_for_reply, String dt) {

        if (noticeRepository.findByNoticeId(id) == null) {

            // 登録
            noticeRepository.insert(id, title, contents, request_for_reply, dt);

            for (int i = 0; i < filename.size(); i++) {
                fileRepository.insert(id, filename.get(i).getFileName().toString(), dt);
            }

        } else {

            // 更新
            noticeRepository.update(id, title, contents, request_for_reply, dt);

            fileRepository.delete(id);

            for (int i = 0; i < filename.size(); i++) {
                fileRepository.insert(id, filename.get(i).getFileName().toString(), dt);
            }

        }

    }

    @Transactional
    public void delete(long id) {

        noticeRepository.delete(id);

        fileRepository.delete(id);

    }

}
