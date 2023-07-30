package com.example.nbs.web.uploadingfiles.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void uploadFile(String folderName);

    void deleteFile(List<String> fileNames);

    void downloadFile(String folderName);

    String renameFolder(String beforeFolderName);

    void deleteFolder(String folderName);

    void updateFile(String folderName);

}