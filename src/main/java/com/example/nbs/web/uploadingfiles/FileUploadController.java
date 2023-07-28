package com.example.nbs.web.uploadingfiles;

import com.example.nbs.web.notice.NoticeForm;
import com.example.nbs.web.uploadingfiles.storage.StorageFileNotFoundException;
import com.example.nbs.web.uploadingfiles.storage.StorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * アップロードファイル一覧表示
     */
    @GetMapping("/formAfterUpdate")
    public String listUploadedFiles(Model model, HttpSession session) throws IOException {

        // セッション属性からフォームデータを取得
        String title = (String) session.getAttribute("title");
        String contents = (String) session.getAttribute("contents");
        Integer request_for_reply = (Integer) session.getAttribute("request_for_reply");

        // NoticeFormの新しいインスタンスを作成し、取得したデータを設定
        NoticeForm noticeForm = new NoticeForm();
        noticeForm.setTitle(title);
        noticeForm.setContents(contents);
        noticeForm.setRequest_for_reply(request_for_reply);

        // NoticeFormをモデルに追加
        model.addAttribute("noticeForm", noticeForm);

        // アップロードされたファイルをモデルに追加
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));


        //model.addAllAttributes(model.asMap());

        return "notice/creationForm";

    }

    /**
     * プレビュー表示
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(file.getFilename())
                .build());
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }

    /**
     * ファイルアップロード(一時フォルダに格納)
     */
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @ModelAttribute("noticeForm") NoticeForm noticeForm, RedirectAttributes redirectAttributes, HttpSession session) throws IOException {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        // ファイルのアップロードした後、 セッション属性にフォーム・データを格納
        session.setAttribute("title", noticeForm.getTitle());
        session.setAttribute("contents", noticeForm.getContents());
        session.setAttribute("request_for_reply", noticeForm.getRequest_for_reply());

        return "redirect:/formAfterUpdate";

    }

    /**
     * アップロード取り消し(一時フォルダから削除)
     */
    @PostMapping("/cancel")
    public String handleFileCancel(@RequestParam(value = "selectedFiles", required = false) List<String> selectedFiles, @ModelAttribute("noticeForm") NoticeForm noticeForm, HttpSession session) throws IOException {

        if (selectedFiles != null && !selectedFiles.isEmpty()) {

            // チェックボックスで選択されたファイル名を取得
            List<String> fileNames = new ArrayList<>();
            for (String filePath : selectedFiles) {

                try {
                    // URLデコード(%20余分なスペース削除)
                    String decodedUrl = URLDecoder.decode(filePath, "UTF-8");

                    String[] elements = decodedUrl.split("/");
                    String lastElement = elements[elements.length - 1];

                    fileNames.add(lastElement);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            // ファイル削除
            storageService.deleteFile(fileNames);

        }

        // ファイルの削除した後、 セッション属性にフォーム・データを格納
        session.setAttribute("title", noticeForm.getTitle());
        session.setAttribute("contents", noticeForm.getContents());
        session.setAttribute("request_for_reply", noticeForm.getRequest_for_reply());

        return "redirect:/formAfterUpdate";

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {

        return ResponseEntity.notFound().build();

    }

}