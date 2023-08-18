package com.example.nbs.web.user;

import com.example.nbs.web.validation.UniqueUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserForm {

    @NotBlank(message = "名前を入力してください。")
    @Size(max = 50 ,message = "名前は50文字以下で入力してください。")
    private String fullname;

    @NotBlank(message = "住所を入力してください。")
    @Size(max = 200 ,message = "住所は200文字以下で入力してください。")
    private  String address;

    @NotBlank(message = "ユーザー名を入力してください。")
    @Size(min = 1, max = 20 ,message = "ユーザー名は1文字以上、20文字以下で入力してください。")
    @UniqueUsername
    private String username;

    @NotBlank(message = "パスワードを入力してください。")
    @Size(min = 4, max = 12 ,message = "パスワードは4文字以上、12文字以下で入力してください。")
    private String password;

    private String authority;

}
