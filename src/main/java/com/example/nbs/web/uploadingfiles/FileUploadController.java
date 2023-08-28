package com.example.nbs.web.uploadingfiles;

import com.example.nbs.web.Global;
import com.example.nbs.web.notice.NoticeForm;
import com.example.nbs.web.uploadingfiles.storage.StorageService;
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
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("loginId", Global.userId);

        // アップロードされたファイルをモデルに追加
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));


        return (Global.h1.equals("お知らせ作成")) ? "notice/creationForm" : "notice/editForm";

    }

    /**
     * プレビュー表示
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        // 拡張子を取得
        String extension = filename.substring(filename.lastIndexOf(".")).replace(".", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(file.getFilename())
                .build());

        switch (extension.toUpperCase()) {
            case "PDF":
                headers.setContentType(MediaType.APPLICATION_PDF);
                break;
            case "JPG", "JPEG":
                headers.setContentType(MediaType.IMAGE_JPEG);
                break;
            default:
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(file);

    }

    /**
     * ファイルアップロード(一時フォルダに格納)
     */
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @ModelAttribute("noticeForm") NoticeForm noticeForm, RedirectAttributes redirectAttributes) throws IOException {

        if (!file.getOriginalFilename().equals("")) {

            if (file.getContentType().equals(MediaType.APPLICATION_PDF.toString()) || file.getContentType().equals(MediaType.IMAGE_JPEG.toString())) {

                storageService.store(file);
                redirectAttributes.addFlashAttribute("message",
                        "正常にアップロードされました。 (" + file.getOriginalFilename() + ")");


            } else {

                redirectAttributes.addFlashAttribute("errorMessage",
                        "PDFファイル(.pdf)、もしくは画像(.JPG)を選択してください。");

            }

        }

        // noticeFormを渡す
        redirectAttributes.addFlashAttribute("noticeForm", noticeForm);

        return "redirect:/formAfterUpdate";

    }

    /**
     * アップロード取り消し(一時フォルダから削除)
     */
    @PostMapping("/cancel")
    public String handleFileCancel(@RequestParam(value = "selectedFiles", required = false) List<String> selectedFiles, @ModelAttribute("noticeForm") NoticeForm noticeForm, RedirectAttributes redirectAttributes) throws IOException {

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

        // noticeFormを渡す
        redirectAttributes.addFlashAttribute("noticeForm", noticeForm);

        return "redirect:/formAfterUpdate";

    }

}