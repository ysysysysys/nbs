package com.example.nbs.domain.draft;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DraftFileRepositoryImpl implements DraftFileRepository {

    private final DraftFileRepository draftFileRepository;

    public void insert(long noticeLastId, String filename, String dt) {

        draftFileRepository.insert(noticeLastId, filename, dt);

    }

    public void delete(long id) {

        draftFileRepository.delete(id);

    }

}
