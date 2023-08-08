package com.example.nbs.domain.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceEntity {

    private long notice_id;
    private long user_id;
    private int attendance_check;
    private String created_datetime;
    private String updated_datetime;

}
