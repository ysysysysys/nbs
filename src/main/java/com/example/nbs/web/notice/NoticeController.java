package com.example.nbs.web.notice;

import com.example.nbs.domain.attendance.AttendanceService;
import com.example.nbs.domain.notice.NoticeDetailDto;
import com.example.nbs.domain.notice.NoticeEntity;
import com.example.nbs.domain.notice.NoticeService;
import com.example.nbs.web.Global;
import com.example.nbs.web.attendance.AttendanceForm;
import com.example.nbs.web.uploadingfiles.FileUploadController;
import com.example.nbs.web.uploadingfiles.storage.FileSystemStorageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    private final AttendanceService attendanceService;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    /**
     * お知らせ一覧表示
     */
    @GetMapping
    public String showList(Model model) {

        List<NoticeEntity> noticeList = noticeService.findAll();

        List<NoticeDetailDto> noticeDetailDtoList = noticeService.toNoticeDetailDtoList(noticeList);

        model.addAttribute("noticeList", noticeDetailDtoList);

        return "notice/list";

    }

    /**
     * お知らせ作成フォーム表示
     */
    @GetMapping("/creationForm")
    public String showCreationForm(Model model) {

        Global.h1 = "お知らせ作成";

        model.addAttribute("noticeForm", new NoticeForm());

        // 一時フォルダクリア
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();

        return "notice/creationForm";

    }

    /**
     * お知らせ公開(登録)
     */
    @PostMapping("/creation")
    public String create(@Validated NoticeForm form, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("files", fileSystemStorageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));

            return "notice/creationForm";

        }

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF1 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // ファイル情報取得
        List<Path> filePathInfo = fileSystemStorageService.loadAll().toList();

        if (filePathInfo.size() > 0) {

            // ファイル保存
            fileSystemStorageService.uploadFile(dtF1);

        }

        // DB反映
        noticeService.create(Long.parseLong(dtF1), form.getTitle(), form.getContents(), filePathInfo, form.getRequest_for_reply(), dtF2);

        return "redirect:/notice";

    }

    /**
     * お知らせ詳細表示
     */
    @GetMapping("/{noticeId}")
    public String showDetail(@PathVariable("noticeId") long noticeId, Model model) {

        // お知らせ詳細取得してセット
        model.addAttribute("notice", noticeService.findById(noticeId));

        // 一時フォルダクリア
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();

        // ファイルを一時フォルダにコピー
        fileSystemStorageService.downloadFile(String.valueOf(noticeId));

        model.addAttribute("files", fileSystemStorageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));


        // 出席確認送信状況取得してセット
        AttendanceForm attendanceForm = new AttendanceForm();
        if (0 == attendanceService.existAttendance(noticeId, Global.userId)) {

            attendanceForm.setAttendance_check(0);
            attendanceForm.setSend_flg("NOTYET");

        } else {

            attendanceForm.setAttendance_check(1);
            attendanceForm.setSend_flg("DONE");

        }

        model.addAttribute("attendanceForm", attendanceForm);

        return "notice/detail";

    }

    /**
     * お知らせ編集フォーム表示
     */
    @GetMapping("/editForm/{noticeId}")
    public String showEditForm(@PathVariable("noticeId") long noticeId, Model model, HttpSession session) {

        Global.h1 = "お知らせ編集";

        session.setAttribute("noticeId", noticeId);

        NoticeEntity noticeEntity = noticeService.findById(noticeId);
        model.addAttribute("notice_Id", noticeId);

        // NoticeFormの新しいインスタンスを作成し、取得したデータを設定
        NoticeForm noticeForm = new NoticeForm();
        noticeForm.setTitle(noticeEntity.getTitle());
        noticeForm.setContents(noticeEntity.getContents());
        noticeForm.setRequest_for_reply(noticeEntity.getRequest_for_reply());

        // NoticeFormをモデルに追加
        model.addAttribute("noticeForm", noticeForm);

        // ファイル情報をモデルに追加
        model.addAttribute("files", fileSystemStorageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));

        return "notice/editForm";

    }

    /**
     * お知らせ公開(更新)
     */
    @PostMapping("/edit")
    public String update(@Validated NoticeForm form, BindingResult bindingResult, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {

            // URLを取得
            String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
            String[] elements = uri.split("/");
            String lastElement = elements[elements.length - 1];

            model.addAttribute("notice_Id", lastElement);

            model.addAttribute("files", fileSystemStorageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));

            return "notice/editForm";

        }

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // ファイル情報取得
        List<Path> filePathInfo = fileSystemStorageService.loadAll().toList();

        Long noticeId = (Long) session.getAttribute("noticeId");

        // アップロードファイル更新
        fileSystemStorageService.updateFile(String.valueOf(noticeId));

        // DB反映
        noticeService.update(noticeId, form.getTitle(), form.getContents(), filePathInfo, form.getRequest_for_reply(), dtF2);

        // セッションクリア
        Enumeration en = session.getAttributeNames();
        String eName;

        while (en.hasMoreElements()) {
            eName = (String) en.nextElement();
            if (!eName.equals("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN") && !eName.equals("SPRING_SECURITY_CONTEXT")) {
                session.removeAttribute(eName);
            }

        }

        return "redirect:/notice";

    }

    /**
     * お知らせ削除(物理削除)
     */
    @PostMapping("/delete/{noticeId}")
    public String delete() {

        // URLを取得
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String[] elements = uri.split("/");
        String lastElement = elements[elements.length - 1];

        // ファイル情報取得
        List<Path> filePathInfo = fileSystemStorageService.loadAll().toList();

        // アップロードファイルファイル削除
        fileSystemStorageService.deleteFolder(lastElement);

        // DB反映
        noticeService.delete(Long.parseLong(lastElement));

        return "redirect:/notice";

    }

}
