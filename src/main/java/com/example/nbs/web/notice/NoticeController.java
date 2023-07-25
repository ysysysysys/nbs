package com.example.nbs.web.notice;

import com.example.nbs.domain.notice.NoticeEntity;
import com.example.nbs.domain.notice.NoticeService;
import com.example.nbs.web.Global;
import com.example.nbs.web.uploadingfiles.FileUploadController;
import com.example.nbs.web.uploadingfiles.storage.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    @GetMapping
    public String showList(Model model) {

        List<NoticeEntity> noticeList = noticeService.findAll();
        model.addAttribute("noticeList", noticeList);
        return "notice/list";

    }

    @GetMapping("/creationForm")
    public String showCreationForm(Model model) {

        model.addAttribute("noticeForm", new NoticeForm());

        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();

        return "notice/creationForm";

    }

    @PostMapping
    public String create(@Validated NoticeForm form, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("files", fileSystemStorageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));

            return "notice/creationForm";

        }

        // ファイル情報取得
        List<Path> filePathInfo = fileSystemStorageService.loadAll().toList();
        int loadfilecount = filePathInfo.size();

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dt = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss"));
        form.setCreated_datetime(dt);
        form.setUpdated_datetime(dt);

        noticeService.create(form.getTitle(), form.getContents(), filePathInfo, form.getRequest_for_reply(), dt);

        // ファイル保存
        fileSystemStorageService.fileUpload();

        return "redirect:/notice";

    }

    @GetMapping("/{noticeId}")
    public String showDetail(@PathVariable("noticeId") int noticeId, Model model) {

        model.addAttribute("notice", noticeService.findById(noticeId));

        // <memo> ここでは不要？　画面遷移先のIDを持つ方法はほかにある？
        // URLを取得
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();

        Global.notice_id = Integer.parseInt(uri.substring(uri.length() - 1));
        // <memo>

        return "notice/detail";

    }

}
