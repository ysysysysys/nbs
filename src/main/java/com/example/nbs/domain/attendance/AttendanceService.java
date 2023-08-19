package com.example.nbs.domain.attendance;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<AttendanceEntity> findAll() {

        return attendanceRepository.findAll();

    }

    @Transactional
    public void create(long noticeId, long userId, int attendanceCheck, String dtF2) {

        attendanceRepository.insert(noticeId, userId, attendanceCheck, dtF2);

    }

    public AttendanceEntity findById(long id) {

        return attendanceRepository.findByNoticeId(id);

    }

    public int existAttendance(long noticeId, long userId) {

        return attendanceRepository.count(noticeId, userId);

    }

    public List<ReplyDto> findByNoticeIdReply(long noticeId) {

        return attendanceRepository.findByNoticeIdReply(noticeId);

    }

}
