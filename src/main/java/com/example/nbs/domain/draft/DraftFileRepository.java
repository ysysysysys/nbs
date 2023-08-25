package com.example.nbs.domain.draft;

import org.apache.ibatis.annotations.*;

@Mapper
public interface DraftFileRepository {

    void insert(long noticeLastId, String filename, String dt);

    void delete(long id);

}
