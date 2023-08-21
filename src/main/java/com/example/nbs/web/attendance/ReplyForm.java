package com.example.nbs.web.attendance;

import lombok.Data;

@Data
public class ReplyForm {

    private String noticeTitle;

    private long numberOfAttendance;

    private long numberOfUsers;

    private double rate;

}
