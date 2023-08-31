package com.example.nbs.domain.attendance;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepositoryImpl attendanceRepositoryImpl;

    public List<AttendanceEntity> findAll() {

        return attendanceRepositoryImpl.findAll();

    }

    @Transactional
    public void create(long noticeId, long userId, int attendanceCheck, String dtF2) {

        attendanceRepositoryImpl.insert(noticeId, userId, attendanceCheck, dtF2);

    }

    public AttendanceEntity findById(long id) {

        return attendanceRepositoryImpl.findByNoticeId(id);

    }

    public int existAttendance(long noticeId, long userId) {

        return attendanceRepositoryImpl.count(noticeId, userId);

    }

    public List<ReplyDto> findByNoticeIdReply(long noticeId) {

        return attendanceRepositoryImpl.findByNoticeIdReply(noticeId);

    }

    public void delete(long noticeId) {

        attendanceRepositoryImpl.delete(noticeId);

    }

}
