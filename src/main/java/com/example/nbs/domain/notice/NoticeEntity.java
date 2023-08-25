package com.example.nbs.domain.notice;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NoticeEntity {

    private long id;
    private String title;
    private String contents;
    private int request_for_reply;
    private long created_user_id;
    private long updated_user_id;
    private String created_datetime;
    private String updated_datetime;

}
