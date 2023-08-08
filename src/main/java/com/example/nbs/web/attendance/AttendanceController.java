package com.example.nbs.web.attendance;

import com.example.nbs.domain.attendance.AttendanceService;
import com.example.nbs.web.Global;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * 出欠可否送信(登録)
     */
    @PostMapping("/notice/attendance/{noticeId}")
    public String update(@Validated AttendanceForm form, @PathVariable("noticeId") long noticeId, HttpSession session) {

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // DB反映
        attendanceService.create(noticeId, Global.userId, form.getAttendance_check(), dtF2);

        return "redirect:/notice/{noticeId}";

    }

}
