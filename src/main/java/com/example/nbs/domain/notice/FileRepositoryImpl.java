package com.example.nbs.domain.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {

    private final FileRepository fileRepository;

    public void insert(long noticeLastId, String filename, String dt) {

        fileRepository.insert(noticeLastId, filename, dt);

    }

    public void delete(long id) {

        fileRepository.delete(id);

    }

}
