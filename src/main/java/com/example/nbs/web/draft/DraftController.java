package com.example.nbs.web.draft;

import com.example.nbs.domain.draft.DraftService;
import com.example.nbs.domain.notice.NoticeEntity;
import com.example.nbs.web.Global;
import com.example.nbs.web.notice.NoticeForm;
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
@RequestMapping("/draft")
@RequiredArgsConstructor
public class DraftController {

    private final DraftService draftService;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    /**
     * 下書き一覧表示
     */
    @GetMapping("/list")
    public String showList(Model model) {

        model.addAttribute("loginId", Global.userId);

        List<NoticeEntity> noticeList = draftService.findAll();
        model.addAttribute("noticeList", noticeList);

        return "draft/list";

    }

    /**
     * お知らせ下書き保存(登録・更新)
     */
    @PostMapping("/save")
    public String create(@Validated NoticeForm form, BindingResult bindingResult, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("files", fileSystemStorageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));

            return "notice/creationForm";

        }

        Long noticeId = (Long) session.getAttribute("noticeId");

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF1 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        if (noticeId == null) {
            noticeId = Long.valueOf(dtF1);
        }


        // ファイル情報取得
        List<Path> filePathInfo = fileSystemStorageService.loadAll().toList();

        // アップロードファイル更新
        fileSystemStorageService.updateFile(String.valueOf(noticeId) + "_draft");

        // DB反映
        draftService.save(noticeId, form.getTitle(), form.getContents(), filePathInfo, form.getRequest_for_reply(), Global.userId, dtF2);

        // セッションクリア
        Enumeration en = session.getAttributeNames();
        String eName;

        while (en.hasMoreElements()) {
            eName = (String) en.nextElement();
            if (!eName.equals("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN") && !eName.equals("SPRING_SECURITY_CONTEXT")) {
                session.removeAttribute(eName);
            }

        }

        return "redirect:/draft/list";

    }

    /**
     * お知らせ詳細表示
     */
    @GetMapping("/{noticeId}")
    public String showDetail(@PathVariable("noticeId") long noticeId, Model model) {

        model.addAttribute("loginId", Global.userId);

        model.addAttribute("notice", draftService.findById(noticeId));

        // 一時フォルダクリア
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();

        // ファイルを一時フォルダにコピー
        fileSystemStorageService.downloadFile(String.valueOf(noticeId) + "_draft");

        model.addAttribute("files", fileSystemStorageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));

        return "draft/detail";

    }

    /**
     * お知らせ編集フォーム表示
     */
    @GetMapping("/editForm/{noticeId}")
    public String showEditForm(@PathVariable("noticeId") long noticeId, Model model, HttpSession session) {

        model.addAttribute("loginId", Global.userId);

        Global.h1 = "お知らせ編集";

        session.setAttribute("noticeId", noticeId);

        NoticeEntity noticeEntity = draftService.findById(noticeId);
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
        fileSystemStorageService.deleteFolder(lastElement + "_draft");

        // DB反映
        draftService.delete(Long.parseLong(lastElement));

        return "redirect:/draft/list";

    }

}
