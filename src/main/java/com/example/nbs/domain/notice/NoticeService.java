package com.example.nbs.domain.notice;


import com.example.nbs.domain.attendance.AttendanceService;
import com.example.nbs.web.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final FileRepository fileRepository;

    private final AttendanceService attendanceService;

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

    public List<NoticeListForUserDto> toNoticeListForUserDto(List<NoticeEntity> noticeList) {

        NoticeListForUserDto noticeListForUserDto;
        List<NoticeListForUserDto> noticeListForUserDtoList = new ArrayList<NoticeListForUserDto>();

        for (int i = 0; i < noticeList.size(); i++) {

            noticeListForUserDto = new NoticeListForUserDto();

            noticeListForUserDto.setId(noticeList.get(i).getId());
            noticeListForUserDto.setTitle(noticeList.get(i).getTitle());
            noticeListForUserDto.setContents(noticeList.get(i).getContents());

            if (0 == noticeList.get(i).getRequest_for_reply()) {

                noticeListForUserDto.setRequest_for_reply("");

            } else {

                if (0 == attendanceService.existAttendance(noticeList.get(i).getId(), Global.userId)) {
                    noticeListForUserDto.setRequest_for_reply("【未送信】");

                } else {
                    noticeListForUserDto.setRequest_for_reply("【送信済】");

                }

            }
            noticeListForUserDto.setUpdated_datetime(noticeList.get(i).getUpdated_datetime());

            noticeListForUserDtoList.add(noticeListForUserDto);
        }

        return noticeListForUserDtoList;

    }

}
