package com.example.nbs.domain.attendance;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceRepository attendanceRepository;

    public List<AttendanceEntity> findAll() {

        return attendanceRepository.findAll();

    }

    public AttendanceEntity findByNoticeId(long noticeId) {

        return attendanceRepository.findByNoticeId(noticeId);
    }

    public int count(@Param("noticeId") long noticeId, @Param("userId") long userId) {

        return attendanceRepository.count(noticeId, userId);
    }

    public List<ReplyDto> findByNoticeIdReply(long noticeId) {

        return attendanceRepository.findByNoticeIdReply(noticeId);
    }

    public void insert(long noticeId, long userId, int attendanceCheck, String dt) {

        attendanceRepository.insert(noticeId, userId, attendanceCheck, dt);
    }

    public void delete(long noticeId) {

        attendanceRepository.delete(noticeId);

    }

}
