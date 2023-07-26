package com.example.nbs.web.notice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


//  <memo>フォームクラスには画面の入力項目に対応したフィールドを待たせていく。
@Data
public class NoticeForm {

    @NotBlank
    @Size(max = 50)
    private String title;

    @Size(max = 1000)
    private String contents;

    private int request_for_reply;

}
