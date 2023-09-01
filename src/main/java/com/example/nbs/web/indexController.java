package com.example.nbs.web;

import com.example.nbs.domain.auth.LoginUser;
import com.example.nbs.domain.auth.UserEntity;
import com.example.nbs.domain.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequiredArgsConstructor
@SessionAttributes(types = LoginUser.class)
public class indexController {

    private final UserService userService;

    // セッションに保存するオブジェクトの本体は、メソッドに@ModelAttributeアノテーションを付けて作成する
    @ModelAttribute("loginUser")
    public LoginUser loginUser() {

        return new LoginUser();

    }

    @GetMapping("/")
    public String index(@ModelAttribute LoginUser loginUser, Model model) {

        // ログインユーザー情報を保持しておく
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loginUser.setLoginId(userService.toUserId(principal.getUsername()));
        loginUser.setLoginUsername(principal.getUsername());
        loginUser.setLoginAuthority(UserEntity.Authority.valueOf(principal.getAuthorities().stream().findFirst().get().toString()));

        model.addAttribute("loginUser", loginUser);

        return "index";

    }

    @GetMapping("/login")
    public String showLoginForm() {

        return "login";

    }

    @GetMapping("/logout")
    public String showLogoutForm() {

        return "login";

    }

}
