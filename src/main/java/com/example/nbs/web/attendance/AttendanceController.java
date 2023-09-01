package com.example.nbs.web.attendance;

import com.example.nbs.domain.attendance.AttendanceService;
import com.example.nbs.domain.auth.LoginUser;
import com.example.nbs.domain.auth.UserService;
import com.example.nbs.domain.notice.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@SessionAttributes(types = LoginUser.class)
public class AttendanceController {

    private final AttendanceService attendanceService;

    private final NoticeService noticeService;

    private final UserService userService;

    @ModelAttribute("loginUser")
    public LoginUser loginUser(HttpSession session) {

        // ログインユーザー情報を保持しておく
        LoginUser loginUser = new LoginUser();
        LoginUser userInfo = (LoginUser) session.getAttribute("loginUser");
        loginUser.setLoginId(userInfo.getLoginId());
        loginUser.setLoginUsername(userInfo.getLoginUsername());
        loginUser.setLoginAuthority(userInfo.getLoginAuthority());

        return loginUser;
    }

    /**
     * 出欠可否送信(登録)
     */
    @PostMapping("/notice/attendance/{noticeId}")
    public String update(@Validated AttendanceForm form, @PathVariable("noticeId") long noticeId, Model model) {

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // DB反映
        LoginUser userInfo = (LoginUser) model.getAttribute("loginUser");
        attendanceService.create(noticeId, userInfo.getLoginId(), form.getAttendance_check(), dtF2);

        return "redirect:/notice/{noticeId}";

    }

    /**
     * 返答確認一覧
     */
    @GetMapping("/attendance/{noticeId}")
    public String showList(@PathVariable("noticeId") long noticeId, @ModelAttribute ReplyForm replyForm, Model model) {

//        model.addAttribute("loginId", Global.userId);

        replyForm.setNoticeTitle(noticeService.findById(noticeId).getTitle());
        replyForm.setNumberOfAttendance(attendanceService.findByNoticeIdReply(noticeId).stream().filter(i -> i.getReply().equals("出席")).count());
        replyForm.setNumberOfUsers(userService.findAll().stream().count());
        replyForm.setRate((double) replyForm.getNumberOfAttendance() / (double) replyForm.getNumberOfUsers() * 100);
        model.addAttribute("replyForm", replyForm);

        model.addAttribute("attendanceList", attendanceService.findByNoticeIdReply(noticeId));

        return "attendance/list";

    }

}
