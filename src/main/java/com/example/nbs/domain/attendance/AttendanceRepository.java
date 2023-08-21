package com.example.nbs.domain.attendance;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("select u.fullname,(case a.attendance_check when 1 then '出席' else '欠席' end) as reply from nbs.user u left join nbs.attendance a on u.id = a.user_id and a.notice_id = #{noticeId}")
    List<ReplyDto> findByNoticeIdReply(long noticeId);

}
