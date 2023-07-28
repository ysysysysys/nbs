package com.example.nbs.web.uploadingfiles.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    private final Path rootLocation2;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        this.rootLocation = Paths.get(properties.getLocation());

        this.rootLocation2 = Paths.get(properties.getLocation2());
    }

    @Override
    public void store(MultipartFile file) {

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();

            // 指定されたディレクトリの外側にファイルが保存されるのを防ぐため、保存先パスがルート・ロケーション内にあるかどうかをチェック
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {

        return rootLocation.resolve(filename);

    }

    @Override
    public Resource loadAsResource(String filename) {

        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }

    }

    @Override
    public void deleteAll() {

        FileSystemUtils.deleteRecursively(rootLocation.toFile());

    }

    @Override
    public void init() {

        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }

    }

    /**
     * 一時保存場所から保管場所に移動する
     */
    @Override
    public void uploadFile(String folderName) {

        // 元のフォルダのパス
        String sourceFolderPath = this.rootLocation.toAbsolutePath().toString();
        // 移動先のフォルダのパス
        String destinationFolderPath = this.rootLocation2.toString() + File.separator + folderName;

        File stopDir = new File(destinationFolderPath);
        if (!stopDir.exists()) {
            stopDir.mkdirs(); // ディレクトリの存在をチェックしてなければ作成
        }

        File sourceFolder = new File(sourceFolderPath);
        File[] files = sourceFolder.listFiles(); // 元のフォルダ内のファイル一覧を取得

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) { // ファイルの場合のみ移動する
                    String destinationFilePath = destinationFolderPath + File.separator + file.getName();
                    Path destinationPath = new File(destinationFilePath).toPath();

                    try {
                        Files.move(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING); // ファイルを移動する

                        System.out.println("Moved file: " + file.getName());
                    } catch (IOException e) {
                        System.out.println("Failed to move file: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 一時保存場所から削除する
     */
    @Override
    public void deleteFile(List<String> fileNames) {

        for (String fileName : fileNames) {
            // 元のフォルダのパス
            String sourceFolderPath = this.rootLocation.toAbsolutePath().toString();

            File file = new File(sourceFolderPath + File.separator + fileName);

            // ファイルを削除
            file.delete();
        }

    }

    /**
     * 保管場所から一時保存場所にコピーする
     */
    @Override
    public void downloadFile(String folderName) {

        // 元のフォルダのパス
        String sourceFolderPath = this.rootLocation2.toString() + File.separator + folderName;
        // 移動先のフォルダのパス
        String destinationFolderPath = this.rootLocation.toAbsolutePath().toString();

        File stopDir = new File(destinationFolderPath);
        if (!stopDir.exists()) {
            stopDir.mkdirs(); // ディレクトリの存在をチェックしてなければ作成
        }

        File sourceFolder = new File(sourceFolderPath);
        File[] files = sourceFolder.listFiles(); // 元のフォルダ内のファイル一覧を取得

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) { // ファイルの場合のみコピーする
                    String destinationFilePath = destinationFolderPath + File.separator + file.getName();
                    Path destinationPath = new File(destinationFilePath).toPath();

                    try {
                        Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING); // ファイルを移動する

                        System.out.println("Moved file: " + file.getName());
                    } catch (IOException e) {
                        System.out.println("Failed to move file: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}