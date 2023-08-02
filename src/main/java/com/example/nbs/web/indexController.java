package com.example.nbs.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

    @GetMapping("/")
    public String index() {

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
