package com.example.nbs.web.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUsernameForm {

    private long id;

    @NotBlank(message = "ユーザー名を入力してください。")
    @Size(min = 1, max = 20 ,message = "ユーザー名は1文字以上、20文字以下で入力してください。")
    private String username;

}
