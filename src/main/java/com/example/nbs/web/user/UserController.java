package com.example.nbs.web.user;

import com.example.nbs.domain.auth.UserEntity;
import com.example.nbs.domain.auth.UserService;
import com.example.nbs.web.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    /**
     * ユーザー一覧表示
     */
    @GetMapping
    public String showList(Model model) {

        model.addAttribute("userList", userService.findAll());

        return "user/list";
    }

    /**
     * ユーザー作成フォーム表示
     */
    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute UserForm form) {

        // 権限のデフォルト設定(user/creationForm.htmlでラジオボタンのcheckedが効かないため、コントローラー側から初期値を渡す。)
        form.setAuthority(UserEntity.Authority.USER.name());

        return "user/creationForm";

    }

    /**
     * ユーザー登録
     */
    @PostMapping
    public String create(@Validated UserForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return showCreationForm(form);
        }

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        userService.create(form.getUsername(), form.getPassword(), form.getAuthority(), form.getFullname(), form.getAddress(), dtF2);

        return "redirect:/user";

    }

    /**
     * ユーザー登録情報表示
     */
    @GetMapping("/{userId}")
    public String showDetail(@PathVariable("userId") long userId, Model model) {

        // ユーザー登録情報取得してセット
        model.addAttribute("user", userService.findById(userId));

        // ログインIDをセット
        model.addAttribute("ownId", Global.userId);

        return "user/detail";

    }

    /**
     * 権限変更フォーム表示
     */
    @GetMapping("/changeAuthorityForm/{userId}")
    public String showChangeAuthorityForm(@PathVariable("userId") long userId, Model model) {

        if (!model.containsAttribute("userAuthorityForm")) {

            UserAuthorityForm userAuthorityForm = new UserAuthorityForm();
            userAuthorityForm.setId(userId);
            userAuthorityForm.setAuthority(String.valueOf(userService.findById(userId).getAuthority()));
            model.addAttribute("userAuthorityForm", userAuthorityForm);

        }

        return "user/changeAuthorityForm";

    }

    /**
     * 基本情報変更フォーム表示
     */
    @GetMapping("/changeBasicInfoForm/{userId}")
    public String showChangeBasicInfoForm(@PathVariable("userId") long userId, Model model) {

        if (!model.containsAttribute("userBasicInfoForm")) {

            UserBasicInfoForm userBasicInfoForm = new UserBasicInfoForm();
            userBasicInfoForm.setId(userId);
            userBasicInfoForm.setFullname(userService.findById(userId).getFullname());
            userBasicInfoForm.setAddress(userService.findById(userId).getAddress());
            model.addAttribute("userBasicInfoForm", userBasicInfoForm);

        }

        return "user/changeBasicInfoForm";

    }

    /**
     * ユーザー名変更フォーム表示
     */
    @GetMapping("/changeUsernameForm/{userId}")
    public String showChangeUsernameForm(@PathVariable("userId") long userId, Model model) {

        if (!model.containsAttribute("userUsernameForm")) {

            UserUsernameForm userUsernameForm = new UserUsernameForm();
            userUsernameForm.setId(userId);
            model.addAttribute("userUsernameForm", userUsernameForm);

        }

        return "user/changeUsernameForm";

    }

    /**
     * パスワード変更フォーム表示
     */
    @GetMapping("/changePasswordForm/{userId}")
    public String showChangePasswordForm(@PathVariable("userId") long userId, Model model) {

        if (!model.containsAttribute("userPasswordForm")) {

            UserPasswordForm userPasswordForm = new UserPasswordForm();
            userPasswordForm.setId(userId);
            model.addAttribute("userPasswordForm", userPasswordForm);

        }

        return "user/changePasswordForm";

    }

    /**
     * 権限更新
     */
    @PostMapping("/changeAuthority")
    public String changeAuthority(@Validated @ModelAttribute UserAuthorityForm userAuthorityForm, UriComponentsBuilder builder) {

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // DB反映
        userService.updateAuthority(userAuthorityForm.getId(), userAuthorityForm.getAuthority(), dtF2);

        // リダイレクト先を指定
        URI location = builder.path("/user/" + userAuthorityForm.getId()).build().toUri();

        return "redirect:" + location.toString();

    }

    /**
     * 基本情報更新
     */
    @PostMapping("/changeBasicInfo")
    public String changeBasicInfo(@Validated @ModelAttribute UserBasicInfoForm userBasicInfoForm, BindingResult bindingResult, Model model, UriComponentsBuilder builder) {

        if (bindingResult.hasErrors()) {

            return showChangeBasicInfoForm(userBasicInfoForm.getId(), model);

        }

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // DB反映
        userService.updateBasicInfo(userBasicInfoForm.getId(), userBasicInfoForm.getFullname(), userBasicInfoForm.getAddress(), dtF2);

        // リダイレクト先を指定
        URI location = builder.path("/user/" + userBasicInfoForm.getId()).build().toUri();

        return "redirect:" + location.toString();

    }

    /**
     * ユーザー名更新
     */
    @PostMapping("/changeUsername")
    public String changeUsername(@Validated @ModelAttribute UserUsernameForm userUsernameForm, BindingResult bindingResult, Model model, UriComponentsBuilder builder) {

        if (bindingResult.hasErrors()) {

            return showChangeUsernameForm(userUsernameForm.getId(), model);

        }

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // DB反映
        userService.updateUsername(userUsernameForm.getId(), userUsernameForm.getUsername(), dtF2);

        // リダイレクト先を指定
        URI location = builder.path("/user/" + userUsernameForm.getId()).build().toUri();

        return "redirect:" + location.toString();

    }

    /**
     * パスワード更新
     */
    @PostMapping("/changePassword")
    public String changePassword(@Validated @ModelAttribute UserPasswordForm userPasswordForm, BindingResult bindingResult, Model model, UriComponentsBuilder builder) {

        if (bindingResult.hasErrors()) {

            return showChangePasswordForm(userPasswordForm.getId(), model);

        }

        // 入力したパスワードとDBのハッシュ化されているパスワードの照合
        String rawPass = userPasswordForm.getPassword();
        String dbPass = userService.findById(userPasswordForm.getId()).getPassword();

        if (!passwordEncoder.matches(rawPass, dbPass)) {

            bindingResult.rejectValue("password", "validation.data-incorrect");

            return showChangePasswordForm(userPasswordForm.getId(), model);

        }

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // DB反映
        userService.updatePassword(userPasswordForm.getId(), userPasswordForm.getPasswordNew(), dtF2);

        // リダイレクト先を指定
        URI location = builder.path("/user/" + userPasswordForm.getId()).build().toUri();

        return "redirect:" + location.toString();

    }

    /**
     * お知らせ削除(物理削除)
     */
    @PostMapping("/delete/{userId}")
    public String delete(@PathVariable("userId") long userId) {

        // DB反映
        userService.delete(userId);

        return "redirect:/user";

    }

}
