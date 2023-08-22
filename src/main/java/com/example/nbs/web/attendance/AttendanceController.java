package com.example.nbs.web.attendance;

import com.example.nbs.domain.attendance.AttendanceService;
import com.example.nbs.domain.auth.UserService;
import com.example.nbs.domain.notice.NoticeService;
import com.example.nbs.web.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    private final NoticeService noticeService;

    private final UserService userService;

    /**
     * 出欠可否送信(登録)
     */
    @PostMapping("/notice/attendance/{noticeId}")
    public String update(@Validated AttendanceForm form, @PathVariable("noticeId") long noticeId) {

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // DB反映
        attendanceService.create(noticeId, Global.userId, form.getAttendance_check(), dtF2);

        return "redirect:/notice/{noticeId}";

    }

    /**
     * 返答確認一覧
     */
    @GetMapping("/attendance/{noticeId}")
    public String showList(@PathVariable("noticeId") long noticeId, @ModelAttribute ReplyForm replyForm, Model model) {

        model.addAttribute("loginId", Global.userId);

        replyForm.setNoticeTitle(noticeService.findById(noticeId).getTitle());
        replyForm.setNumberOfAttendance(attendanceService.findByNoticeIdReply(noticeId).stream().filter(i -> i.getReply().equals("出席")).count());
        replyForm.setNumberOfUsers(userService.findAll().stream().count());
        replyForm.setRate((double) replyForm.getNumberOfAttendance() / (double) replyForm.getNumberOfUsers() * 100);
        model.addAttribute("replyForm", replyForm);

        model.addAttribute("attendanceList", attendanceService.findByNoticeIdReply(noticeId));

        return "attendance/list";

    }

}
