package com.example.nbs.domain.attendance;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AttendanceRepository {

    List<AttendanceEntity> findAll();

    AttendanceEntity findByNoticeId(long noticeId);

    int count(@Param("noticeId") long noticeId, @Param("userId") long userId);

    List<ReplyDto> findByNoticeIdReply(long noticeId);

    void insert(long noticeId, long userId, int attendanceCheck, String dt);

}
