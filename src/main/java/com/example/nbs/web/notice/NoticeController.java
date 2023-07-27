package com.example.nbs.web.notice;

import com.example.nbs.domain.notice.NoticeEntity;
import com.example.nbs.domain.notice.NoticeService;
import com.example.nbs.web.uploadingfiles.FileUploadController;
import com.example.nbs.web.uploadingfiles.storage.FileSystemStorageService;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    /**
     * お知らせ一覧表示
     */
    @GetMapping
    public String showList(Model model) {

        List<NoticeEntity> noticeList = noticeService.findAll();
        model.addAttribute("noticeList", noticeList);

        return "notice/list";

    }

    /**
     * お知らせ作成フォーム表示
     */
    @GetMapping("/creationForm")
    public String showCreationForm(Model model) {

        model.addAttribute("noticeForm", new NoticeForm());

        // 一時フォルダクリア
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();

        return "notice/creationForm";

    }

    /**
     * お知らせ公開(登録)
     */
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

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF1 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        noticeService.create(Long.parseLong(dtF1), form.getTitle(), form.getContents(), filePathInfo, form.getRequest_for_reply(), dtF2);

        // ファイル保存
        fileSystemStorageService.fileUpload(dtF1);

        return "redirect:/notice";

    }

    /**
     * お知らせ詳細表示
     */
    @GetMapping("/{noticeId}")
    public String showDetail(@PathVariable("noticeId") long noticeId, Model model) {

        model.addAttribute("notice", noticeService.findById(noticeId));

        return "notice/detail";

    }

    /**
     * お知らせ編集フォーム表示
     */
    @GetMapping("/editForm/{noticeId}")
    public String showEditForm(@PathVariable("noticeId") long noticeId, Model model) {

        NoticeEntity noticeEntity = noticeService.findById(noticeId);
        model.addAttribute("notice_Id", noticeId);

        // NoticeFormの新しいインスタンスを作成し、取得したデータを設定
        NoticeForm noticeForm = new NoticeForm();
        noticeForm.setTitle(noticeEntity.getTitle());
        noticeForm.setContents(noticeEntity.getContents());
        noticeForm.setRequest_for_reply(noticeEntity.getRequest_for_reply());

        // NoticeFormをモデルに追加
        model.addAttribute("noticeForm", noticeForm);

        // ファイル情報取得


        return "notice/editForm";

    }

    /**
     * お知らせ公開(更新)
     */
    @PostMapping("/editForm/{noticeId}")
    public String update(@Validated NoticeForm form, BindingResult bindingResult, Model model) {

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

        // ファイル情報取得
        List<Path> filePathInfo = fileSystemStorageService.loadAll().toList();

        // システム日付取得
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        String dtF2 = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // URLを取得
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String[] elements = uri.split("/");
        String lastElement = elements[elements.length - 1];

        noticeService.update(Long.parseLong(lastElement), form.getTitle(), form.getContents(), filePathInfo, form.getRequest_for_reply(), dtF2);

        // ファイル保存
        fileSystemStorageService.fileUpload(lastElement);

        return "redirect:/notice";

    }

    /**
     * お知らせ削除(物理削除)
     */
    @PostMapping("/delete/{noticeId}")
    public String delete(@Validated NoticeForm form, BindingResult bindingResult, Model model) {

        // ファイル情報取得
        List<Path> filePathInfo = fileSystemStorageService.loadAll().toList();

        // URLを取得
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String[] elements = uri.split("/");
        String lastElement = elements[elements.length - 1];

        noticeService.delete(Long.parseLong(lastElement));

        // ファイル削除
//        fileSystemStorageService.fileUpload(lastElement);

        return "redirect:/notice";

    }

}
