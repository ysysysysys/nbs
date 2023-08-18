package com.example.nbs.web.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserBasicInfoForm {

    private long id;

    @NotBlank(message = "名前を入力してください。")
    @Size(max = 50 ,message = "名前は50文字以下で入力してください。")
    private String fullname;

    @NotBlank(message = "住所を入力してください。")
    @Size(max = 200 ,message = "住所は200文字以下で入力してください。")
    private String address;

}
