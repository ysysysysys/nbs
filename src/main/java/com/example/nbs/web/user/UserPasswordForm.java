package com.example.nbs.web.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordForm {

    private long id;

    @NotBlank(message = "パスワードを入力してください。")
    private String password;

    @NotBlank(message = "パスワードを入力してください。")
    @Size(min = 4, max = 12 ,message = "パスワードは4文字以上、12文字以下で入力してください。")
    private String passwordNew;

}
