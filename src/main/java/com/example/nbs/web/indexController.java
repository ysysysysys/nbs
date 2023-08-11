package com.example.nbs.web;

import com.example.nbs.domain.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class indexController {

    private final UserService userService;

    @GetMapping("/")
    public String index() {

        // ログインユーザー情報を保持しておく
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Global.authority = principal.getAuthorities().stream().findFirst().get().toString();

        Global.userId = userService.toUserId(principal.getUsername());

        return "index";

    }

    @GetMapping("/login")
    public String showLoginForm() {

        return "login";

    }

    @GetMapping("/logout")
    public String showLogoutForm() {

        return "/logout";

    }
}
