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

    public List<NoticeDetailDto> toNoticeDetailDtoList(List<NoticeEntity> noticeList) {

        NoticeDetailDto noticeDetailDto;
        List<NoticeDetailDto> noticeDetailDtoList = new ArrayList<NoticeDetailDto>();

        if (Global.authority == "ADMIN") {

            for (int i = 0; i < noticeList.size(); i++) {

                noticeDetailDto = new NoticeDetailDto();

                noticeDetailDto.setId(noticeList.get(i).getId());
                noticeDetailDto.setTitle(noticeList.get(i).getTitle());
                noticeDetailDto.setContents(noticeList.get(i).getContents());

                if (0 == noticeList.get(i).getRequest_for_reply()) {

                    noticeDetailDto.setRequest_for_reply("");

                } else {

                    noticeDetailDto.setRequest_for_reply("【あり】");

                }

                noticeDetailDto.setUpdated_datetime(noticeList.get(i).getUpdated_datetime());

                noticeDetailDtoList.add(noticeDetailDto);
            }

        } else {

            for (int i = 0; i < noticeList.size(); i++) {

                noticeDetailDto = new NoticeDetailDto();

                noticeDetailDto.setId(noticeList.get(i).getId());
                noticeDetailDto.setTitle(noticeList.get(i).getTitle());
                noticeDetailDto.setContents(noticeList.get(i).getContents());

                if (0 == noticeList.get(i).getRequest_for_reply()) {

                    noticeDetailDto.setRequest_for_reply("");

                } else {

                    if (0 == attendanceService.existAttendance(noticeList.get(i).getId(), Global.userId)) {
                        noticeDetailDto.setRequest_for_reply("【未送信】");

                    } else {
                        noticeDetailDto.setRequest_for_reply("【送信済】");

                    }

                }
                noticeDetailDto.setUpdated_datetime(noticeList.get(i).getUpdated_datetime());

                noticeDetailDtoList.add(noticeDetailDto);
            }

        }
        return noticeDetailDtoList;
    }

}
