package com.example.nbs.web.uploadingfiles;

import com.example.nbs.web.uploadingfiles.storage.StorageFileNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadControllerAdvice {

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException e) {

        return ResponseEntity.notFound().build();

    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> maxUploadSizeException(MaxUploadSizeExceededException e) {

        return ResponseEntity.badRequest().body("【ファイルサイズエラー】 ファイルサイズの上限を超えています。1MB(約1000KB)以下のサイズを選択してください。");

    }

}