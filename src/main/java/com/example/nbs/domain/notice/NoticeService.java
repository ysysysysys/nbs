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

    public List<NoticeEntity> findAll() {

        return noticeRepository.findAllNotice();

    }

    @Transactional
    public void create(long id, String title, String contents, List<Path> filename, int request_for_reply, String dt) {

        noticeRepository.insertNotice(id, title, contents, request_for_reply, dt);

        for (int i = 0; i < filename.size(); i++) {
            noticeRepository.insertFile(id, filename.get(i).getFileName().toString(), dt);
        }

    }

    public long fineByLastInsertId() {

        return noticeRepository.fineByLastInsertNoticeId();

    }

    public NoticeEntity findById(long noticeId) {

        return noticeRepository.findByNoticeId(noticeId);

    }

}
