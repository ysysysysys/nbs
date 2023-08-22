package com.example.nbs.domain.notice;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NoticeListForUserDto {

    private long id;
    private String title;
    private String contents;
    private String request_for_reply;
    private String updated_datetime;

}
