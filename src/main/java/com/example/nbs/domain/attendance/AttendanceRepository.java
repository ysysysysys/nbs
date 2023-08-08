package com.example.nbs.domain.attendance;

import com.example.nbs.domain.notice.NoticeEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AttendanceRepository {

    @Select("select * from nbs.attendance")
    List<AttendanceEntity> findAll();

    @Insert("insert into nbs.attendance (notice_id,user_id,attendance_check,created_datetime,updated_datetime) value (#{noticeId},#{userId},#{attendanceCheck},#{dtF2},#{dtF2})")
    void insert(long noticeId, long userId, int attendanceCheck, String dtF2);

    @Select("select * from nbs.attendance where notice_id = #{noticeId}")
    AttendanceEntity findByNoticeId(long noticeId);

    @Select("select count(*) from nbs.attendance where notice_id = #{noticeId} and user_id = #{userId}")
    int count(long noticeId, long userId);

}
