package com.example.nbs.web.user;

import com.example.nbs.domain.auth.UserEntity;
import com.example.nbs.domain.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String showList(Model model) {

        model.addAttribute("userList", userService.findAll());

        return "user/list";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute UserForm form) {

        // 権限のデフォルト設定(user/creationForm.htmlでラジオボタンのcheckedが効かないため、コントローラー側から初期値を渡す。)
        form.setAuthority(UserEntity.Authority.USER.name());

        return "user/creationForm";

    }

    @PostMapping
    public String create(@Validated UserForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return showCreationForm(form);
        }

        userService.create(form.getUsername(), form.getPassword(), form.getAuthority());

        return "redirect:/user";

    }

}
